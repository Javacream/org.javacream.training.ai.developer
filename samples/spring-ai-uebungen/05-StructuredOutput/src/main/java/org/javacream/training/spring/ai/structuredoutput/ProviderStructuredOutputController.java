package org.javacream.training.spring.ai.structuredoutput;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/provider-structured-output")
@Tag(name = "ProviderStructuredOutput")
public class ProviderStructuredOutputController {
 private final ChatClient chatClient;
 
 public ProviderStructuredOutputController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ProviderStructuredOutput", description = "ProviderStructuredOutput mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(Filmography.class, spec -> spec.useProviderStructuredOutput());
 }
 
}
