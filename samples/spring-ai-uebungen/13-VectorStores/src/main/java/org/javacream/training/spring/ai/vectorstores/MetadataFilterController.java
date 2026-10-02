package org.javacream.training.spring.ai.vectorstores;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
@RestController
@RequestMapping("/api/metadata-filter")
@Tag(name = "MetadataFilter")
public class MetadataFilterController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public MetadataFilterController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "MetadataFilter", description = "Cassandra: Kategorie als SchemaColumn indizieren, siehe README")
 public Object execute(@RequestParam(defaultValue="training") String category, @RequestBody String message) {
 return vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(5).filterExpression(new FilterExpressionBuilder().eq("category",category).build()).build());
 }
 
}
