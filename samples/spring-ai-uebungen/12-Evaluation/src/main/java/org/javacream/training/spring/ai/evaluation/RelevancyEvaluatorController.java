package org.javacream.training.spring.ai.evaluation;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.evaluation.*;
import org.springframework.ai.chat.evaluation.*;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaChatOptions;
@RestController
@RequestMapping("/api/relevancy-evaluator")
@Tag(name = "RelevancyEvaluator")
public class RelevancyEvaluatorController {
 private final ChatClient chatClient;
 
 public RelevancyEvaluatorController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "RelevancyEvaluator", description = "RelevancyEvaluator mit Spring AI und Ollama")
 public Object execute(@RequestBody EvaluationInput input) {
 var request=new EvaluationRequest(input.question(),List.of(new Document(input.context())),input.answer());
 return new RelevancyEvaluator(chatClient.mutate()).evaluate(request);
 }
 public record EvaluationInput(String question,String context,String answer) {}
}
