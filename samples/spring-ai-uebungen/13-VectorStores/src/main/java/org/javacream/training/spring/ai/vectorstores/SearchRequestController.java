package org.javacream.training.spring.ai.vectorstores;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/search-request")
@Tag(name = "SearchRequest")
public class SearchRequestController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public SearchRequestController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "SearchRequest", description = "SearchRequest mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="3") int topK, @RequestParam(defaultValue="0.0") double threshold, @RequestBody String message) {
 return vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(topK).similarityThreshold(threshold).build());
 }
 
}
