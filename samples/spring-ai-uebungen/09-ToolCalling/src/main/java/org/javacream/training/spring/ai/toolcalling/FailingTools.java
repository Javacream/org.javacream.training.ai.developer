package org.javacream.training.spring.ai.toolcalling;
import org.springframework.ai.tool.annotation.Tool;
public class FailingTools { @Tool(description="Deliberately fail to demonstrate tool error handling") public String fail() { throw new IllegalArgumentException("Absichtlich ausgelöster Übungsfehler"); } }
