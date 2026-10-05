package org.javacream.training.spring.ai.mcp.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatApplication {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(ChatApplication.class);
		application.setAdditionalProfiles("mcp-chat");
		application.run(args);
	}
}
