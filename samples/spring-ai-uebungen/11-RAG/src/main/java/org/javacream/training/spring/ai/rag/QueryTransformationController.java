package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
@RestController
@RequestMapping("/api/query-transformation")
@Tag(name = "QueryTransformation")
public class QueryTransformationController {
 private final ChatClient chatClient;
 
 public QueryTransformationController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "QueryTransformation", description = "QueryTransformation mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return RewriteQueryTransformer.builder().chatClientBuilder(chatClient.mutate()).build().transform(new Query(message));
 }
 
}
