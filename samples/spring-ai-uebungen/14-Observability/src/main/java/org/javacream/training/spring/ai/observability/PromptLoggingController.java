package org.javacream.training.spring.ai.observability;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
@RestController
@RequestMapping("/api/prompt-logging")
@Tag(name = "PromptLogging")
public class PromptLoggingController {
 private final ChatClient chatClient;
 
 public PromptLoggingController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "PromptLogging", description = "Mit Profil prompt-logging starten; Inhalte werden ausdrücklich in Logs geschrieben")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).advisors(new SimpleLoggerAdvisor()).call().content();
 }
 
}
