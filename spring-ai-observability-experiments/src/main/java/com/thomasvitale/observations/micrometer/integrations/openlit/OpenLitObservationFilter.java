package com.thomasvitale.observations.micrometer.integrations.openlit;

import com.thomasvitale.observations.micrometer.common.AiObservationContext;
import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.springframework.core.env.Environment;

/**
 * An {@link ObservationFilter} to update the generative AI observation context
 * and make the produced telemetry data compatible with <a href="https://openlit.io">OpenLit.</a>
 */
public class OpenLitObservationFilter implements ObservationFilter {

    private final Environment environment;

    public OpenLitObservationFilter(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Observation.Context map(Observation.Context context) {
        if (!(context instanceof AiObservationContext)) {
            return context;
        }

        // See:
        context.addLowCardinalityKeyValues(KeyValues.of(
                KeyValue.of("gen_ai.application_name", environment.getProperty("spring.application.name", "default")),
                KeyValue.of("gen_ai.environment", "default"),
                KeyValue.of("telemetry.sdk.name", "openlit")
        ));

        return context;
    }
}
