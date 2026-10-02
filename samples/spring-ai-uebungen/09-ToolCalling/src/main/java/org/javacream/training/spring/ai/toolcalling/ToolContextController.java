package org.javacream.training.spring.ai.toolcalling;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
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
