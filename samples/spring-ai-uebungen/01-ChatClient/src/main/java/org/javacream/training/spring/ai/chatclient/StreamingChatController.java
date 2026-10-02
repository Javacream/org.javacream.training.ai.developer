package org.javacream.training.spring.ai.chatclient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Flux;
@RestController
@RequestMapping("/api/streaming-chat")
@Tag(name = "StreamingChat")
public class StreamingChatController {
 private final ChatClient chatClient;
 
 public StreamingChatController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping(produces = "text/event-stream")
 @Operation(summary = "StreamingChat", description = "StreamingChat mit Spring AI und Ollama")
 public Flux<String> execute(@RequestBody String message) {
 return chatClient.prompt().user(message).stream().content();
 }
 
}
