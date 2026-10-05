package org.javacream.training.spring.ai.prompts;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfiguration {
	@Bean
	ChatClient chatClient(ChatClient.Builder builder) {
		return builder.build();
	}
}
