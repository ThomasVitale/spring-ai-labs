package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.common.KeyValue;
import io.micrometer.common.KeyValues;

public class DefaultLlmObservationConvention implements LlmObservationConvention {

    private static final KeyValue OPERATION_TYPE_NONE = KeyValue.of(LlmObservation.LowCardinalityKeyNames.OPERATION_NAME, KeyValue.NONE_VALUE);

    private static final KeyValue REQUEST_FREQUENCY_PENALTY_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_FREQUENCY_PENALTY, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_IS_STREAM_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_IS_STREAM, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_MAX_TOKENS_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_MAX_TOKENS, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_PRESENCE_PENALTY_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_PRESENCE_PENALTY, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_SEED_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_SEED, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_STOP_SEQUENCES_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_STOP_SEQUENCES, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_TEMPERATURE_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TEMPERATURE, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_TOP_K_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TOP_K, KeyValue.NONE_VALUE);
    private static final KeyValue REQUEST_TOP_P_NONE = KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TOP_P, KeyValue.NONE_VALUE);

    private static final KeyValue RESPONSE_FINISH_REASONS_NONE = KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_FINISH_REASONS, KeyValue.NONE_VALUE);
    private static final KeyValue RESPONSE_ID_NONE = KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_ID, KeyValue.NONE_VALUE);
    private static final KeyValue RESPONSE_MODEL_NONE = KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_MODEL, KeyValue.NONE_VALUE);

    private static final KeyValue USAGE_COMPLETION_TOKENS_NONE = KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COMPLETION_TOKENS, KeyValue.NONE_VALUE);
    private static final KeyValue USAGE_PROMPT_TOKENS_NONE = KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_PROMPT_TOKENS, KeyValue.NONE_VALUE);
    private static final KeyValue USAGE_TOTAL_TOKENS_NONE = KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_TOTAL_TOKENS, KeyValue.NONE_VALUE);
    private static final KeyValue USAGE_COST_NONE = KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COST, KeyValue.NONE_VALUE);

    private static final String DEFAULT_NAME = "llm.operation";

    @Override
    public String getName() {
        return DEFAULT_NAME;
    }

    @Override
    public String getContextualName(LlmObservationContext context) {
        return "%s.%s".formatted(context.getLlmRequest().system(), context.getLlmRequest().operationName());
    }

    @Override
    public KeyValues getLowCardinalityKeyValues(LlmObservationContext context) {
        return KeyValues.of(requestModel(context), genAiOperationType(context), genAiSystem(context));
    }

    protected KeyValue requestModel(LlmObservationContext context) {
        return KeyValue.of(LlmObservation.LowCardinalityKeyNames.REQUEST_MODEL, context.getLlmRequest().model());
    }

    protected KeyValue genAiOperationType(LlmObservationContext context) {
        if (context.getLlmRequest().operationName() != null) {
            return KeyValue.of(LlmObservation.LowCardinalityKeyNames.OPERATION_NAME, context.getLlmRequest().operationName());
        }
        return OPERATION_TYPE_NONE;
    }

    protected KeyValue genAiSystem(LlmObservationContext context) {
        return KeyValue.of(LlmObservation.LowCardinalityKeyNames.SYSTEM, context.getLlmRequest().system());
    }

    @Override
    public KeyValues getHighCardinalityKeyValues(LlmObservationContext context) {
        return KeyValues.of(
                // Request
                requestFrequencyPenalty(context), requestIsStream(context), requestMaxTokens(context), requestPresencePenalty(context),
                requestSeed(context), requestStopSequences(context), requestTemperature(context), requestTopK(context), requestTopP(context),
                // Response
                responseFinishReasons(context), responseId(context), responseModel(context),
                usageCompletionTokens(context), usagePromptTokens(context), usageTotalTokens(context), usageCost(context));
    }

    // Request

    protected KeyValue requestFrequencyPenalty(LlmObservationContext context) {
        if (context.getLlmRequest().frequencyPenalty() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_FREQUENCY_PENALTY, String.valueOf(context.getLlmRequest().frequencyPenalty()));
        }
        return REQUEST_FREQUENCY_PENALTY_NONE;
    }

    protected KeyValue requestIsStream(LlmObservationContext context) {
        if (context.getLlmRequest().isStream() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_IS_STREAM, String.valueOf(context.getLlmRequest().isStream()));
        }
        return REQUEST_IS_STREAM_NONE;
    }

    protected KeyValue requestMaxTokens(LlmObservationContext context) {
        if (context.getLlmRequest().maxTokens() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_MAX_TOKENS, String.valueOf(context.getLlmRequest().maxTokens()));
        }
        return REQUEST_MAX_TOKENS_NONE;
    }

    protected KeyValue requestPresencePenalty(LlmObservationContext context) {
        if (context.getLlmRequest().presencePenalty() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_PRESENCE_PENALTY, String.valueOf(context.getLlmRequest().presencePenalty()));
        }
        return REQUEST_PRESENCE_PENALTY_NONE;
    }

    protected KeyValue requestSeed(LlmObservationContext context) {
        if (context.getLlmRequest().seed() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_SEED, String.valueOf(context.getLlmRequest().seed()));
        }
        return REQUEST_SEED_NONE;
    }

    protected KeyValue requestStopSequences(LlmObservationContext context) {
        if (context.getLlmRequest().stopSequences() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_STOP_SEQUENCES, String.join(",", context.getLlmRequest().stopSequences()));
        }
        return REQUEST_STOP_SEQUENCES_NONE;
    }

    protected KeyValue requestTemperature(LlmObservationContext context) {
        if (context.getLlmRequest().temperature() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TEMPERATURE, String.valueOf(context.getLlmRequest().temperature()));
        }
        return REQUEST_TEMPERATURE_NONE;
    }

    protected KeyValue requestTopK(LlmObservationContext context) {
        if (context.getLlmRequest().topK() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TOP_K, String.valueOf(context.getLlmRequest().topK()));
        }
        return REQUEST_TOP_K_NONE;
    }

    protected KeyValue requestTopP(LlmObservationContext context) {
        if (context.getLlmRequest().topP() != null) {
            return KeyValue.of(LlmObservation.RequestHighCardinalityKeyNames.REQUEST_TOP_P, String.valueOf(context.getLlmRequest().topP()));
        }
        return REQUEST_TOP_P_NONE;
    }

    // Response

    protected KeyValue responseFinishReasons(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().finishReasons() != null) {
            return KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_FINISH_REASONS, String.join(",", context.getLlmResponse().finishReasons()));
        }
        return RESPONSE_FINISH_REASONS_NONE;
    }

    protected KeyValue responseId(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().responseId() != null) {
            return KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_ID, context.getLlmResponse().responseId());
        }
        return RESPONSE_ID_NONE;
    }

    protected KeyValue responseModel(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().responseModel() != null) {
            return KeyValue.of(LlmObservation.ResponseHighCardinalityKeyNames.RESPONSE_MODEL, context.getLlmResponse().responseModel());
        }
        return RESPONSE_MODEL_NONE;
    }

    protected KeyValue usageCompletionTokens(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().completionTokens() != null) {
            return KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COMPLETION_TOKENS, String.valueOf(context.getLlmResponse().completionTokens()));
        }
        return USAGE_COMPLETION_TOKENS_NONE;
    }

    protected KeyValue usagePromptTokens(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().promptTokens() != null) {
            return KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_PROMPT_TOKENS, String.valueOf(context.getLlmResponse().promptTokens()));
        }
        return USAGE_PROMPT_TOKENS_NONE;
    }

    protected KeyValue usageTotalTokens(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().totalTokens() != null) {
            return KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_TOTAL_TOKENS, String.valueOf(context.getLlmResponse().totalTokens()));
        }
        return USAGE_TOTAL_TOKENS_NONE;
    }

    protected KeyValue usageCost(LlmObservationContext context) {
        if (context.getLlmResponse() != null && context.getLlmResponse().cost() != null) {
            return KeyValue.of(LlmObservation.UsageHighCardinalityKeyNames.USAGE_COST, String.valueOf(context.getLlmResponse().cost()));
        }
        return USAGE_COST_NONE;
    }

}
