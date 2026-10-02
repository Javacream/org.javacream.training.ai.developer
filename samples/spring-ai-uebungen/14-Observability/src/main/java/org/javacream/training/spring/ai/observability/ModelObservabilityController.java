package org.javacream.training.spring.ai.observability;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.model.ChatModel;
@RestController
@RequestMapping("/api/model-observability")
@Tag(name = "ModelObservability")
public class ModelObservabilityController {
 private final ChatClient chatClient;
 private final ChatModel model;
 public ModelObservabilityController(ChatClient chatClient, ChatModel model) { this.chatClient = chatClient; this.model=model; }
 @PostMapping
 @Operation(summary = "ModelObservability", description = "ModelObservability mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return model.call(new org.springframework.ai.chat.prompt.Prompt(message));
 }
 
}
