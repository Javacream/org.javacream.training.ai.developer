package org.javacream.training.spring.ai.advisors;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
@RestController
@RequestMapping("/api/logging-advisor")
@Tag(name = "LoggingAdvisor")
public class LoggingAdvisorController {
 private final ChatClient chatClient;
 
 public LoggingAdvisorController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "LoggingAdvisor", description = "LoggingAdvisor mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).advisors(new SimpleLoggerAdvisor()).call().content();
 }
 
}
