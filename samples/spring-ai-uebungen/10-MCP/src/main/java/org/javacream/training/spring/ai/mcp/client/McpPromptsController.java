package org.javacream.training.spring.ai.mcp.client;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.ObjectProvider;
@RestController
@RequestMapping("/api/mcp-prompts")
@Tag(name = "McpPrompts")
public class McpPromptsController {
 private final ObjectProvider<List<McpSyncClient>> clients;
 public McpPromptsController(ObjectProvider<List<McpSyncClient>> clients) { this.clients=clients; }
 @PostMapping
 @Operation(summary = "McpPrompts", description = "MCP-Prompt abrufen")
 public Object execute(@RequestBody String message) {
 return client().getPrompt(new McpSchema.GetPromptRequest("explain",Map.of("topic",message)));
 }
 private McpSyncClient client() { return clients.stream().flatMap(List::stream).findFirst().orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit ClientApplication starten und Server zuerst starten")); }
}
