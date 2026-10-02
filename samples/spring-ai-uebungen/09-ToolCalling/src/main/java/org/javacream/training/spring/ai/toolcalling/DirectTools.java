package org.javacream.training.spring.ai.toolcalling;
import org.springframework.ai.tool.annotation.Tool;
public class DirectTools { @Tool(description="Return the fixed training code",returnDirect=true) public String code() { return "SPRING-AI-TRAINING"; } }
