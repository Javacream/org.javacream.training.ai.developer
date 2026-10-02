package org.javacream.training.spring.ai.toolcalling;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/tool-parameters")
@Tag(name = "ToolParameters")
public class ToolParametersController {
 private final ChatClient chatClient;
 
 public ToolParametersController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolParameters", description = "Frage z.B. Addiere 17 und 25; @ToolParam")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).tools(new TrainingTools()).call().content();
 }
 
}
