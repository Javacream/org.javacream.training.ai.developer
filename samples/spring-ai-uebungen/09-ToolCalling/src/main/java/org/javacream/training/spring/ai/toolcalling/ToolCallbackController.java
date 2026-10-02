package org.javacream.training.spring.ai.toolcalling;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.tool.function.FunctionToolCallback;
@RestController
@RequestMapping("/api/tool-callback")
@Tag(name = "ToolCallback")
public class ToolCallbackController {
 private final ChatClient chatClient;
 
 public ToolCallbackController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ToolCallback", description = "ToolCallback mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var tool = FunctionToolCallback.builder("multiply", (Numbers input) -> input.a() * input.b()).description("Multiply two integers").inputType(Numbers.class).build();
return chatClient.prompt().user(message).tools(tool).call().content();
 }
 public record Numbers(int a, int b) {}
}
