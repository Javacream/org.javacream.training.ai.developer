package org.javacream.training.spring.ai.advisors;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/custom-call-advisor")
@Tag(name = "CustomCallAdvisor")
public class CustomCallAdvisorController {
	private final ChatClient chatClient;

	public CustomCallAdvisorController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "CustomCallAdvisor", description = "CustomCallAdvisor mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var response = chatClient.prompt().user(message)
				.advisors(a -> a.advisors(new TraceAdvisor("custom-call", 0)).param("exercise", "CustomCallAdvisor"))
				.call().chatClientResponse();
		return Map.of("answer", response.chatResponse().getResult().getOutput().getText(), "context",
				response.context());
	}

}
