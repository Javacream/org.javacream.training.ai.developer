package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/entity-response")
@Tag(name = "EntityResponse")
public class EntityResponseController {
 private final ChatClient chatClient;
 
 public EntityResponseController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "EntityResponse", description = "Filmografie als Java Record; Request z.B. Nenne drei Filme von Tom Hanks")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(Filmography.class);
 }
 
}
