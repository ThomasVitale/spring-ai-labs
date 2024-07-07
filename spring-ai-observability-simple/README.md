# Spring AI Observability: Simple Example

## Pre-requisites

This example is based on an experimental proposal. You need to build that version of Spring AI locally to make
this application work. Check out the code from [this PR branch](https://github.com/spring-projects/spring-ai/pull/954) of the Spring AI repository and publish the packages to your local Maven
repository by running the following command.

```shell
./mvnw clean -DskipTests install
```

Next, make sure you have an [OpenAI account](https://platform.openai.com/signup). The application relies on an OpenAI API for providing LLMs.
Define an environment variable with the OpenAI API Key associated to your OpenAI account as the value.

```shell
export SPRING_AI_OPENAI_API_KEY=<INSERT KEY HERE>
```

## Running the application

Run the Spring Boot application as follows to get Testcontainers automatically provision a Grafana LGTM observability stack.

```shell
./gradlew bootTestRun
```

Grafana is listening to port 3000. Check your container runtime to find the port to which is exposed to your localhost
and access Grafana from http://localhost:. The credentials are `admin`/`admin`.

The application is automatically configured to export metrics and traces to the Grafana LGTM stack via OpenTelemetry.
In Grafana, you can query the traces from the "Explore" page, selecting the "Tempo" data source. You can also visualize metrics in "Explore > Metrics".

## Calling the application

You can now call the application that will use OpenAI to perform generative AI operations.
This example uses [httpie](https://httpie.io) to send HTTP requests.

### Chat

```shell
http :8080/chat
```

Try passing your custom prompt and check the result.

```shell
http :8080/chat message=="What is the capital of Italy?"
```

The next request is configured with a custom temperature value to obtain a more creative, yet less precise answer.

```shell
http :8080/chat/generic-options message=="Why is a raven like a writing desk? Give a short answer."
```

The next request is configured with Open AI-specific customizations.

```shell
http :8080/chat/openai-options message=="What can you see beyond what you can see? Give a short answer."
```

### Embedding

```shell
http :8080/embed
```

Try passing your custom prompt and check the result.

```shell
http :8080/embed message=="The capital of Italy is Rome"
```

The next request is configured with OpenAI-specific customizations.

```shell
http :8080/embed/openai-options message=="The capital of Italy is Rome"
```

### Image

```shell
http :8080/image
```

Try passing your custom prompt and check the result.

```shell
http :8080/image message=="Yellow Submarine"
```

The next request is configured with Open AI-specific customizations.

```shell
http :8080/image/openai-options message=="Here comes the sun"
```
