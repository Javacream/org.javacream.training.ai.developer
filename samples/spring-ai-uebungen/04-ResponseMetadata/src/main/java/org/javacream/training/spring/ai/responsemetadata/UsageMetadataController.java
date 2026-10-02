package org.javacream.training.spring.ai.responsemetadata;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/usage-metadata")
@Tag(name = "UsageMetadata")
public class UsageMetadataController {
 private final ChatClient chatClient;
 
 public UsageMetadataController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "UsageMetadata", description = "UsageMetadata mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var usage = chatClient.prompt().user(message).call().chatResponse().getMetadata().getUsage();
return Map.of("promptTokens",usage.getPromptTokens(), "completionTokens",usage.getCompletionTokens(), "totalTokens",usage.getTotalTokens());
 }
 
}
