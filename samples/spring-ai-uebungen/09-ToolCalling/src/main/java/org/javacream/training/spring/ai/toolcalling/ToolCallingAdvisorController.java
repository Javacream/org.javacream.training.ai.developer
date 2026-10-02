package org.javacream.training.spring.ai.toolcalling;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.client.advisor.ToolCallingAdvisor;
import org.springframework.ai.model.tool.ToolCallingManager;
@RestController
@RequestMapping("/api/tool-calling-advisor")
@Tag(name = "ToolCallingAdvisor")
public class ToolCallingAdvisorController {
 private final ChatClient chatClient;
 
 public ToolCallingAdvisorController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolCallingAdvisor", description = "ToolCallingAdvisor mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).tools(new TrainingTools()).advisors(ToolCallingAdvisor.builder().toolCallingManager(ToolCallingManager.builder().maxTotalToolCalls(5).build()).build()).call().content();
 }
 
}
