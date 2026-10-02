package org.javacream.training.spring.ai.testcontainers;
import org.springframework.boot.SpringApplication;
public class TestApplication { public static void main(String[] args) { SpringApplication.from(Application::main).with(ContainerConfiguration.class).run(args); } }
