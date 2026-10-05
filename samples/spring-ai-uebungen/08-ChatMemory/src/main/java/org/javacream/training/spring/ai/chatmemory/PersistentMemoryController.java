package org.javacream.training.spring.ai.chatmemory;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/persistent-memory")
@Tag(name = "PersistentMemory")
public class PersistentMemoryController {
	private final ChatClient chatClient;
	private final ChatMemory memory;

	public PersistentMemoryController(ChatClient chatClient, TrainingCassandraChatMemoryRepository repository) {
		this.chatClient = chatClient;
		this.memory = MessageWindowChatMemory.builder().chatMemoryRepository(repository).maxMessages(20).build();
	}

	@PostMapping
	@Operation(summary = "PersistentMemory", description = "Eigenes Cassandra ChatMemoryRepository; Text-Rollen, keine Tool-Messages; sequentielle Demo-Aufrufe")
	public Object execute(@RequestParam(defaultValue = "persistent-demo") String conversationId,
			@RequestBody String message) {
		var answer = chatClient.prompt().user(message).advisors(MessageChatMemoryAdvisor.builder(memory).build())
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId)).call().content();
		return Map.of("answer", answer, "messages", memory.get(conversationId));
	}

}
