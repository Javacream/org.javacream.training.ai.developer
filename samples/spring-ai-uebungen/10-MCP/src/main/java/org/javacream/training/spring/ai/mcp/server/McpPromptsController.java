package org.javacream.training.spring.ai.mcp.server;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "McpPromptsController")
public class McpPromptsController {
	private final McpTools features;

	public McpPromptsController(McpTools features) {
		this.features = features;
	}

	@PostMapping(path = "/api/mcp-explain")
	@Operation(summary = "McpExplainPromptController", description = "REST-Einstieg zur Erzeugung eines explain-Pprompts; über /mcp in ServerApplication verfügbar")
	public Object execute_explain(@RequestBody String message) {
		return features.explain(message);
	}
	@PostMapping(path = "/api/mcp-guide")
	@Operation(summary = "McpGuidePromptController", description = "REST-Einstieg zur Erzeugung des guide-Pprompts; über /mcp in ServerApplication verfügbar")
	public Object execute_describe() {
		return features.guide();
	}

}
