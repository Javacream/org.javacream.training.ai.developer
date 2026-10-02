package org.javacream.training.spring.ai.multimodality;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.MimeTypeUtils;
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
