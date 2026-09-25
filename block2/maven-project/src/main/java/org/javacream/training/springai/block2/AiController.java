package org.javacream.training.springai.block2;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class AiController {
    private final ChatClient chatClient;

    public AiController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Du bist ein präziser Tutor für Java-Entwickler.")
                .build();
    }

    @GetMapping("/ask")
    public String ask(@RequestParam String q) {
        return chatClient.prompt().user(q).call().content();
    }

    @GetMapping("/card")
    public LearningCard card(@RequestParam String topic) {
        return chatClient.prompt()
                .user(u -> u.text("Erstelle eine kompakte Lernkarte zu {topic}. Genau drei keyPoints.")
                        .param("topic", topic))
                .call()
                .entity(LearningCard.class);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestParam String q) {
        return chatClient.prompt().user(q).stream().content();
    }

    public record LearningCard(String title, String summary, List<String> keyPoints) {}
}
