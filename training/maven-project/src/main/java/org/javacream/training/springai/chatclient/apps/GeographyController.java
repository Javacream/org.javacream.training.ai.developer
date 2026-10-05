package org.javacream.training.springai.chatclient.apps;
import org.javacream.training.springai.chatclient.Filmography;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/geography")
@Tag(name = "Geography")
public class GeographyController {
 private final ChatClient chatClient;
 
 public GeographyController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "GeographyResponse", description = "Geografie als Java Record; Request z.B. 5 Städte Deutschlands")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(Geography.class);
 }
 
}
