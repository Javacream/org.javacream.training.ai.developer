package org.javacream.training.spring.ai.structuredoutput;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.converter.BeanOutputConverter;
@RestController
@RequestMapping("/api/bean-output")
@Tag(name = "BeanOutput")
public class BeanOutputController {
 private final ChatClient chatClient;
 
 public BeanOutputController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "BeanOutput", description = "BeanOutput mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var converter = new BeanOutputConverter<>(Filmography.class);
return chatClient.prompt().user(message + "\n" + converter.getFormat()).call().entity(converter);
 }
 
}
