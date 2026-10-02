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
@RequestMapping("/api/conversation-memory")
@Tag(name = "ConversationMemory")
public class ConversationMemoryController {
 private final ChatClient chatClient;
 private final ChatMemory memory=MessageWindowChatMemory.builder().maxMessages(20).build();
 public ConversationMemoryController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "ConversationMemory", description = "ConversationMemory mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="demo") String conversationId, @RequestBody String message) {
 var answer = chatClient.prompt().user(message).advisors(MessageChatMemoryAdvisor.builder(memory).build()).advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId)).call().content();
 return Map.of("conversationId",conversationId,"answer",answer,"messages",memory.get(conversationId));
 }
 @DeleteMapping public void clear(@RequestParam(defaultValue="demo") String conversationId) { memory.clear(conversationId); }
}
