package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;

/**
 * An {@link ObservationFilter} to include the completion content in the observation.
 */
public class LlmCompletionContentObservationFilter implements ObservationFilter {

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof LlmObservationContext llmObservationContext)) {
            return context;
        }

        if (llmObservationContext.getLlmResponse() == null || llmObservationContext.getLlmResponse().completion() == null) {
            return llmObservationContext;
        }

        llmObservationContext.addHighCardinalityKeyValue(
                LlmObservation.ContentHighCardinalityKeyNames.COMPLETION.withValue(llmObservationContext.getLlmResponse().completion())
        );

        return llmObservationContext;
    }

}
