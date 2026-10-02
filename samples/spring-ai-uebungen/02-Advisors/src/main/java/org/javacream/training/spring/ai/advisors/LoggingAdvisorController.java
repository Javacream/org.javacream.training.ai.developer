package org.javacream.training.spring.ai.advisors;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
