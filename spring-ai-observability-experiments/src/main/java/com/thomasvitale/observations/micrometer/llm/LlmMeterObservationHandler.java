package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.common.KeyValue;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LlmMeterObservationHandler implements ObservationHandler<LlmObservationContext> {

    private final MeterRegistry meterRegistry;

    public LlmMeterObservationHandler(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void onStop(LlmObservationContext context) {
        Counter.builder("gen_ai.total.requests")
                .description("Total number of requests to GenAI")
                .tags(createTags(context))
                .register(meterRegistry)
                .increment();

        Counter.builder(LlmObservation.UsageHighCardinalityKeyNames.USAGE_PROMPT_TOKENS.asString())
                .description("Number of prompt tokens used in the operation.")
                .tags(createTags(context))
                .register(meterRegistry)
                .increment(context.getLlmResponse() != null ? Objects.requireNonNullElse(context.getLlmResponse().promptTokens(), 0) : 0);

        Counter.builder(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COMPLETION_TOKENS.asString())
                .description("Number of completion tokens used in the operation.")
                .tags(createTags(context))
                .register(meterRegistry)
                .increment(context.getLlmResponse() != null ? Objects.requireNonNullElse(context.getLlmResponse().completionTokens(), 0) : 0);

        Counter.builder(LlmObservation.UsageHighCardinalityKeyNames.USAGE_TOTAL_TOKENS.asString())
                .description("Total number of tokens used in the operation.")
                .tags(createTags(context))
                .register(meterRegistry)
                .increment(context.getLlmResponse() != null ? Objects.requireNonNullElse(context.getLlmResponse().totalTokens(), 0) : 0);

        DistributionSummary.builder(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COST.asString())
                .description("Distribution of GenAI request costs.")
                .baseUnit("USD")
                .publishPercentileHistogram()
                .publishPercentiles()
                .tags(createTags(context))
                .register(meterRegistry);
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return context instanceof LlmObservationContext;
    }

    private List<Tag> createTags(Observation.Context context) {
        List<Tag> tags = new ArrayList<>();
        for (KeyValue keyValue : context.getLowCardinalityKeyValues()) {
            tags.add(Tag.of(keyValue.getKey(), keyValue.getValue()));
        }
        return tags;
    }

}
