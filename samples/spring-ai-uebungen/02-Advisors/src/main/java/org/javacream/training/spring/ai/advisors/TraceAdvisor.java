package org.javacream.training.spring.ai.advisors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import reactor.core.publisher.Flux;

public class TraceAdvisor implements CallAdvisor, StreamAdvisor {
	private final String name;
	private final int order;

	public TraceAdvisor(String name, int order) {
		this.name = name;
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public int getOrder() {
		return order;
	}

	private ChatClientRequest before(ChatClientRequest request) {
		var context = new HashMap<String, Object>(request.context());
		var trace = new ArrayList<String>((List<String>) context.getOrDefault("trace", List.of()));
		trace.add(name + ":before");
		context.put("trace", trace);
		context.put(name + ":start", System.nanoTime());
		return request.mutate().context(context).build();
	}

	private ChatClientResponse after(ChatClientResponse response) {
		var context = new HashMap<String, Object>(response.context());
		var trace = new ArrayList<String>((List<String>) context.getOrDefault("trace", List.of()));
		trace.add(name + ":after");
		context.put("trace", trace);
		context.put(name + ":elapsedNs", System.nanoTime() - (Long) context.get(name + ":start"));
		return response.mutate().context(context).build();
	}

	public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
		return after(chain.nextCall(before(request)));
	}

	public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
		return chain.nextStream(before(request)).map(this::after);
	}
}
