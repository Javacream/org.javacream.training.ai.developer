package org.javacream.training.spring.ai.mcp;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-prompts")
@Tag(name = "McpPrompts")
public class McpPromptsController {
	private final ChatClient chatClient;
	private final ObjectProvider<List<McpSyncClient>> clients;

	public McpPromptsController(ChatClient chatClient, ObjectProvider<List<McpSyncClient>> clients) {
		this.chatClient = chatClient;
		this.clients = clients;
	}

	@PostMapping
	@Operation(summary = "McpPrompts", description = "McpPrompts mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return client().getPrompt(new McpSchema.GetPromptRequest("explain", Map.of("topic", message)));
	}

	private McpSyncClient client() {
		return clients.stream().flatMap(List::stream).findFirst()
				.orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
						org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,
						"Mit Profil mcp-client starten und Server zuerst starten"));
	}
}
