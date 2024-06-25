package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.tracing.otel.bridge.OtelTracer;

public class LlmTracingObservationHandler implements ObservationHandler<LlmObservationContext> {

    private final OtelTracer tracer;

    public LlmTracingObservationHandler(OtelTracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public void onStop(LlmObservationContext context) {
        tracer.currentSpan();
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return context instanceof LlmObservationContext;
    }
}
