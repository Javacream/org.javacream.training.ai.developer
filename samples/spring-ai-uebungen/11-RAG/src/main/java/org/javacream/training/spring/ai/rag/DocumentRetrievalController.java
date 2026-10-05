package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/document-retrieval")
@Tag(name = "DocumentRetrieval")
public class DocumentRetrievalController {
	private final ChatClient chatClient;
	private final VectorStore vectorStore;

	public DocumentRetrievalController(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	@PostMapping
	@Operation(summary = "DocumentRetrieval", description = "DocumentRetrieval mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		return VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).topK(3).build()
				.retrieve(new Query(message));
	}

}
