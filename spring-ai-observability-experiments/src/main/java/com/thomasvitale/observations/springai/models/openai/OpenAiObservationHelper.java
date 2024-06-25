package com.thomasvitale.observations.springai.models.openai;

import com.thomasvitale.observations.micrometer.common.AiOperationNames;
import com.thomasvitale.observations.micrometer.llm.LlmRequest;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.model.Model;
import org.springframework.ai.openai.api.OpenAiApi;

class OpenAiObservationHelper {

    static final String SYSTEM = "openai";

    static LlmRequest llmRequest(OpenAiApi.ChatCompletionRequest request, Prompt prompt, Model<?,?> modelClass) {
        return LlmRequest.builder()
                .system(OpenAiObservationHelper.SYSTEM)
                .model(request.model())
                .operationName(operationName(modelClass))
                .frequencyPenalty(request.frequencyPenalty())
                .isStream(request.stream())
                .maxTokens(request.maxTokens())
                .presencePenalty(request.presencePenalty())
                .seed(request.seed())
                .stopSequences(request.stop())
                .temperature(request.temperature())
                .topP(request.topP())
                .prompt(prompt.getContents())
                .build();
    }

    static String operationName(Model<?,?> modelClass) {
        return switch(modelClass) {
            case ChatModel _ -> AiOperationNames.CHAT;
            case ImageModel _ -> AiOperationNames.IMAGE;
            default -> throw new IllegalStateException("Unexpected value: " + modelClass);
        };
    }
}
