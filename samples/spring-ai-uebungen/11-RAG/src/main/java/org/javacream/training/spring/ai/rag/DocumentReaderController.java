package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
@RestController
@RequestMapping("/api/document-reader")
@Tag(name = "DocumentReader")
public class DocumentReaderController {
 private final ChatClient chatClient;
 
 public DocumentReaderController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "DocumentReader", description = "DocumentReader mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return new TextReader(new ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8))).get();
 }
 
}
