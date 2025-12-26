package com.langchain.lesson04;

import com.langchain.lesson04.service.Book;
import com.langchain.lesson04.service.FormatAssistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

public class JSONSchemaAiServicesTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("MISTRAL_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://api.mistral.ai/v1")
                .modelName("mistral-large-2512")
                .strictJsonSchema(true) // 设置JsonSchema为true
                .supportedCapabilities(RESPONSE_FORMAT_JSON_SCHEMA) // 支持类型为Json Schema
                .build();
        FormatAssistant formatAssistant = AiServices.create(FormatAssistant.class,model);
        Book book = formatAssistant.chat("《活着》是余华在1993年写的书，书中描述主人公在特殊时期经历的故事");
        System.out.println(book);
    }
}
