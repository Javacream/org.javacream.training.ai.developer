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
@RequestMapping("/api/similarity-search")
@Tag(name = "SimilaritySearch")
public class SimilaritySearchController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public SimilaritySearchController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "SimilaritySearch", description = "SimilaritySearch mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return vectorStore.similaritySearch(message);
 }
 
}
