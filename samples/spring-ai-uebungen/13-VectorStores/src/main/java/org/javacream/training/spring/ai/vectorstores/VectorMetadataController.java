package org.javacream.training.spring.ai.vectorstores;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/vector-metadata")
@Tag(name = "VectorMetadata")
public class VectorMetadataController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public VectorMetadataController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "VectorMetadata", description = "VectorMetadata mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="training") String category, @RequestBody String message) {
 var document=new Document(message,Map.of("category",category)); vectorStore.add(List.of(document)); return Map.of("id",document.getId(),"metadata",document.getMetadata());
 }
 
}
