package org.javacream.training.spring.ai.models;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.ChatOptions;
@RestController
@RequestMapping("/api/chat-options")
@Tag(name = "ChatOptions")
public class ChatOptionsController {
 private final ChatClient chatClient;
 
 public ChatOptionsController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ChatOptions", description = "ChatOptions mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).options(ChatOptions.builder().temperature(0.1).maxTokens(100)).call().content();
 }
 
}
