package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;

@RestController
@RequestMapping("/api/simple-rag")
@Tag(name = "SimpleRag")
public class SimpleRagController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public SimpleRagController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "SimpleRag", description = "SimpleRag mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var documents=vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(3).build());
String context=documents.stream().map(Document::getText).collect(java.util.stream.Collectors.joining("\n"));
return chatClient.prompt().system("Antworte ausschließlich mit diesem Kontext. Fehlt die Information, sage es.\n"+context).user(message).call().content();
 }
 
}
