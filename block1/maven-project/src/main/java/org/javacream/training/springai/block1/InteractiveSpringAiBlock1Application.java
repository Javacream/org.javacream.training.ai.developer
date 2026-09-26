package org.javacream.training.springai.block1;

import java.util.Scanner;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class InteractiveSpringAiBlock1Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(SpringAiBlock1Configuration.class, args);

        try {
            ChatClient.Builder builder = context.getBean(ChatClient.Builder.class);
            ChatClient client = builder.build();

            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("\n=== Spring AI – interaktive Konsole ===");
                System.out.println("Frage eingeben; Leerzeile beendet das Programm.");

                while (true) {
                    System.out.print("\nFrage: ");
                    String question = scanner.nextLine();

                    if (question.isBlank()) {
                        System.out.println("Programm wird beendet.");
                        break;
                    }

                    var response = client.prompt()
                            .user(question)
                            .call()
                            .chatResponse();

                    System.out.println("\n=== Antwort ===");
                    System.out.println(response.getResult().getOutput().getText());

                    var usage = response.getMetadata().getUsage();

                    System.out.println("\n=== Token-Nutzung ===");
                    System.out.println("Prompt-Tokens:     " + usage.getPromptTokens());
                    System.out.println("Completion-Tokens: " + usage.getCompletionTokens());
                    System.out.println("Tokens gesamt:     " + usage.getTotalTokens());
                }
            }
        } finally {
            context.close();
        }
    }
}
