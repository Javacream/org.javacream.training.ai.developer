package org.javacream.training.spring.ai.rag;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
