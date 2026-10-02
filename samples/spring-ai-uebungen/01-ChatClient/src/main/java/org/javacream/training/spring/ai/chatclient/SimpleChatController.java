package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/simple-chat")
@Tag(name = "SimpleChat")
public class SimpleChatController {
 private final ChatClient chatClient;
 
 public SimpleChatController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "SimpleChat", description = "SimpleChat mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().content();
 }
 
}
