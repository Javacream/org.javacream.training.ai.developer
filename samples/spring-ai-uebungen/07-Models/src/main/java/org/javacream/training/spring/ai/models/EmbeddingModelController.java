package org.javacream.training.spring.ai.models;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/embedding-model")
@Tag(name = "EmbeddingModel")
public class EmbeddingModelController {
	private final ChatClient chatClient;
	private final EmbeddingModel embeddingModel;

	public EmbeddingModelController(ChatClient chatClient, EmbeddingModel embeddingModel) {
		this.chatClient = chatClient;
		this.embeddingModel = embeddingModel;
	}

	@PostMapping
	@Operation(summary = "EmbeddingModel", description = "EmbeddingModel mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		float[] vector = embeddingModel.embed(message);
		return Map.of("dimensions", vector.length, "vector", vector);
	}

}
