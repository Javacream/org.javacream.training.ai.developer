package org.javacream.training.spring.ai.mcp;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-tools")
@Tag(name = "McpTools")
public class McpToolsController {
	private final ChatClient chatClient;
	private final ObjectProvider<SyncMcpToolCallbackProvider> provider;

	public McpToolsController(ChatClient chatClient, ObjectProvider<SyncMcpToolCallbackProvider> provider) {
		this.chatClient = chatClient;
		this.provider = provider;
	}

	@PostMapping
	@Operation(summary = "McpTools", description = "McpTools mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var tools = provider.getIfAvailable();
		if (tools == null)
			throw new org.springframework.web.server.ResponseStatusException(
					org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, "Profil mcp-client benötigt");
		return chatClient.prompt().user(message).tools(tools).call().content();
	}

}
