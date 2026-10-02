package org.javacream.training.spring.ai.toolcalling;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/return-direct")
@Tag(name = "ReturnDirect")
public class ReturnDirectController {
 private final ChatClient chatClient;
 
 public ReturnDirectController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ReturnDirect", description = "ReturnDirect mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).tools(new DirectTools()).call().content();
 }
 
}
