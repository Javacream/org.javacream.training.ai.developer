package org.javacream.training.spring.ai.mcp.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ClientApplication.class);
        application.setAdditionalProfiles("mcp-client");
        application.run(args);
    }
}
