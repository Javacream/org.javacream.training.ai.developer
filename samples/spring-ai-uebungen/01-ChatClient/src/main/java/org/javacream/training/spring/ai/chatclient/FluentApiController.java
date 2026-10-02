package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/fluent-api")
@Tag(name = "FluentApi")
public class FluentApiController {
 private final ChatClient chatClient;
 
 public FluentApiController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "FluentApi", description = "FluentApi mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt(message).system("Antworte kurz auf Deutsch.").call().content();
 }
 
}
