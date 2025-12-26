package com.langchain.lesson11;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class HuggingfaceTest {

    public static void main(String[] args) {
        ChatModel model = OpenAiChatModel. builder()
                .apiKey(System.getenv("HF_API_KEY"))
                .baseUrl("https://api.endpoints.huggingface.cloud/v2/endpoint/xxxxx")
                .modelName("qwen2-5-7b-instruct")
                .build();
        System.out.println(model.chat("你是谁？"));
    }
}
