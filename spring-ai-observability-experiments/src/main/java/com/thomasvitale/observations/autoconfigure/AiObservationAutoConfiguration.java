package com.thomasvitale.observations.autoconfigure;

import com.thomasvitale.observations.micrometer.llm.LlmCompletionContentObservationFilter;
import com.thomasvitale.observations.micrometer.llm.LlmMeterObservationHandler;
import com.thomasvitale.observations.micrometer.llm.LlmPromptContentObservationFilter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Spring AI observations.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ AiObservationProperties.class })
public class AiObservationAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AiObservationAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(MeterRegistry.class)
    LlmMeterObservationHandler llmMeterObservationHandler(MeterRegistry meterRegistry) {
        return new LlmMeterObservationHandler(meterRegistry);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = AiObservationProperties.CONFIG_PREFIX, name = "include-prompt", havingValue = "true", matchIfMissing = false)
    LlmPromptContentObservationFilter llmPromptContentObservationFilter() {
        logger.warn("You have enabled the inclusion of the prompt content in the observations, with the risk of exposing sensitive or private information. Please, be careful!");
        return new LlmPromptContentObservationFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = AiObservationProperties.CONFIG_PREFIX, name = "include-completion", havingValue = "true", matchIfMissing = false)
    LlmCompletionContentObservationFilter llmCompletionContentObservationFilter() {
        logger.warn("You have enabled the inclusion of the completion content in the observations, with the risk of exposing sensitive or private information. Please, be careful!");
        return new LlmCompletionContentObservationFilter();
    }

}
