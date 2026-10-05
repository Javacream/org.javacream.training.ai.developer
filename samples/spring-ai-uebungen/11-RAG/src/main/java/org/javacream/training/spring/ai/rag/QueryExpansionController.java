package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/query-expansion")
@Tag(name = "QueryExpansion")
public class QueryExpansionController {
	private final ChatClient chatClient;

	public QueryExpansionController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "QueryExpansion", description = "QueryExpansion mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return MultiQueryExpander.builder().chatClientBuilder(chatClient.mutate()).numberOfQueries(3).build()
				.expand(new Query(message));
	}

}
