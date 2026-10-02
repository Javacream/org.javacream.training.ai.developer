package org.javacream.training.spring.ai.responsemetadata;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/response-metadata")
@Tag(name = "ResponseMetadata")
public class ResponseMetadataController {
 private final ChatClient chatClient;
 
 public ResponseMetadataController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ResponseMetadata", description = "ResponseMetadata mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().chatResponse().getMetadata();
 }
 
}
