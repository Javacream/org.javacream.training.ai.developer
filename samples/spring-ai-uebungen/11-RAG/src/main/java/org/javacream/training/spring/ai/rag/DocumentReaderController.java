package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.reader.TextReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/document-reader")
@Tag(name = "DocumentReader")
public class DocumentReaderController {
	private final ChatClient chatClient;

	public DocumentReaderController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "DocumentReader", description = "DocumentReader mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return new TextReader(new ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8))).get();
	}

}
