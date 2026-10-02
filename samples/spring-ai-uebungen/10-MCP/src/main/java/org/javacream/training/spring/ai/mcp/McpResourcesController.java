package org.javacream.training.spring.ai.mcp;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/mcp-resources")
@Tag(name = "McpResources")
public class McpResourcesController {
 private final ChatClient chatClient;
 private final ObjectProvider<List<McpSyncClient>> clients;
 public McpResourcesController(ChatClient chatClient, ObjectProvider<List<McpSyncClient>> clients) { this.chatClient = chatClient; this.clients=clients; }
 @GetMapping
 @Operation(summary = "McpResources", description = "McpResources mit Spring AI und Ollama")
 public Object execute() {
 return client().readResource(new McpSchema.ReadResourceRequest("training://guide"));
 }
 private McpSyncClient client() { return clients.stream().flatMap(List::stream).findFirst().orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit Profil mcp-client starten und Server zuerst starten")); }
}
