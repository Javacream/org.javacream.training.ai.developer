package org.javacream.training.spring.ai.mcp.client;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.ObjectProvider;
@RestController
@RequestMapping("/api/mcp-resources")
@Tag(name = "McpResources")
public class McpResourcesController {
 private final ObjectProvider<List<McpSyncClient>> clients;
 public McpResourcesController(ObjectProvider<List<McpSyncClient>> clients) { this.clients=clients; }
 @GetMapping
 @Operation(summary = "McpResources", description = "MCP-Resource lesen")
 public Object execute() {
 return client().readResource(new McpSchema.ReadResourceRequest("training://guide"));
 }
 private McpSyncClient client() { return clients.stream().flatMap(List::stream).findFirst().orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit ClientApplication starten und Server zuerst starten")); }
}
