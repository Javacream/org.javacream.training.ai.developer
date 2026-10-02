package org.javacream.training.spring.ai.prompts;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/prompt-template")
@Tag(name = "PromptTemplate")
public class PromptTemplateController {
 private final ChatClient chatClient;
 
 public PromptTemplateController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "PromptTemplate", description = "PromptTemplate mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var template = new PromptTemplate("Erkläre {topic} mit einem Beispiel.");
return chatClient.prompt(template.create(Map.of("topic",message))).call().content();
 }
 
}
