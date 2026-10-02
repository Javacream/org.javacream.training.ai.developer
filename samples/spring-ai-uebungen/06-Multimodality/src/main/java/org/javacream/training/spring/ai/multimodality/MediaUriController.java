package org.javacream.training.spring.ai.multimodality;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.UrlResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/media-uri")
@Tag(name = "MediaUri")
public class MediaUriController {
 private final ChatClient chatClient;
 
 public MediaUriController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MediaUri", description = "URI als UrlResource herunterladen: Ollama erwartet Bildbytes, keinen entfernten URI")
 public Object execute(@RequestParam(defaultValue="Beschreibe das Bild.") String message, @RequestParam String uri) throws java.net.MalformedURLException {
 var resource = new UrlResource(java.net.URI.create(uri));
return chatClient.prompt().user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, resource)).call().content();
 }
 
}
