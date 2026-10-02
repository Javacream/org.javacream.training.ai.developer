package org.javacream.training.spring.ai.structuredoutput;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.converter.MapOutputConverter;
@RestController
@RequestMapping("/api/map-output")
@Tag(name = "MapOutput")
public class MapOutputController {
 private final ChatClient chatClient;
 
 public MapOutputController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MapOutput", description = "MapOutput mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(new MapOutputConverter());
 }
 
}
