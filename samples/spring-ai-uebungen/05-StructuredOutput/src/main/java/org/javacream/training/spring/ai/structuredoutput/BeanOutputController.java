package org.javacream.training.spring.ai.structuredoutput;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/bean-output")
@Tag(name = "BeanOutput")
public class BeanOutputController {
	private final ChatClient chatClient;

	public BeanOutputController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "BeanOutput", description = "BeanOutput mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var converter = new BeanOutputConverter<>(Filmography.class);
		return chatClient.prompt().user(message + "\n" + converter.getFormat()).call().entity(converter);
	}

}
