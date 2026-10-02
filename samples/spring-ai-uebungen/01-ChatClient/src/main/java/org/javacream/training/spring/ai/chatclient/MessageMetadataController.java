package org.javacream.training.spring.ai.chatclient;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/message-metadata")
@Tag(name = "MessageMetadata")
public class MessageMetadataController {
 private final ChatClient chatClient;
 
 public MessageMetadataController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MessageMetadata", description = "Message Metadata sind Anwendungskontext; sie werden nicht automatisch als Text an Ollama übermittelt")
 public Object execute(@RequestBody String message) {
 var metadata = Map.<String,Object>of("exercise", "message-metadata", "requestId", UUID.randomUUID().toString());
return Map.of("metadata", metadata, "answer", chatClient.prompt().user(u -> u.text(message).metadata(metadata)).call().content());
 }
 
}
