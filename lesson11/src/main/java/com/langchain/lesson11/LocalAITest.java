package com.langchain.lesson11;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.localai.LocalAiEmbeddingModel;

import java.util.Arrays;

public class LocalAITest {
    public static void main(String[] args) {
        EmbeddingModel embeddingModel = LocalAiEmbeddingModel.builder()
                .baseUrl("http://localhost:6006")
                .modelName("deepseek-r1:7b")
                .build();
        System.out.println(Arrays.toString(embeddingModel.embed("你是谁？").content().vector()));
    }
}
