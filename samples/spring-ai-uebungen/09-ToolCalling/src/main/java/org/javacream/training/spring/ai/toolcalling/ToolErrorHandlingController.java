package org.javacream.training.spring.ai.toolcalling;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tool-error-handling")
@Tag(name = "ToolErrorHandling")
public class ToolErrorHandlingController {
 private final ChatClient chatClient;
 
 public ToolErrorHandlingController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolErrorHandling", description = "Absichtlicher Tool-Fehler; abhängig vom ExceptionProcessor an Modell zurückgegeben oder HTTP 502")
 public Object execute(@RequestBody String message) {
 try { return chatClient.prompt().user(message).tools(new FailingTools()).call().content(); } catch (RuntimeException e) { throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_GATEWAY, "Tool-Ausführung fehlgeschlagen: " + e.getMessage(),e); }
 }
 
}
