package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/question-answer-advisor")
@Tag(name = "QuestionAnswerAdvisor")
public class QuestionAnswerAdvisorController {
	private final ChatClient chatClient;
	private final VectorStore vectorStore;

	public QuestionAnswerAdvisorController(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	@PostMapping
	@Operation(summary = "QuestionAnswerAdvisor", description = "QuestionAnswerAdvisor mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return chatClient.prompt().user(message).advisors(QuestionAnswerAdvisor.builder(vectorStore)
				.searchRequest(SearchRequest.builder().topK(3).build()).build()).call().content();
	}

}
