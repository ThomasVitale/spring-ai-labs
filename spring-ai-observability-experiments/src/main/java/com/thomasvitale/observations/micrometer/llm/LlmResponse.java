package com.thomasvitale.observations.micrometer.llm;

import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Contextual data to observe in an LLM response.
 */
public record LlmResponse(
        @Nullable
        List<String> finishReasons,
        @Nullable
        String responseId,
        @Nullable
        String responseImage,
        @Nullable
        String responseModel,
        @Nullable
        Integer completionTokens,
        @Nullable
        Integer promptTokens,
        @Nullable
        Integer totalTokens,
        @Nullable
        Double cost,
        @Nullable
        String completion
) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<String> finishReasons;
        private String responseId;
        private String responseImage;
        private String responseModel;
        private Integer completionTokens;
        private Integer promptTokens;
        private Integer totalTokens;
        private Double cost;
        private String completion;

        private Builder() {}

        public Builder finishReasons(List<String> finishReasons) {
            this.finishReasons = finishReasons;
            return this;
        }

        public Builder responseId(String responseId) {
            this.responseId = responseId;
            return this;
        }

        public Builder responseImage(String responseImage) {
            this.responseImage = responseImage;
            return this;
        }

        public Builder responseModel(String responseModel) {
            this.responseModel = responseModel;
            return this;
        }

        public Builder completionTokens(Integer completionTokens) {
            this.completionTokens = completionTokens;
            return this;
        }

        public Builder promptTokens(Integer promptTokens) {
            this.promptTokens = promptTokens;
            return this;
        }

        public Builder totalTokens(Integer totalTokens) {
            this.totalTokens = totalTokens;
            return this;
        }

        public Builder cost(Double cost) {
            this.cost = cost;
            return this;
        }

        public Builder completion(String completion) {
            this.completion = completion;
            return this;
        }

        public LlmResponse build() {
            return new LlmResponse(
                    finishReasons,
                    responseId,
                    responseImage,
                    responseModel,
                    completionTokens,
                    promptTokens,
                    totalTokens,
                    cost,
                    completion
            );
        }
    }

}
