package com.thomasvitale.observations.springai.models.openai;

import com.thomasvitale.observations.micrometer.llm.DefaultLlmObservationConvention;
import com.thomasvitale.observations.micrometer.llm.LlmObservation;
import com.thomasvitale.observations.micrometer.llm.LlmObservationContext;
import com.thomasvitale.observations.micrometer.llm.LlmObservationConvention;
import com.thomasvitale.observations.springai.llm.chat.ChatModelObservationHelper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.metadata.ChatGenerationMetadata;
import org.springframework.ai.chat.metadata.RateLimit;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.metadata.OpenAiChatResponseMetadata;
import org.springframework.ai.openai.metadata.support.OpenAiResponseHeaderExtractor;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeType;

import java.util.*;

public class ObservedOpenAiChatModel extends OpenAiChatModel {

    private static final Logger logger = LoggerFactory.getLogger(ObservedOpenAiChatModel.class);

    private static final LlmObservationConvention DEFAULT_OBSERVATION_CONVENTION = new DefaultLlmObservationConvention();

    /**
     * The default options used for the chat completion requests.
     */
    private final OpenAiChatOptions defaultOptions;

    /**
     * The retry template used to retry the OpenAI API calls.
     */
    private final RetryTemplate retryTemplate;

    private final ObservationRegistry observationRegistry;
    private LlmObservationConvention observationConvention = DEFAULT_OBSERVATION_CONVENTION;

    public ObservedOpenAiChatModel(OpenAiApi openAiApi, OpenAiChatOptions options, FunctionCallbackContext functionCallbackContext, RetryTemplate retryTemplate, ObservationRegistry observationRegistry) {
        super(openAiApi, options, functionCallbackContext, retryTemplate);
        Assert.notNull(options, "Options must not be null");
        Assert.notNull(retryTemplate, "RetryTemplate must not be null");
        this.defaultOptions = options;
        this.retryTemplate = retryTemplate;
        this.observationRegistry = observationRegistry;
    }

    @Override
    public ChatResponse call(Prompt prompt) {
        OpenAiApi.ChatCompletionRequest request = createRequest(prompt, false);

        LlmObservationContext observationContext = new LlmObservationContext(OpenAiObservationHelper.llmRequest(request, prompt, this));
        Observation observation = LlmObservation.LLM_OPERATION.observation(null, observationConvention,
                () -> observationContext, observationRegistry);

        return observation.observe(() -> {
            ChatResponse response = this.retryTemplate.execute(ctx -> {

                ResponseEntity<OpenAiApi.ChatCompletion> completionEntity = this.callWithFunctionSupport(request);

                var chatCompletion = completionEntity.getBody();
                if (chatCompletion == null) {
                    logger.warn("No chat completion returned for prompt: {}", prompt);
                    return new ChatResponse(List.of());
                }

                RateLimit rateLimits = OpenAiResponseHeaderExtractor.extractAiResponseHeaders(completionEntity);

                List<OpenAiApi.ChatCompletion.Choice> choices = chatCompletion.choices();
                if (choices == null) {
                    logger.warn("No choices returned for prompt: {}", prompt);
                    return new ChatResponse(List.of());
                }

                List<Generation> generations = choices.stream().map(choice -> {
                    return new Generation(choice.message().content(), toMap(chatCompletion.id(), choice))
                            .withGenerationMetadata(ChatGenerationMetadata.from(choice.finishReason().name(), null));
                }).toList();

                return new ChatResponse(generations,
                        OpenAiChatResponseMetadata.from(completionEntity.getBody()).withRateLimit(rateLimits));
            });

            observationContext.setLlmResponse(ChatModelObservationHelper.buildLlmResponseFrom(response));

            return response;
        });
    }

    private Map<String, Object> toMap(String id, OpenAiApi.ChatCompletion.Choice choice) {
        Map<String, Object> map = new HashMap<>();

        var message = choice.message();
        if (message.role() != null) {
            map.put("role", message.role().name());
        }
        if (choice.finishReason() != null) {
            map.put("finishReason", choice.finishReason().name());
        }
        map.put("id", id);
        return map;
    }

