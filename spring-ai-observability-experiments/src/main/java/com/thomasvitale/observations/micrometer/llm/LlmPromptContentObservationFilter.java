package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

/**
 * An {@link ObservationFilter} to include the prompt content in the observation.
 */
public class LlmPromptContentObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof LlmObservationContext llmObservationContext)) {
            return context;
        }

        if (llmObservationContext.getLlmRequest().prompt() == null) {
            return llmObservationContext;
        }

        llmObservationContext.addHighCardinalityKeyValue(
                LlmObservation.ContentHighCardinalityKeyNames.PROMPT.withValue(llmObservationContext.getLlmRequest().prompt())
        );

        return llmObservationContext;
    }

}
