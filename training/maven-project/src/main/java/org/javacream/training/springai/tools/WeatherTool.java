package org.javacream.training.springai.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class WeatherTool {

	@Tool(description = "actual weather for city")
	public String weatherForCity(@ToolParam(description = "city") String city) {
		System.out.println("determine weather for city " + city);
		return "GRANDIOS";
	}
}
