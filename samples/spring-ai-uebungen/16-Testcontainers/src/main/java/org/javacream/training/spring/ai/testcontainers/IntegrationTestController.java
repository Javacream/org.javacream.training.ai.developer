package org.javacream.training.spring.ai.testcontainers;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.ObjectProvider;
@RestController
@RequestMapping("/api/integration-test")
@Tag(name = "IntegrationTest")
public class IntegrationTestController {
 private final ChatClient chatClient;
 private final ObjectProvider<VectorStore> vectorStore;
 public IntegrationTestController(ChatClient chatClient, ObjectProvider<VectorStore> vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "IntegrationTest", description = "IntegrationTest mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var store=vectorStore.getIfAvailable();if(store==null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE,"Mit TestApplication starten oder CASSANDRA_ENABLED=true setzen");
var doc=new Document(message);store.add(List.of(doc));return Map.of("id",doc.getId(),"results",store.similaritySearch(SearchRequest.builder().query(message).topK(1).build()));
 }
 
}
