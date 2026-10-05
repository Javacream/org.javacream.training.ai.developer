package org.javacream.training.spring.ai.toolcalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/simple-tool")
@Tag(name = "SimpleTool")
public class SimpleToolController {
	private final ChatClient chatClient;

	public SimpleToolController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "SimpleTool", description = "Frage z.B. Welches Datum ist heute?")
	public Object execute(@RequestBody String message) {
		return chatClient.prompt().user(message).tools(new TrainingTools()).call().content();
	}

}
