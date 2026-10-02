package org.javacream.training.spring.ai.chatclient;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chat-client-mutation")
@Tag(name = "ChatClientMutation")
public class ChatClientMutationController {
 private final ChatClient chatClient;
 
 public ChatClientMutationController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ChatClientMutation", description = "ChatClientMutation mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var derived = chatClient.mutate().defaultSystem("Antworte nur auf Englisch.").build();
return Map.of("original", chatClient.prompt().user(message).call().content(), "derived", derived.prompt().user(message).call().content());
 }
 
}
