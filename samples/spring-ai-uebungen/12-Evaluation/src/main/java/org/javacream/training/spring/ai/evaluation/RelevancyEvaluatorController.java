package org.javacream.training.spring.ai.evaluation;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/relevancy-evaluator")
@Tag(name = "RelevancyEvaluator")
public class RelevancyEvaluatorController {
	private final ChatClient chatClient;

	public RelevancyEvaluatorController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "RelevancyEvaluator", description = "RelevancyEvaluator mit Spring AI und Ollama")
	public Object execute(@RequestBody EvaluationInput input) {
		var request = new EvaluationRequest(input.question(), List.of(new Document(input.context())), input.answer());
		return new RelevancyEvaluator(chatClient.mutate()).evaluate(request);
	}

	public record EvaluationInput(String question, String context, String answer) {
	}
}
