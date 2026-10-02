package org.javacream.training.spring.ai.mcp;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-server-tools")
@Tag(name = "McpServerTools")
public class McpServerToolsController {
 private final ChatClient chatClient;
 private final TrainingMcpFeatures features;
 public McpServerToolsController(ChatClient chatClient, TrainingMcpFeatures features) { this.chatClient = chatClient; this.features=features; }
 @GetMapping
 @Operation(summary = "McpServerTools", description = "REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar")
 public Object execute(@RequestParam(defaultValue="2") int a, @RequestParam(defaultValue="3") int b) {
 return features.add(a,b);
 }
 
}