    OpenAiApi.ChatCompletionRequest createRequest(Prompt prompt, boolean stream) {

        Set<String> functionsForThisRequest = new HashSet<>();

        List<OpenAiApi.ChatCompletionMessage> chatCompletionMessages = prompt.getInstructions().stream().map(m -> {
            Object content;
            if (CollectionUtils.isEmpty(m.getMedia())) {
                content = m.getContent();
            }
            else {
                List<OpenAiApi.ChatCompletionMessage.MediaContent> contentList = new ArrayList<>(List.of(new OpenAiApi.ChatCompletionMessage.MediaContent(m.getContent())));

                contentList.addAll(m.getMedia()
                        .stream()
                        .map(media -> new OpenAiApi.ChatCompletionMessage.MediaContent(
                                new OpenAiApi.ChatCompletionMessage.MediaContent.ImageUrl(this.fromMediaData(media.getMimeType(), media.getData()))))
                        .toList());

                content = contentList;
            }

            return new OpenAiApi.ChatCompletionMessage(content, OpenAiApi.ChatCompletionMessage.Role.valueOf(m.getMessageType().name()));
        }).toList();

        OpenAiApi.ChatCompletionRequest request = new OpenAiApi.ChatCompletionRequest(chatCompletionMessages, stream);

        if (prompt.getOptions() != null) {
            OpenAiChatOptions updatedRuntimeOptions = ModelOptionsUtils.copyToTarget(prompt.getOptions(),
                    ChatOptions.class, OpenAiChatOptions.class);

            Set<String> promptEnabledFunctions = this.handleFunctionCallbackConfigurations(updatedRuntimeOptions,
                    IS_RUNTIME_CALL);
            functionsForThisRequest.addAll(promptEnabledFunctions);

            request = ModelOptionsUtils.merge(updatedRuntimeOptions, request, OpenAiApi.ChatCompletionRequest.class);
        }

        if (this.defaultOptions != null) {

            Set<String> defaultEnabledFunctions = this.handleFunctionCallbackConfigurations(this.defaultOptions,
                    !IS_RUNTIME_CALL);

            functionsForThisRequest.addAll(defaultEnabledFunctions);

            request = ModelOptionsUtils.merge(request, this.defaultOptions, OpenAiApi.ChatCompletionRequest.class);
        }

        // Add the enabled functions definitions to the request's tools parameter.
        if (!CollectionUtils.isEmpty(functionsForThisRequest)) {

            request = ModelOptionsUtils.merge(
                    OpenAiChatOptions.builder().withTools(this.getFunctionTools(functionsForThisRequest)).build(),
                    request, OpenAiApi.ChatCompletionRequest.class);
        }

        return request;
    }

    private String fromMediaData(MimeType mimeType, Object mediaContentData) {
        if (mediaContentData instanceof byte[] bytes) {
            // Assume the bytes are an image. So, convert the bytes to a base64 encoded
            // following the prefix pattern.
            return String.format("data:%s;base64,%s", mimeType.toString(), Base64.getEncoder().encodeToString(bytes));
        }
        else if (mediaContentData instanceof String text) {
            // Assume the text is a URLs or a base64 encoded image prefixed by the user.
            return text;
        }
        else {
            throw new IllegalArgumentException(
                    "Unsupported media data type: " + mediaContentData.getClass().getSimpleName());
        }
    }

    private List<OpenAiApi.FunctionTool> getFunctionTools(Set<String> functionNames) {
        return this.resolveFunctionCallbacks(functionNames).stream().map(functionCallback -> {
            var function = new OpenAiApi.FunctionTool.Function(functionCallback.getDescription(),
                    functionCallback.getName(), functionCallback.getInputTypeSchema());
            return new OpenAiApi.FunctionTool(function);
        }).toList();
    }

    /**
     * Use the provided convention for reporting observation data
     * @param observationConvention The provided convention
     */
    public void setObservationConvention(LlmObservationConvention observationConvention) {
        Assert.notNull(observationConvention, "observationConvention cannot be null");
        this.observationConvention = observationConvention;
    }

}
