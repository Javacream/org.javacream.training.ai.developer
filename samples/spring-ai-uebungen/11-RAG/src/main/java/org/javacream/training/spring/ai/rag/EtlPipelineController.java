package org.javacream.training.spring.ai.rag;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.ByteArrayResource;
@RestController
@RequestMapping("/api/etl-pipeline")
@Tag(name = "EtlPipeline")
public class EtlPipelineController {
 private final ChatClient chatClient;
 private final VectorStore vectorStore;
 public EtlPipelineController(ChatClient chatClient, VectorStore vectorStore) { this.chatClient = chatClient; this.vectorStore=vectorStore; }
 @PostMapping
 @Operation(summary = "EtlPipeline", description = "EtlPipeline mit Spring AI und Ollama")
 public Object execute(@RequestBody String message) {
 var reader=new TextReader(new ByteArrayResource(message.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
var documents=TokenTextSplitter.builder().withChunkSize(100).withMinChunkSizeChars(10).withMinChunkLengthToEmbed(5).build().apply(reader.get());
vectorStore.accept(documents);return documents.stream().map(Document::getId).toList();
 }
 
}
