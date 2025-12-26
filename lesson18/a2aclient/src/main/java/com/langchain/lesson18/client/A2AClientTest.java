package com.langchain.lesson18.client;

import dev.langchain4j.agentic.AgenticServices;

public class A2AClientTest {

    public static void main(String[] args) throws InterruptedException {
        CreativeWriter creativeWriter = AgenticServices
                .a2aBuilder("http://localhost:8080", CreativeWriter.class)
                .inputKeys("topic")
                .outputKey("story")
                .build();
        String result = creativeWriter.generateStory("神话故事");
        System.out.println(result);
    }

}
