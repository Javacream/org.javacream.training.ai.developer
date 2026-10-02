package org.javacream.training.spring.ai.vectorstores;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;

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
