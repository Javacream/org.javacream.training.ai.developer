package org.javacream.training.spring.ai.prompts;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/resource-prompt")
@Tag(name = "ResourcePrompt")
public class ResourcePromptController {
 private final ChatClient chatClient;
 
 public ResourcePromptController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ResourcePrompt", description = "ResourcePrompt mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var template = new PromptTemplate(new ClassPathResource("prompts/explain.st"));
return chatClient.prompt(template.create(Map.of("topic", message))).call().content();
 }
 
}
