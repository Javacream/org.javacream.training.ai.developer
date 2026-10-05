package org.javacream.training.spring.ai.mcp.server;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/mcp-server-resources")
@Tag(name = "McpServerResources")
public class McpServerResourcesController {
 private final McpTools features;
 public McpServerResourcesController(McpTools features) { this.features=features; }
 @GetMapping
 @Operation(summary = "McpServerResources", description = "REST-Einstieg zur annotierten MCP-Funktion; über /mcp in ServerApplication verfügbar")
 public Object execute() {
 return features.guide();
 }
 
}
