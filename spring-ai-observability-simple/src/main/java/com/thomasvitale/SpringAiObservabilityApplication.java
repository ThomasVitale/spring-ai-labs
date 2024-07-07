package com.thomasvitale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@SpringBootApplication
public class SpringAiObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiObservabilityApplication.class, args);
	}

	@Bean
	RestClientCustomizer restClientCustomizer() {
		return builder -> builder.requestFactory(ClientHttpRequestFactories.get(SimpleClientHttpRequestFactory.class, ClientHttpRequestFactorySettings.DEFAULTS));
	}

}
