package org.javacream.training.springai.block1;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class SpringAiBlock1Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringAiBlock1Configuration.class, args);

        try {
            ChatClient.Builder builder = context.getBean(ChatClient.Builder.class);
            ChatClient client = builder.build();

            var response = client.prompt()
                    .user("Erkläre Spring AI für einen Java-Entwickler in drei kurzen Sätzen.")
                    .call()
                    .chatResponse();

            System.out.println("\n=== Antwort ===");
            System.out.println(response.getResult().getOutput().getText());

            var usage = response.getMetadata().getUsage();

            System.out.println("\n=== Token-Nutzung ===");
            System.out.println("Prompt-Tokens:    " + usage.getPromptTokens());
            System.out.println("Completion-Tokens: " + usage.getCompletionTokens());
            System.out.println("Tokens gesamt:    " + usage.getTotalTokens());
        } finally {
            context.close();
        }
    }
}
