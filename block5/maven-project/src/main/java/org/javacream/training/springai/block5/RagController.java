package org.javacream.training.springai.block5;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rag")
public class RagController {
    private final ChatClient client;

    public RagController(ChatClient client) {
        this.client = client;
    }

    @GetMapping
    String ask(@RequestParam String q) {
        return client.prompt().user(q).call().content();
    }
}
