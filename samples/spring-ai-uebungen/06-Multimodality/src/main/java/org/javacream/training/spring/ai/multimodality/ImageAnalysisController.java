package org.javacream.training.spring.ai.multimodality;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/image-analysis")
@Tag(name = "ImageAnalysis")
public class ImageAnalysisController {
	private final ChatClient chatClient;

	public ImageAnalysisController(ChatClient chatClient) {
		this.chatClient = chatClient;
	}

	@PostMapping
	@Operation(summary = "ImageAnalysis", description = "ImageAnalysis mit Spring AI und Ollama")
	public Object execute(@RequestParam(defaultValue = "Beschreibe das Bild.") String message,
			@RequestPart MultipartFile image) {
		return chatClient.prompt()
				.user(u -> u.text(message).media(MimeTypeUtils.IMAGE_PNG, new ByteArrayResource(bytes(image)))).call()
				.content();
	}

	private byte[] bytes(MultipartFile image) {
		try {
			return image.getBytes();
		} catch (java.io.IOException e) {
			throw new java.io.UncheckedIOException(e);
		}
	}
}
