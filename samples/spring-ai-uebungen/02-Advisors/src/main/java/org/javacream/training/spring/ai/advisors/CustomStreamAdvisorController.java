package org.javacream.training.spring.ai.advisors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/custom-stream-advisor")
@Tag(name = "CustomStreamAdvisor")
public class CustomStreamAdvisorController {
	private final ChatClient chatClient;

	public CustomStreamAdvisorController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping(produces = "text/event-stream")
	@Operation(summary = "CustomStreamAdvisor", description = "CustomStreamAdvisor mit Spring AI und Ollama")
	public Flux<String> execute(@RequestBody String message) {
		return chatClient.prompt().user(message).advisors(new TraceAdvisor("stream", 0)).stream().content();
	}

}
