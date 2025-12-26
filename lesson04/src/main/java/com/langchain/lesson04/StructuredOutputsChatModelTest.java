package com.langchain.lesson04;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;

import static dev.langchain4j.model.chat.request.ResponseFormatType.JSON;

public class StructuredOutputsChatModelTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("QWEN_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .modelName("qwen-flash-2025-07-28")
                .build();
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(JSON) // 设置response_format
                .build();
        UserMessage userMessage = UserMessage.from("《活着》是余华在1993年写的书，书中描述主人公在特殊时期经历的故事。转为JSON格式");
        ChatRequest chatRequest = ChatRequest.builder()
                .responseFormat(responseFormat)
                .messages(userMessage)
                .build();
        ChatResponse response = model.chat(chatRequest);
        System.out.println(response.aiMessage().text());
    }
}
