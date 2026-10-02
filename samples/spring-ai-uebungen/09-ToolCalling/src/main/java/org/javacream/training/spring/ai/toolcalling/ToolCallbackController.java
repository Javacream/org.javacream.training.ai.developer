package org.javacream.training.spring.ai.toolcalling;

import java.util.Date;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tool-callback")
@Tag(name = "ToolCallback")
public class ToolCallbackController {
	private final ChatClient chatClient;

	public ToolCallbackController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "ToolCallback", description = "ToolCallback mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var tool = FunctionToolCallback.builder("actuals", this::callback)
				.description("actual time").build();
		return chatClient.prompt().user(message).tools(tool).call().content();
	}
	public Date callback() {
		return new Date();
	}
	public record Numbers(int a, int b) {
	}
}
