package org.javacream.training.spring.ai.mcp.server;

import org.springframework.stereotype.Component;
import org.springframework.ai.mcp.annotation.*;
import io.modelcontextprotocol.spec.McpSchema;

@Component
public class McpTools {
	@McpTool(name = "add", description = "Add two integer numbers")
	public int add(@McpToolParam(description = "First number") int a,
			@McpToolParam(description = "Second number") int b) {
		return Math.addExact(a, b);
	}

	@McpResource(uri = "training://guide", name = "Training Guide", description = "A small training resource")
	public String guide() {
		return "Spring AI exercises use Ollama and Cassandra.";
	}

	@McpPrompt(name = "explain", description = "Explain a technical topic")
	public McpSchema.GetPromptResult explain(
			@McpArg(name = "topic", description = "Technical topic", required = true) String topic) {
		return new McpSchema.GetPromptResult("Explain " + topic,
				java.util.List.of(new McpSchema.PromptMessage(McpSchema.Role.USER,
						new McpSchema.TextContent("Explain " + topic + " with a Java example."))));
	}
}
