package org.javacream.training.spring.ai.toolcalling;
import java.time.LocalDate;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
public class TrainingTools {
 @Tool(description="Get today's date as ISO-8601") public String today() { return LocalDate.now().toString(); }
 @Tool(description="Add two integer numbers") public int add(@ToolParam(description="First number") int a,@ToolParam(description="Second number") int b) { return Math.addExact(a,b); }
 @Tool(description="Get the tenant ID from trusted application context") public String tenant(ToolContext context) { return String.valueOf(context.getContext().get("tenantId")); }
}
