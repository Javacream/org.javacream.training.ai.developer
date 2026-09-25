package org.javacream.training.springai.block6;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tools")
public class ToolController {
    private final ChatClient client;

    public ToolController(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    @GetMapping
    String ask(@RequestParam String q) {
        return client.prompt()
                .user(q)
                .tools(new CatalogTools())
                .call()
                .content();
    }
}
