package org.javacream.training.spring.ai.observability;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chat-client-observability")
@Tag(name = "ChatClientObservability")
public class ChatClientObservabilityController {
 private final ChatClient chatClient;
 
 public ChatClientObservabilityController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ChatClientObservability", description = "Nach Aufruf /actuator/metrics und /actuator/prometheus ansehen")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).call().content();
 }
 
}
