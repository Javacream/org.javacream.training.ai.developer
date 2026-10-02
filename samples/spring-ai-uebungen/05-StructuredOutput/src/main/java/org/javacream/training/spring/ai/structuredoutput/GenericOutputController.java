package org.javacream.training.spring.ai.structuredoutput;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.ParameterizedTypeReference;
@RestController
@RequestMapping("/api/generic-output")
@Tag(name = "GenericOutput")
public class GenericOutputController {
 private final ChatClient chatClient;
 
 public GenericOutputController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "GenericOutput", description = "GenericOutput mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(new ParameterizedTypeReference<List<Filmography>>() {});
 }
 
}
