package org.javacream.training.spring.ai.advisors;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/advisor-context")
@Tag(name = "AdvisorContext")
public class AdvisorContextController {
 private final ChatClient chatClient;
 
 public AdvisorContextController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "AdvisorContext", description = "AdvisorContext mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var response = chatClient.prompt().user(message).advisors(a -> a.advisors(new TraceAdvisor("context", 0)).param("exercise", "AdvisorContext")).call().chatClientResponse();
return Map.of("answer",response.chatResponse().getResult().getOutput().getText(), "context",response.context());
 }
 
}
