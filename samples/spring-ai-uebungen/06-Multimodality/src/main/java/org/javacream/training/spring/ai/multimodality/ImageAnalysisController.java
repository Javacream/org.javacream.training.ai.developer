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
@RequestMapping("/api/image-analysis")
@Tag(name = "ImageAnalysis")
public class ImageAnalysisController {
 private final ChatClient chatClient;
 
 public ImageAnalysisController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ImageAnalysis", description = "ImageAnalysis mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="Beschreibe das Bild.") String message, @RequestPart MultipartFile image) {
 return chatClient.prompt().user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, new ByteArrayResource(bytes(image)))).call().content();
 }
 private byte[] bytes(MultipartFile image) { try { return image.getBytes(); } catch (java.io.IOException e) { throw new java.io.UncheckedIOException(e); } }
}
