package org.javacream.training.spring.ai.structuredoutput;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/schema-validation")
@Tag(name = "SchemaValidation")
public class SchemaValidationController {
 private final ChatClient chatClient;
 
 public SchemaValidationController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "SchemaValidation", description = "SchemaValidation mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(Filmography.class, spec -> spec.validateSchema());
 }
 
}
