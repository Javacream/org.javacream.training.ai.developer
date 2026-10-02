package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/prompt-parameter")
@Tag(name = "PromptParameter")
public class PromptParameterController {
 private final ChatClient chatClient;
 
 public PromptParameterController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "PromptParameter", description = "PromptParameter mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().system(s -> s.text("Du bist Experte für {topic}.").param("topic", message)).user(u -> u.text("Erkläre {topic} in drei Sätzen.").param("topic", message)).call().content();
 }
 
}
