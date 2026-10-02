package org.javacream.training.spring.ai.observability;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
@RestController
@RequestMapping("/api/advisor-observability")
@Tag(name = "AdvisorObservability")
public class AdvisorObservabilityController {
 private final ChatClient chatClient;
 
 public AdvisorObservabilityController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "AdvisorObservability", description = "AdvisorObservability mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).advisors(new SimpleLoggerAdvisor()).call().content();
 }
 
}
