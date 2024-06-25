package com.thomasvitale.observations.micrometer.llm;

import io.micrometer.common.docs.KeyName;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationConvention;
import io.micrometer.observation.docs.ObservationDocumentation;

/**
 * Documented semantic conventions for LLM observations.
 */
public enum LlmObservation implements ObservationDocumentation {

    LLM_OPERATION {
        @Override
        public Class<? extends ObservationConvention<? extends Observation.Context>> getDefaultConvention() {
            return DefaultLlmObservationConvention.class;
        }

        @Override
        public KeyName[] getLowCardinalityKeyNames() {
            return LowCardinalityKeyNames.values();
        }

        @Override
        public KeyName[] getHighCardinalityKeyNames() {
            return KeyName.merge(
                    RequestHighCardinalityKeyNames.values(),
                    ResponseHighCardinalityKeyNames.values(),
                    UsageHighCardinalityKeyNames.values(),
                    ContentHighCardinalityKeyNames.values()
            );
        }

        @Override
        public Observation.Event[] getEvents() {
            return Events.values();
        }
    };

    /**
     * Low-cardinality observation key names for LLM operations.
     */
    public enum LowCardinalityKeyNames implements KeyName {

        /**
         * Name of the AI system the client is using.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        SYSTEM {
            @Override
            public String asString() {
                return "gen_ai.system";
            }
        },

        /**
         * Type of AI operation performed by the client.
         * Used by: OTEL, OpenLit. OpenLLMetry => llm.request.type
         */
        OPERATION_NAME {
            @Override
            public String asString() {
                return "gen_ai.operation.name";
            }
        },

        /**
         * Name of the model the request is sent to.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        REQUEST_MODEL {
            @Override
            public String asString() {
                return "gen_ai.request.model";
            }
        }
    }

    /**
     * LLM request, high-cardinality observation key names for LLM operations.
     */
    public enum RequestHighCardinalityKeyNames implements KeyName {

        /**
         * Frequency penalty setting for the model request.
         * Used by: OTEL, OpenLit. OpenLLMetry => llm.frequency_penalty
         */
        REQUEST_FREQUENCY_PENALTY {
            @Override
            public String asString() {
                return "gen_ai.request.frequency_penalty";
            }
        },

        /**
         * Indicates if the request to the model is a stream.
         * Used by: OpenLit. OpenLLMetry => llm.is_streaming
         */
        REQUEST_IS_STREAM {
            @Override
            public String asString() {
                return "gen_ai.request.is_stream";
            }
        },

        /**
         * Maximum number of tokens the model generates for a request.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        REQUEST_MAX_TOKENS {
            @Override
            public String asString() {
                return "gen_ai.request.max_tokens";
            }
        },

        /**
         * Presence penalty setting for the model request.
         * Used by: OTEL, OpenLit. OpenLLMetry => llm.presence_penalty
         */
        REQUEST_PRESENCE_PENALTY {
            @Override
            public String asString() {
                return "gen_ai.request.presence_penalty";
            }
        },

        /**
         * Seed used to generate deterministic results from the model.
         * Used by: OpenLit.
         */
        REQUEST_SEED {
            @Override
            public String asString() {
                return "gen_ai.request.seed";
            }
        },

        /**
         * List of sequences that the model will use to stop generating further tokens.
         * Used by: OTEL. OpenLLMetry => llm.chat.stop_sequences
         */
        REQUEST_STOP_SEQUENCES {
            @Override
            public String asString() {
                return "gen_ai.request.stop_sequences";
            }
        },

        /**
         * Temperature setting for the model request.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        REQUEST_TEMPERATURE {
            @Override
            public String asString() {
                return "gen_ai.request.temperature";
            }
        },

        /**
         * Top-K sampling setting for the model request.
         * Used by: OTEL, OpenLit.
         */
        REQUEST_TOP_K {
            @Override
            public String asString() {
                return "gen_ai.request.top_k";
            }
        },

        /**
         * Top-P sampling setting for the model request.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        REQUEST_TOP_P {
            @Override
            public String asString() {
                return "gen_ai.request.top_p";
            }
        }
    }

    /**
     * LLM response, high-cardinality observation key names for LLM operations.
     */
    public enum ResponseHighCardinalityKeyNames implements KeyName {

        /**
         * Reasons the model stopped generating tokens, corresponding to each completion received.
         * Used by: OTEL, OpenLit.
         */
        RESPONSE_FINISH_REASONS {
            @Override
            public String asString() {
                return "gen_ai.response.finish_reasons";
            }
        },

        /**
         * Unique identifier for the completion.
         * Used by: OTEL, OpenLit.
         */
        RESPONSE_ID {
            @Override
            public String asString() {
                return "gen_ai.response.id";
            }
        },

        /**
         * Image content generated by the LLM.
         * Used by: OpenLit.
         */
        RESPONSE_IMAGE {
            @Override
            public String asString() {
                return "gen_ai.response.image";
            }
        },

