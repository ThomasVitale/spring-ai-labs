package com.thomasvitale;

import com.thomasvitale.observations.springai.models.openai.ObservedOpenAiChatModel;
import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.autoconfigure.openai.OpenAiChatProperties;
import org.springframework.ai.autoconfigure.openai.OpenAiConnectionProperties;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SpringAiObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiObservabilityApplication.class, args);
	}

	@Bean
	public ObservedOpenAiChatModel openAiChatModel(OpenAiConnectionProperties commonProperties,
												   OpenAiChatProperties chatProperties, RestClient.Builder restClientBuilder,
												   WebClient.Builder webClientBuilder, List<FunctionCallback> toolFunctionCallbacks,
												   FunctionCallbackContext functionCallbackContext, RetryTemplate retryTemplate,
												   ResponseErrorHandler responseErrorHandler, ObservationRegistry observationRegistry) {
		var openAiApi = openAiApi(chatProperties.getBaseUrl(), commonProperties.getBaseUrl(),
				chatProperties.getApiKey(), commonProperties.getApiKey(), restClientBuilder, webClientBuilder,
				responseErrorHandler);

		if (!CollectionUtils.isEmpty(toolFunctionCallbacks)) {
			chatProperties.getOptions().getFunctionCallbacks().addAll(toolFunctionCallbacks);
		}

		return new ObservedOpenAiChatModel(openAiApi, chatProperties.getOptions(), functionCallbackContext, retryTemplate, observationRegistry);
	}

	private OpenAiApi openAiApi(String baseUrl, String commonBaseUrl, String apiKey, String commonApiKey,
								RestClient.Builder restClientBuilder, WebClient.Builder webClientBuilder,
								ResponseErrorHandler responseErrorHandler) {

		String resolvedBaseUrl = StringUtils.hasText(baseUrl) ? baseUrl : commonBaseUrl;
		Assert.hasText(resolvedBaseUrl, "OpenAI base URL must be set");

		String resolvedApiKey = StringUtils.hasText(apiKey) ? apiKey : commonApiKey;
		Assert.hasText(resolvedApiKey, "OpenAI API key must be set");

		return new OpenAiApi(resolvedBaseUrl, resolvedApiKey, restClientBuilder, webClientBuilder,
				responseErrorHandler);
	}

}
