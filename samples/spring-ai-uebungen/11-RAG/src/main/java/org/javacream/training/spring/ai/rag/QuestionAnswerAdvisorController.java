package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
@RestController
@RequestMapping("/api/question-answer-advisor")
@Tag(name = "QuestionAnswerAdvisor")
public class QuestionAnswerAdvisorController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public QuestionAnswerAdvisorController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "QuestionAnswerAdvisor", description = "QuestionAnswerAdvisor mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 return chatClient.prompt().user(message).advisors(QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest.builder().topK(3).build()).build()).call().content();
 }
 
}
