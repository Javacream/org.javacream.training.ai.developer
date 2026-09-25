package org.javacream.training.springai.block5;

import java.util.List;
import java.util.Map;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringAiBlock5Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringAiBlock5Application.class, args);
    }

    @Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
        var store = SimpleVectorStore.builder(embeddingModel).build();
        store.add(List.of(
            new Document("ChatClient ist die fluente High-Level-API für Chat-Anwendungen in Spring AI.",
                Map.of("source","seminar-notes", "chapter","chat")),
            new Document("VectorStore ist die portable Spring-AI-Abstraktion für semantische Vektorsuche.",
                Map.of("source","seminar-notes", "chapter","rag")),
            new Document("QuestionAnswerAdvisor führt Retrieval durch und ergänzt den gefundenen Kontext für die Antwort.",
                Map.of("source","seminar-notes", "chapter","rag"))
        ));
        return store;
    }

    @Bean
    ChatClient ragClient(ChatModel model, VectorStore store) {
        return ChatClient.builder(model)
                .defaultSystem("Antworte nur auf Basis des bereitgestellten Kontexts. Reicht er nicht, sage das ausdrücklich.")
                .defaultAdvisors(QuestionAnswerAdvisor.builder(store).build())
                .build();
    }
}
