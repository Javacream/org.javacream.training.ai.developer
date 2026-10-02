package org.javacream.training.spring.ai.developmentservices;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.datastax.oss.driver.api.core.CqlSession;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/service-connection")
@Tag(name = "ServiceConnection")
public class ServiceConnectionController {
 private final ChatClient chatClient;
 private final CqlSession session;
 public ServiceConnectionController(ChatClient chatClient, CqlSession session) { this.chatClient = chatClient; this.session=session; }
 @PostMapping
 @Operation(summary = "ServiceConnection", description = "Compose startet Cassandra; ConnectionDetails siehe Konfiguration")
 public Object execute(@RequestBody String message) {
 return Map.of("release",session.execute("SELECT release_version FROM system.local").one().getString("release_version"),"answer",chatClient.prompt().user(message).call().content());
 }
 
}
