package org.javacream.training.spring.ai.observability;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/prompt-logging")
@Tag(name = "PromptLogging")
public class PromptLoggingController {
	private final ChatClient chatClient;

	public PromptLoggingController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "PromptLogging", description = "Mit Profil prompt-logging starten; Inhalte werden ausdrücklich in Logs geschrieben")
	public Object execute(@RequestBody String message) {
		return chatClient.prompt().user(message).advisors(new SimpleLoggerAdvisor()).call().content();
	}

}
