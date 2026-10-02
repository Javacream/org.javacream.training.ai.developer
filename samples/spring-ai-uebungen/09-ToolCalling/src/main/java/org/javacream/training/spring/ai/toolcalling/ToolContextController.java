package org.javacream.training.spring.ai.toolcalling;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tool-context")
@Tag(name = "ToolContext")
public class ToolContextController {
 private final ChatClient chatClient;
 
 public ToolContextController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolContext", description = "Frage nach der Tenant-ID; Context wird von der Anwendung gesetzt")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).tools(new TrainingTools()).toolContext(Map.of("tenantId", "training-acme")).call().content();
 }
 
}
