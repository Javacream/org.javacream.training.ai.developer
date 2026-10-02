package org.javacream.training.spring.ai.mcp;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-annotations")
@Tag(name = "McpAnnotations")
public class McpAnnotationsController {
 private final ChatClient chatClient;
 private final TrainingMcpFeatures features;
 public McpAnnotationsController(ChatClient chatClient, TrainingMcpFeatures features) { this.chatClient = chatClient; this.features=features; }
 @PostMapping
 @Operation(summary = "McpAnnotations", description = "REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar")
 public Object execute(@RequestBody String message) {
 return features.explain(message);
 }
 
}
