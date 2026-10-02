package org.javacream.training.spring.ai.models;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/image-model")
@Tag(name = "ImageModel")
public class ImageModelController {
 private final ChatClient chatClient;
 
 public ImageModelController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ImageModel", description = "Provider-Grenze: dieser dedizierte Model-Typ ist mit dem Ollama-Adapter nicht verfügbar")
 public Object execute(@RequestBody String message) {
 throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_IMPLEMENTED, "ImageModel: kein dedizierter Spring-AI-Ollama-Adapter. Siehe requirements.md.");
 }
 
}
