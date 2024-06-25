package com.thomasvitale.observations.micrometer.llm;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Contextual data to observe in an LLM request.
 */
public record LlmRequest(
        String system,
        String model,
        @Nullable
        String operationName,
        @Nullable
        Float frequencyPenalty,
        @Nullable
        Boolean isStream,
        @Nullable
        Integer maxTokens,
        @Nullable
        Float presencePenalty,
        @Nullable
        Integer seed,
        @Nullable
        List<String> stopSequences,
        @Nullable
        Float temperature,
        @Nullable
        Float topK,
        @Nullable
        Float topP,
        @Nullable
        String prompt
) {

    public LlmRequest {
        Assert.hasText(system, "system cannot be null or empty");
        Assert.hasText(model, "model cannot be null or empty");
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String system;
        private String model;
        private String operationName;
        private Float frequencyPenalty;
        private Boolean isStream;
        private Integer maxTokens;
        private Float presencePenalty;
        private Integer seed;
        private List<String> stopSequences;
        private Float temperature;
        private Float topK;
        private Float topP;
        private String prompt;

        private Builder() {}

        public Builder system(String system) {
            this.system = system;
            return this;
        }

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder operationName(String operationName) {
            this.operationName = operationName;
            return this;
        }

        public Builder frequencyPenalty(Float frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder isStream(Boolean isStream) {
            this.isStream = isStream;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder presencePenalty(Float presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder seed(Integer seed) {
            this.seed = seed;
            return this;
        }

        public Builder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        public Builder temperature(Float temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder topK(Float topK) {
            this.topK = topK;
            return this;
        }

        public Builder topP(Float topP) {
            this.topP = topP;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public LlmRequest build() {
            return new LlmRequest(
                    system,
                    model,
                    operationName,
                    frequencyPenalty,
                    isStream,
                    maxTokens,
                    presencePenalty,
                    seed,
                    stopSequences,
                    temperature,
                    topK,
                    topP,
                    prompt
            );
        }
    }

}
