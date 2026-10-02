package org.javacream.training.spring.ai.prompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/template-renderer")
@Tag(name = "TemplateRenderer")
public class TemplateRendererController {
 private final ChatClient chatClient;
 
 public TemplateRendererController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "TemplateRenderer", description = "TemplateRenderer mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(u -> u.text("Erkläre <topic>.").param("topic", message)).templateRenderer(StTemplateRenderer.builder().startDelimiterToken('<').endDelimiterToken('>').build()).call().content();
 }
 
}
