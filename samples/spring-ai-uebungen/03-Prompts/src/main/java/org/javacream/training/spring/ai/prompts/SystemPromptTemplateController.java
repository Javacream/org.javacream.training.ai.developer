package org.javacream.training.spring.ai.prompts;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/system-prompt-template")
@Tag(name = "SystemPromptTemplate")
public class SystemPromptTemplateController {
	private final ChatClient chatClient;

	public SystemPromptTemplateController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "SystemPromptTemplate", description = "SystemPromptTemplate mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var system = new SystemPromptTemplate("Du bist Experte für {topic}.").createMessage(Map.of("topic", message));
		return chatClient.prompt(new Prompt(List.of(system, new UserMessage("Nenne drei zentrale Konzepte.")))).call()
				.content();
	}

}
