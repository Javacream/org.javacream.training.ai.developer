package org.javacream.training.spring.ai.chatmemory;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/message-window")
@Tag(name = "MessageWindow")
public class MessageWindowController {
	private final ChatClient chatClient;
	private final ChatMemory memory = MessageWindowChatMemory.builder().maxMessages(4).build();

	public MessageWindowController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "MessageWindow", description = "MessageWindow mit Spring AI und Ollama")
	public Object execute(@RequestParam(defaultValue = "demo") String conversationId, @RequestBody String message) {
		var answer = chatClient.prompt().user(message).advisors(MessageChatMemoryAdvisor.builder(memory).build())
				.advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId)).call().content();
		return Map.of("conversationId", conversationId, "answer", answer, "messages", memory.get(conversationId));
	}

	@DeleteMapping
	public void clear(@RequestParam(defaultValue = "demo") String conversationId) {
		memory.clear(conversationId);
	}
}
