package com.thomasvitale.observations.autoconfigure.integrations.openlit;

import com.thomasvitale.observations.autoconfigure.AiObservationProperties;
import com.thomasvitale.observations.micrometer.integrations.openlit.OpenLitObservationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = AiObservationProperties.CONFIG_PREFIX, name = "semantic-conventions", havingValue = "openlit", matchIfMissing = false)
public class OpenLitAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    OpenLitObservationFilter openLitObservationFilter(Environment environment) {
        return new OpenLitObservationFilter(environment);
    }

}
