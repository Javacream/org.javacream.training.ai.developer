package org.javacream.training.spring.ai.evaluation;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.Evaluator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/custom-evaluator")
@Tag(name = "CustomEvaluator")
public class CustomEvaluatorController {
	private final ChatClient chatClient;

	public CustomEvaluatorController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "CustomEvaluator", description = "Deterministischer eigener Evaluator; Bewertung ist keine semantische Faktenprüfung")
	public Object execute(@RequestParam(defaultValue = "Spring") String required, @RequestBody String message) {
		Evaluator evaluator = request -> {
			String answer = request.getResponseContent();
			boolean pass = answer != null && answer.contains(required);
			return new EvaluationResponse(pass, pass ? 1.0f : 0.0f,
					pass ? "Required term found" : "Required term missing", Map.of());
		};
		return evaluator.evaluate(new EvaluationRequest("", List.of(), message));
	}

}
