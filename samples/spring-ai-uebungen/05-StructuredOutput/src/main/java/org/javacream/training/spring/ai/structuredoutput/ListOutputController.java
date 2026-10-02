package org.javacream.training.spring.ai.structuredoutput;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/list-output")
@Tag(name = "ListOutput")
public class ListOutputController {
 private final ChatClient chatClient;
 
 public ListOutputController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ListOutput", description = "Kommagetrennte Ausgabe über ListOutputConverter")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().entity(new ListOutputConverter());
 }
 
}
