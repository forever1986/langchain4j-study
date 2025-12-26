package com.langchain.lesson03.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class LoggingChatModelTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey) // API KEY 访问大模型的认证密钥
                .baseUrl("https://open.bigmodel.cn/api/paas/v4") // 设置模型访问URL，默认是openAI的地址
                .modelName("glm-4-flash-250414") // 设置模型名称
                .logRequests(true)
                .logResponses(true)
                .build();
        String response = model.chat("直接给我推荐一本书？");
        System.out.println(response);
    }
}
