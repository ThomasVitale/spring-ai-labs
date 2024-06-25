package com.thomasvitale;

import org.springframework.boot.SpringApplication;

public class TestSpringAiObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringAiObservabilityApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
