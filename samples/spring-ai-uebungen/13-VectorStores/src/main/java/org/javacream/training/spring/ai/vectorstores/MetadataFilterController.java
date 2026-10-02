package org.javacream.training.spring.ai.vectorstores;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
