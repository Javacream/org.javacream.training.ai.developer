package org.javacream.training.spring.ai.chatmemory;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
