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
@RequestMapping("/api/document-writer")
@Tag(name = "DocumentWriter")
public class DocumentWriterController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public DocumentWriterController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "DocumentWriter", description = "DocumentWriter mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var documents=List.of(new Document(message));vectorStore.accept(documents);return documents.stream().map(Document::getId).toList();
 }
 
}
