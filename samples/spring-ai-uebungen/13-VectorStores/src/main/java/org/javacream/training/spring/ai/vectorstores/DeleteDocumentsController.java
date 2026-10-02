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
@RequestMapping("/api/delete-documents")
@Tag(name = "DeleteDocuments")
public class DeleteDocumentsController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public DeleteDocumentsController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "DeleteDocuments", description = "Request enthält Dokument-ID aus AddDocuments")
 public Object execute(@RequestBody String message) {
 vectorStore.delete(List.of(message)); return Map.of("deletedId",message);
 }
 
}
