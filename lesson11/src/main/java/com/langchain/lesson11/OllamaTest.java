package com.langchain.lesson11;

import dev.langchain4j.model.ollama.OllamaChatModel;

public class OllamaTest {
    public static void main(String[] args) {
        OllamaChatModel ollamaChatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:6006")
                .modelName("deepseek-r1:7b")
                .build();
        System.out.println(ollamaChatModel.chat("你是谁？"));
    }
}
