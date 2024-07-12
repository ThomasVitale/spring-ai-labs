package com.thomasvitale;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptionsBuilder;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
class ChatController {

    private final ChatModel chatModel;

    ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/chat")
    String chat(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String message) {
        return chatModel.call(message);
    }

    @GetMapping("/chat/generic-options")
    String chatWithGenericOptions(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String message) {
        return chatModel.call(new Prompt(message, ChatOptionsBuilder.builder()
                        .withTemperature(1.3f)
                        .build()))
                .getResult().getOutput().getContent();
    }

    @GetMapping("/chat/openai-options")
    String chatWithOpenAiOptions(@RequestParam(defaultValue = "What did Gandalf say to the Balrog?") String message) {
        return chatModel.call(new Prompt(message, OpenAiChatOptions.builder()
                        .withModel("gpt-4o")
                        .withUser("jon.snow")
                        .withFrequencyPenalty(1.3f)
                        .build()))
                .getResult().getOutput().getContent();
    }

    @GetMapping("/chat/functions")
    String chatWithFunctions(@RequestParam(defaultValue = "Philip Pullman") String author) {
        return chatModel.call(new Prompt("What books written by %s are available to read and what is their bestseller?".formatted(author),
                        OpenAiChatOptions.builder()
                        .withModel("gpt-4o")
                        .withTemperature(0.3f)
                        .withFunctions(Set.of("booksByAuthor", "bestsellerBookByAuthor"))
                        .build()))
                .getResult().getOutput().getContent();
    }

}
