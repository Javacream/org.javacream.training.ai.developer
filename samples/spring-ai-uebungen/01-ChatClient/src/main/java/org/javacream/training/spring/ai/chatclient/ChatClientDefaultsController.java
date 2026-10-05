package org.javacream.training.spring.ai.chatclient;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chat-client-defaults")
@Tag(name = "ChatClientDefaults")
public class ChatClientDefaultsController {
	private final ChatClient chatClient;

	public ChatClientDefaultsController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "ChatClientDefaults", description = "ChatClientDefaults mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var client = chatClient.mutate().defaultSystem("Antworte in der Stimme eines Piraten.")
				.defaultOptions(ChatOptions.builder().temperature(0.2)).build();
		return client.prompt().user(message).call().content();
	}

}
