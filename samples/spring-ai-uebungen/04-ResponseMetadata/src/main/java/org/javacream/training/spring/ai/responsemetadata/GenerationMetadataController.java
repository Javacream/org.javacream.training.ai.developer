package org.javacream.training.spring.ai.responsemetadata;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/generation-metadata")
@Tag(name = "GenerationMetadata")
public class GenerationMetadataController {
	private final ChatClient chatClient;

	public GenerationMetadataController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "GenerationMetadata", description = "GenerationMetadata mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return chatClient.prompt().user(message).call().chatResponse().getResults().stream()
				.map(g -> Map.of("text", g.getOutput().getText(), "metadata", g.getMetadata())).toList();
	}

}
