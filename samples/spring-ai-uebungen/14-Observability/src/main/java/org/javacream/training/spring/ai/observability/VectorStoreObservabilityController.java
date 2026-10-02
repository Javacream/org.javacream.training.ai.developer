package org.javacream.training.spring.ai.observability;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/vector-store-observability")
@Tag(name = "VectorStoreObservability")
public class VectorStoreObservabilityController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public VectorStoreObservabilityController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "VectorStoreObservability", description = "VectorStoreObservability mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return vectorStore.similaritySearch(message);
 }
 
}
