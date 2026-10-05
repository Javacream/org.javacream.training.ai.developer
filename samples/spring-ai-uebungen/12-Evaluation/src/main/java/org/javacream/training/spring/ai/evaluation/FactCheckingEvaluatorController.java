package org.javacream.training.spring.ai.evaluation;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/fact-checking-evaluator")
@Tag(name = "FactCheckingEvaluator")
public class FactCheckingEvaluatorController {
	private final ChatClient chatClient;

	public FactCheckingEvaluatorController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "FactCheckingEvaluator", description = "FactCheckingEvaluator mit Spring AI und Ollama")
	public Object execute(@RequestBody EvaluationInput input) {
		var builder = chatClient.mutate()
				.defaultOptions(OllamaChatOptions.builder().model("bespoke-minicheck").temperature(0.0).numPredict(2));
		return FactCheckingEvaluator.forBespokeMinicheck(builder)
				.evaluate(new EvaluationRequest(input.context(), List.of(), input.answer()));
	}

	public record EvaluationInput(String question, String context, String answer) {
	}
}
