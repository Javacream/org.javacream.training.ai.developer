package org.javacream.training.spring.ai.chatmemory;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.chat.memory.*;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
@RestController
@RequestMapping("/api/chat-memory")
@Tag(name = "ChatMemory")
public class ChatMemoryController {
 private final ChatClient chatClient;
 private final ChatMemory memory=MessageWindowChatMemory.builder().maxMessages(20).build();
 public ChatMemoryController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ChatMemory", description = "ChatMemory mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="demo") String conversationId, @RequestBody String message) {
 memory.add(conversationId,new UserMessage(message));
 var response = chatClient.prompt(new org.springframework.ai.chat.prompt.Prompt(memory.get(conversationId))).call().chatResponse();
 memory.add(conversationId,response.getResult().getOutput());
 return Map.of("answer",response.getResult().getOutput().getText(),"messages",memory.get(conversationId));
 }
 @DeleteMapping public void clear(@RequestParam(defaultValue="demo") String conversationId) { memory.clear(conversationId); }
}
