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
@RequestMapping("/api/multiple-media")
@Tag(name = "MultipleMedia")
public class MultipleMediaController {
 private final ChatClient chatClient;
 
 public MultipleMediaController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MultipleMedia", description = "MultipleMedia mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="Vergleiche die Bilder.") String message, @RequestPart List<MultipartFile> images) {
 return chatClient.prompt().user(u -> { u.text(message); for (var image : images) u.media(MimeTypeUtils.IMAGE_PNG, new ByteArrayResource(bytes(image))); }).call().content();
 }
 private byte[] bytes(MultipartFile image) { try { return image.getBytes(); } catch (java.io.IOException e) { throw new java.io.UncheckedIOException(e); } }
}
