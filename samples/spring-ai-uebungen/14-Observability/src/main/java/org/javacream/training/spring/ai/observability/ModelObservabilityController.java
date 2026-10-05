package org.javacream.training.spring.ai.observability;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/model-observability")
@Tag(name = "ModelObservability")
public class ModelObservabilityController {
	private final ChatClient chatClient;
	private final ChatModel model;

	public ModelObservabilityController(ChatClient chatClient, ChatModel model) {
		this.chatClient = chatClient;
		this.model = model;
	}

	@PostMapping
	@Operation(summary = "ModelObservability", description = "ModelObservability mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return model.call(new org.springframework.ai.chat.prompt.Prompt(message));
	}

}