        /**
         * Name of the model that generated the response.
         * Used by: OTEL, OpenLLMetry.
         */
        RESPONSE_MODEL {
            @Override
            public String asString() {
                return "gen_ai.response.model";
            }
        }
    }

    /**
     * LLM usage, high-cardinality observation key names for LLM operations.
     */
    public enum UsageHighCardinalityKeyNames implements KeyName {

        /**
         * Number of tokens used in the model response (completion).
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        USAGE_COMPLETION_TOKENS {
            @Override
            public String asString() {
                return "gen_ai.usage.completion_tokens";
            }
        },

        /**
         * Number of tokens used in the model input or prompt.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        USAGE_PROMPT_TOKENS {
            @Override
            public String asString() {
                return "gen_ai.usage.prompt_tokens";
            }
        },

        /**
         * Total number of tokens used in both the prompt and completion.
         * Used by: OpenLit. OpenLLMetry => llm.usage.total_tokens
         */
        USAGE_TOTAL_TOKENS {
            @Override
            public String asString() {
                return "gen_ai.usage.total_tokens";
            }
        },

        /**
         * Cost associated with the model request.
         * Used by: OpenLit.
         */
        USAGE_COST {
            @Override
            public String asString() {
                return "gen_ai.usage.cost";
            }
        }
    }

    /**
     * LLM content, high-cardinality observation key names for LLM operations.
     */
    public enum ContentHighCardinalityKeyNames implements KeyName {

        /**
         * The full prompt sent to the model.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        PROMPT {
            @Override
            public String asString() {
                return "gen_ai.prompt";
            }
        },

        /**
         * The full response received from the model.
         * Used by: OTEL, OpenLit, OpenLLMetry.
         */
        COMPLETION {
            @Override
            public String asString() {
                return "gen_ai.completion";
            }
        }
    }

    /**
     * Need to be tested and vetted yet.
     */
    public enum OtherHighCardinalityKeyNames implements KeyName {

        /**
         * Dimensionality of the generated embeddings.
         * Used by: OpenLit.
         */
        REQUEST_EMBEDDING_DIMENSION {
            @Override
            public String asString() {
                return "gen_ai.request.embedding_dimension";
            }
        },

        /**
         * Format of embeddings requested from the model.
         * Used by: OpenLit.
         */
        REQUEST_EMBEDDING_FORMAT {
            @Override
            public String asString() {
                return "gen_ai.request.embedding_format";
            }
        },

        /**
         * Username or identifier for the user making the request.
         * Used by: OpenLit. OpenLLMetry => llm.user
         */
        REQUEST_USER {
            @Override
            public String asString() {
                return "gen_ai.request.user";
            }
        },

        /**
         * Tool or library chosen for processing the LLM request.
         * Used by: OpenLit.
         */
        REQUEST_TOOL_CHOICE {
            @Override
            public String asString() {
                return "gen_ai.request.tool_choice";
            }
        },

        /**
         * Voice setting for audio generated by the model.
         * Used by: OpenLit.
         */
        REQUEST_AUDIO_VOICE {
            @Override
            public String asString() {
                return "gen_ai.request.audio_voice";
            }
        },

        /**
         * Format of the audio response from the model.
         * Used by: OpenLit.
         */
        REQUEST_AUDIO_RESPONSE_FORMAT {
            @Override
            public String asString() {
                return "gen_ai.request.audio_response_format";
            }
        },

        /**
         * Speed setting for audio responses generated by the model.
         * Used by: OpenLit.
         */
        REQUEST_AUDIO_SPEED {
            @Override
            public String asString() {
                return "gen_ai.request.audio_response_format";
            }
        },

        /**
         * Size specification for the generated image.
         * Used by: OpenLit.
         */
        REQUEST_IMAGE_SIZE {
            @Override
            public String asString() {
                return "gen_ai.request.image_size";
            }
        },

        /**
         * Quality parameter for the generated image, usually a percentage.
         * Used by: OpenLit.
         */
        REQUEST_IMAGE_QUALITY {
            @Override
            public String asString() {
                return "gen_ai.request.image_quality";
            }
        },

        /**
         * Style or theme of the generated image.
         * Used by: OpenLit.
         */
        REQUEST_IMAGE_STYLE {
            @Override
            public String asString() {
                return "gen_ai.request.image_style";
            }
        },

        /**
         * Function definitions provided to the model in the request.
         * Used by: OpenLLMetry.
         */
        REQUEST_FUNCTIONS {
            @Override
            public String asString() {
                return "gen_ai.request.functions";
            }
        },

        /**
         * Username or identifier for the user making the request.
         * Used by: OpenLLMetry.
         */
        REQUEST_HEADERS {
            @Override
            public String asString() {
                return "llm.headers";
            }
        },
    }

    /**
     * Events for LLM operations.
     */
    public enum Events implements Observation.Event {

        /**
         * Full prompt sent to the model.
         */
        PROMPT {
            @Override
            public String getName() {
                return "gen_ai.content.prompt";
            }
        },

        /**
         * Full response received from the model.
         */
        COMPLETION {
            @Override
            public String getName() {
                return "gen_ai.content.completion";
            }
        }
    }

}
