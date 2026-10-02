package org.javacream.training.spring.ai.rag;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/document-transformer")
@Tag(name = "DocumentTransformer")
public class DocumentTransformerController {
 private final ChatClient chatClient;
 
 public DocumentTransformerController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "DocumentTransformer", description = "DocumentTransformer mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return TokenTextSplitter.builder().withChunkSize(100).withMinChunkSizeChars(10).withMinChunkLengthToEmbed(5).build().apply(List.of(new org.springframework.ai.document.Document(message)));
 }
 
}
