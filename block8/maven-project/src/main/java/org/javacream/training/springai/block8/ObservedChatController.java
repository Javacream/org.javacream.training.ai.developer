package org.javacream.training.springai.block8;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ObservedChatController {
    private final ChatClient client;

    public ObservedChatController(ChatClient.Builder builder) {
        this.client = builder
                .defaultSystem("Antworte präzise und kennzeichne Unsicherheit.")
                .build();
    }

    @GetMapping
    String ask(@RequestParam String q) {
        return client.prompt().user(q).call().content();
    }
}
