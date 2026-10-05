package org.javacream.training.spring.ai.mcp.server;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-server-tools")
@Tag(name = "McpServerTools")
public class McpServerToolsController {
 private final McpTools features;
 public McpServerToolsController(McpTools features) { this.features=features; }
 @GetMapping
 @Operation(summary = "McpServerTools", description = "REST-Einstieg zur annotierten MCP-Funktion; über /mcp in ServerApplication verfügbar")
 public Object execute(@RequestParam(defaultValue="2") int a, @RequestParam(defaultValue="3") int b) {
 return features.add(a,b);
 }
 
}
