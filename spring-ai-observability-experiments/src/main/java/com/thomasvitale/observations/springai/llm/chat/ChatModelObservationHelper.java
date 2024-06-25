package com.thomasvitale.observations.springai.llm.chat;

import com.thomasvitale.observations.micrometer.llm.LlmResponse;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.metadata.OpenAiChatResponseMetadata;

import java.util.List;

public class ChatModelObservationHelper {

    public static LlmResponse buildLlmResponseFrom(ChatResponse chatResponse) {
        return LlmResponse.builder()
                .finishReasons(List.of(chatResponse.getResult().getMetadata().getFinishReason().toLowerCase()))
                .responseId(((OpenAiChatResponseMetadata) chatResponse.getMetadata()).getId())
                .responseModel(null)
                .completionTokens(Integer.parseInt(chatResponse.getMetadata().getUsage().getGenerationTokens().toString()))
                .promptTokens(Integer.parseInt(chatResponse.getMetadata().getUsage().getPromptTokens().toString()))
                .totalTokens(Integer.parseInt(chatResponse.getMetadata().getUsage().getTotalTokens().toString()))
                .cost(null)
                .completion(chatResponse.getResult().getOutput().getContent())
                .build();
    }

}
