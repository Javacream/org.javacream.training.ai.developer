package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/query-transformation")
@Tag(name = "QueryTransformation")
public class QueryTransformationController {
	private final ChatClient chatClient;

	public QueryTransformationController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "QueryTransformation", description = "QueryTransformation mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate()).build()
				.transform(new Query(message));
	}

}
