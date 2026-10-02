package org.javacream.training.spring.ai.mcp;
import org.springframework.ai.mcp.annotation.McpArg;
import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import io.modelcontextprotocol.spec.McpSchema;
@Component
public class TrainingMcpFeatures {
 @McpTool(name="add",description="Add two integer numbers")
 public int add(@McpToolParam(description="First number") int a,@McpToolParam(description="Second number") int b) { return Math.addExact(a,b); }
 @McpResource(uri="training://guide",name="Training Guide",description="A small training resource")
 public String guide() { return "Spring AI exercises use Ollama and Cassandra."; }
 @McpPrompt(name="explain",description="Explain a technical topic")
 public McpSchema.GetPromptResult explain(@McpArg(name="topic",description="Technical topic",required=true) String topic) {
  return new McpSchema.GetPromptResult("Explain "+topic,java.util.List.of(new McpSchema.PromptMessage(McpSchema.Role.USER,new McpSchema.TextContent("Explain "+topic+" with a Java example."))));
 }
}
