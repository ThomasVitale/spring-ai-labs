package com.thomasvitale.observations.micrometer.llm;

import com.thomasvitale.observations.micrometer.common.AiObservationContext;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Observation context for large language model interactions.
 */
public class LlmObservationContext extends AiObservationContext {

    private LlmRequest llmRequest;
    @Nullable
    private LlmResponse llmResponse;

    public LlmObservationContext(LlmRequest llmRequest) {
        this(llmRequest, null);
    }

    public LlmObservationContext(LlmRequest llmRequest, @Nullable LlmResponse llmResponse) {
        Assert.notNull(llmRequest, "llmRequest cannot be null");
        this.llmRequest = llmRequest;
        this.llmResponse = llmResponse;
    }

    public LlmRequest getLlmRequest() {
        return llmRequest;
    }

    public void setLlmRequest(LlmRequest llmRequest) {
        this.llmRequest = llmRequest;
    }

    @Nullable
    public LlmResponse getLlmResponse() {
        return llmResponse;
    }

    public void setLlmResponse(@Nullable LlmResponse llmResponse) {
        this.llmResponse = llmResponse;
    }

}
