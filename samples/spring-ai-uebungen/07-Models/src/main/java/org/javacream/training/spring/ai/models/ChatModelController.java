package org.javacream.training.spring.ai.models;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chat-model")
@Tag(name = "ChatModel")
public class ChatModelController {
	private final ChatClient chatClient;
	private final ChatModel model;

	public ChatModelController(ChatClient chatClient, ChatModel model) {
		this.chatClient = chatClient;
		this.model = model;
	}

	@PostMapping
	@Operation(summary = "ChatModel", description = "ChatModel mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return model.call(new org.springframework.ai.chat.prompt.Prompt(message));
	}

}
