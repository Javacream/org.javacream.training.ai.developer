package org.javacream.training.spring.ai.responsemetadata;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
