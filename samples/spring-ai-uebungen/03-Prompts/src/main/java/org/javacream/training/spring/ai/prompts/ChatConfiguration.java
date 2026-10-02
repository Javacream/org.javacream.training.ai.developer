package org.javacream.training.spring.ai.prompts;
import org.springframework.context.annotation.*;
import org.springframework.ai.chat.client.ChatClient;
@Configuration
public class ChatConfiguration {
 @Bean ChatClient chatClient(ChatClient.Builder builder) { return builder.build(); }
}
