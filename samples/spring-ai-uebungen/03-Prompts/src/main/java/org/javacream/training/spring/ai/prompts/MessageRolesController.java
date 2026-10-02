package org.javacream.training.spring.ai.prompts;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.*;
import org.springframework.ai.chat.messages.*;
@RestController
@RequestMapping("/api/message-roles")
@Tag(name = "MessageRoles")
public class MessageRolesController {
 private final ChatClient chatClient;
 
 public MessageRolesController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MessageRoles", description = "MessageRoles mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var prompt = new Prompt(List.of(new SystemMessage("Du bist ein präziser Tutor."), new UserMessage("Was ist Java?"), new AssistantMessage("Java ist eine Programmiersprache."), new UserMessage(message)));
return chatClient.prompt(prompt).call().content();
 }
 
}
