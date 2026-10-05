package org.javacream.training.spring.ai.responsemetadata;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/response-metadata")
@Tag(name = "ResponseMetadata")
public class ResponseMetadataController {
	private final ChatClient chatClient;

	public ResponseMetadataController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "ResponseMetadata", description = "ResponseMetadata mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return chatClient.prompt().user(message).call().chatResponse().getMetadata();
	}

}
