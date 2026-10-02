package org.javacream.training.spring.ai.prompts;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.prompt.*;
import org.springframework.ai.chat.messages.*;
@RestController
@RequestMapping("/api/system-prompt-template")
@Tag(name = "SystemPromptTemplate")
public class SystemPromptTemplateController {
 private final ChatClient chatClient;
 
 public SystemPromptTemplateController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "SystemPromptTemplate", description = "SystemPromptTemplate mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var system = new SystemPromptTemplate("Du bist Experte für {topic}.").createMessage(Map.of("topic",message));
return chatClient.prompt(new Prompt(List.of(system, new UserMessage("Nenne drei zentrale Konzepte.")))).call().content();
 }
 
}
