package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
@RestController
@RequestMapping("/api/query-expansion")
@Tag(name = "QueryExpansion")
public class QueryExpansionController {
 private final ChatClient chatClient;
 
 public QueryExpansionController(ChatClient chatClient) { this.chatClient = chatClient;  }
 @PostMapping
 @Operation(summary = "QueryExpansion", description = "QueryExpansion mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return MultiQueryExpander.builder().chatClientBuilder(chatClient.mutate()).numberOfQueries(3).build().expand(new Query(message));
 }
 
}
