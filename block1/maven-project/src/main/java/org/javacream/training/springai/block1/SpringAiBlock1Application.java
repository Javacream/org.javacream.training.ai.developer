package org.javacream.training.springai.block1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiBlock1Application {
    public static void main(String[] args) {
        SpringApplication.run(SpringAiBlock1Application.class, args);
    }

    @Bean
    CommandLineRunner demo(ChatClient.Builder builder) {
        return args -> {
            ChatClient client = builder.build();
            var response = client.prompt()
                    .user("Erkläre Spring AI für einen Java-Entwickler in drei kurzen Sätzen.")
                    .call()
                    .chatResponse();
            System.out.println("\n=== Antwort ===");
            System.out.println(response.getResult().getOutput().getText());
            System.out.println("\n=== Metadaten ===");
            System.out.println(response.getMetadata());
        };
    }
}
