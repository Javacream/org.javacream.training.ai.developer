package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
@RestController
@RequestMapping("/api/document-retrieval")
@Tag(name = "DocumentRetrieval")
public class DocumentRetrievalController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public DocumentRetrievalController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "DocumentRetrieval", description = "DocumentRetrieval mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3).build().retrieve(new Query(message));
 }
 
}
