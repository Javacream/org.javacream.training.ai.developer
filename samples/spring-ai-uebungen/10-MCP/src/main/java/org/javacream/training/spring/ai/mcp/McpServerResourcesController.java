package org.javacream.training.spring.ai.mcp;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-server-resources")
@Tag(name = "McpServerResources")
public class McpServerResourcesController {
 private final ChatClient chatClient;
 private final TrainingMcpFeatures features;
 public McpServerResourcesController(ChatClient chatClient, TrainingMcpFeatures features) { this.chatClient = chatClient; this.features=features; }
 @GetMapping
 @Operation(summary = "McpServerResources", description = "REST-Einstieg zur annotierten MCP-Funktion; über /mcp mit Profil mcp-server verfügbar")
 public Object execute() {
 return features.guide();
 }
 
}
