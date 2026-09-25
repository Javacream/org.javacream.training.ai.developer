package org.javacream.training.springai.block4;

import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiBlock4Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringAiBlock4Application.class, args);
    }

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    @Bean
    CommandLineRunner demo(VectorStore store) {
        return args -> {
            store.add(List.of(
                new Document("Spring AI bietet ChatModel und ChatClient für Modellzugriffe.",
                    Map.of("source", "intro")),
                new Document("Ein VectorStore speichert Embeddings und ermöglicht semantische Ähnlichkeitssuche.",
                    Map.of("source", "vector")),
                new Document("RAG kombiniert Retrieval aus eigenen Dokumenten mit der Antwortgenerierung eines LLM.",
                    Map.of("source", "rag"))
            ));

            var hits = store.similaritySearch(SearchRequest.builder()
                    .query("Wie nutzt eine AI-Anwendung eigenes Fachwissen?")
                    .topK(2)
                    .build());

            System.out.println("\n=== Retrieval-Treffer ===");
            hits.forEach(d -> System.out.println(d.getMetadata() + " -> " + d.getText()));
        };
    }
}
