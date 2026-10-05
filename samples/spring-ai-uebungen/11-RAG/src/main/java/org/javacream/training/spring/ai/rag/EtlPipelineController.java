package org.javacream.training.spring.ai.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/etl-pipeline")
@Tag(name = "EtlPipeline")
public class EtlPipelineController {
	private final ChatClient chatClient;
	private final VectorStore vectorStore;

	public EtlPipelineController(ChatClient chatClient, VectorStore vectorStore) {
		this.chatClient = chatClient;
		this.vectorStore = vectorStore;
	}

	@PostMapping
	@Operation(summary = "EtlPipeline", description = "EtlPipeline mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var reader = new TextReader(new ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
		var documents = TokenTextSplitter.builder().withChunkSize(100).withMinChunkSizeChars(10)
				.withMinChunkLengthToEmbed(5).build().apply(reader.get());
		vectorStore.accept(documents);
		return documents.stream().map(Document::getId).toList();
	}

}
