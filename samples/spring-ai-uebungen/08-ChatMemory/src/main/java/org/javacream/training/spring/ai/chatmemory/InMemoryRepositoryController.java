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
@RequestMapping("/api/in-memory-repository")
@Tag(name = "InMemoryRepository")
public class InMemoryRepositoryController {
 private final ChatClient chatClient;
 private final ChatMemoryRepository repository=new InMemoryChatMemoryRepository();
 public InMemoryRepositoryController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "InMemoryRepository", description = "InMemoryRepository mit Spring AI und Ollama")
 public Object execute(@RequestParam(defaultValue="repository-demo") String conversationId, @RequestBody String message) {
 repository.saveAll(conversationId,List.of(new UserMessage(message)));
return Map.of("conversationIds",repository.findConversationIds(),"messages",repository.findByConversationId(conversationId));
 }
 
}
