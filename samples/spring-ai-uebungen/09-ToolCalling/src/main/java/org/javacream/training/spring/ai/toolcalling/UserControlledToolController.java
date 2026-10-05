package org.javacream.training.spring.ai.toolcalling;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user-controlled-tool")
@Tag(name = "UserControlledTool")
public class UserControlledToolController {
	private final ChatClient chatClient;

	public UserControlledToolController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "UserControlledTool", description = "UserControlledTool mit Spring AI und Ollama")
	public Object execute(@RequestBody String message) {
		var tools = ToolCallbacks.from(new TrainingTools());
		var options = ToolCallingChatOptions.builder().toolCallbacks(tools).build();
		var prompt = new Prompt(List.of(new UserMessage(message)), options);
		var response = chatClient.prompt(prompt).advisors(AdvisorParams.toolCallingAdvisorAutoRegister(false)).call()
				.chatClientResponse();
		var manager = ToolCallingManager.builder().build();
		int rounds = 0;
		while (response.chatResponse().hasToolCalls()) {
			if (++rounds > 5)
				throw new IllegalStateException("Tool loop limit reached");
			var result = manager.executeToolCalls(prompt, response.chatResponse());
			prompt = new Prompt(result.conversationHistory(), options);
			response = chatClient.prompt(prompt).advisors(AdvisorParams.toolCallingAdvisorAutoRegister(false)).call()
					.chatClientResponse();
		}
		return Map.of("rounds", rounds, "answer", response.chatResponse().getResult().getOutput().getText());
	}

}
