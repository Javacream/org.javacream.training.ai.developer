package org.javacream.training.spring.ai.prompts;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
