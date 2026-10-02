package org.javacream.training.spring.ai.prompts;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/prompt")
@Tag(name = "Prompt")
public class PromptController {
 private final ChatClient chatClient;
 
 public PromptController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "Prompt", description = "Prompt mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt(new org.springframework.ai.chat.prompt.Prompt(message)).call().content();
 }
 
}
