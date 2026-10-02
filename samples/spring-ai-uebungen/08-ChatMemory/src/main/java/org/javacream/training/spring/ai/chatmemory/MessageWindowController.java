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
@RequestMapping("/api/message-window")
@Tag(name = "MessageWindow")
public class MessageWindowController {
 private final ChatClient chatClient;
 private final ChatMemory memory=MessageWindowChatMemory.builder().maxMessages(4).build();
 public MessageWindowController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "MessageWindow", description = "MessageWindow mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="demo") String conversationId, @RequestBody String message) {
 var answer = chatClient.prompt().user(message).advisors(MessageChatMemoryAdvisor.builder(memory).build()).advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId)).call().content();
 return Map.of("conversationId",conversationId,"answer",answer,"messages",memory.get(conversationId));
 }
 @DeleteMapping public void clear(@RequestParam(defaultValue="demo") String conversationId) { memory.clear(conversationId); }
}
