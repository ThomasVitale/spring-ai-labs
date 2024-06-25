package com.thomasvitale.observations.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Spring AI observations.
 */
@ConfigurationProperties(AiObservationProperties.CONFIG_PREFIX)
public class AiObservationProperties {

    public static final String CONFIG_PREFIX = "demo.spring.ai.observations";

    /**
     * Whether to include the completion content in the observations.
     */
    private boolean includeCompletion = false;

    /**
     * Whether to include the prompt content in the observations.
     */
    private boolean includePrompt = false;

    /**
     * Conventions to use for naming traces and spans related to Generative AI scenarios.
     */
    private SemanticConventions semanticConventions = SemanticConventions.DEFAULT;

    public boolean isIncludeCompletion() {
        return includeCompletion;
    }

    public void setIncludeCompletion(boolean includeCompletion) {
        this.includeCompletion = includeCompletion;
    }

    public boolean isIncludePrompt() {
        return includePrompt;
    }

    public void setIncludePrompt(boolean includePrompt) {
        this.includePrompt = includePrompt;
    }

    public SemanticConventions getSemanticConventions() {
        return semanticConventions;
    }

    public void setSemanticConventions(SemanticConventions semanticConventions) {
        this.semanticConventions = semanticConventions;
    }

    public enum SemanticConventions {
        DEFAULT,
        OPENLIT,
        OTEL
    }
}
