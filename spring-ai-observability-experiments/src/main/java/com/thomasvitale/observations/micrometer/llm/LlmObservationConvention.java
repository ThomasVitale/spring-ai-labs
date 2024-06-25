package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;

/**
 * {@link ObservationConvention} for {@link LlmObservationContext}.
 */
public interface LlmObservationConvention extends ObservationConvention<LlmObservationContext> {

    @Override
    default boolean supportsContext(Observation.Context context) {
        return context instanceof LlmObservationContext;
    }

}
