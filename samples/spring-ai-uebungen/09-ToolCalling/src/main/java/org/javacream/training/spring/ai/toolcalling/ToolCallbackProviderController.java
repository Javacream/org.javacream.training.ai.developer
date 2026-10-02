package org.javacream.training.spring.ai.toolcalling;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
@RestController
@RequestMapping("/api/tool-callback-provider")
@Tag(name = "ToolCallbackProvider")
public class ToolCallbackProviderController {
 private final ChatClient chatClient;
 
 public ToolCallbackProviderController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolCallbackProvider", description = "ToolCallbackProvider mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var provider = MethodToolCallbackProvider.builder().toolObjects(new TrainingTools()).build();
return chatClient.prompt().user(message).tools(provider).call().content();
 }
 
}
