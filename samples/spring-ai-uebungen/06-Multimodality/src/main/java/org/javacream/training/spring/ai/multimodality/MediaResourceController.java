package org.javacream.training.spring.ai.multimodality;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/media-resource")
@Tag(name = "MediaResource")
public class MediaResourceController {
 private final ChatClient chatClient;
 
 public MediaResourceController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MediaResource", description = "MediaResource mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, new ClassPathResource("images/sample.png"))).call().content();
 }
 
}
