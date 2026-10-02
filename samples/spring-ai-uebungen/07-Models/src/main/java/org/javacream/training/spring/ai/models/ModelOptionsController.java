package org.javacream.training.spring.ai.models;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.ollama.api.OllamaChatOptions;
@RestController
@RequestMapping("/api/model-options")
@Tag(name = "ModelOptions")
public class ModelOptionsController {
 private final ChatClient chatClient;
 
 public ModelOptionsController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ModelOptions", description = "ModelOptions mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).options(OllamaChatOptions.builder().numCtx(4096).numPredict(100).seed(42)).call().content();
 }
 
}
