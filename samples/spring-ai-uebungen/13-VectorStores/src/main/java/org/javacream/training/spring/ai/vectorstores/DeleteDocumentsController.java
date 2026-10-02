package org.javacream.training.spring.ai.vectorstores;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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
