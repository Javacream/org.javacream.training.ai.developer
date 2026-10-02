package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.ChatOptions;
@RestController
@RequestMapping("/api/chat-client-defaults")
@Tag(name = "ChatClientDefaults")
public class ChatClientDefaultsController {
 private final ChatClient chatClient;
 
 public ChatClientDefaultsController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ChatClientDefaults", description = "ChatClientDefaults mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var client = chatClient.mutate().defaultSystem("Antworte in der Stimme eines Piraten.").defaultOptions(ChatOptions.builder().temperature(0.2)).build();
return client.prompt().user(message).call().content();
 }
 
}
