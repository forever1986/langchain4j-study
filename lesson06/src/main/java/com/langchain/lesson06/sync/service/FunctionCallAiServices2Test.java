package com.langchain.lesson06.sync.service;

import com.langchain.lesson06.tools.TimeTool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class FunctionCallAiServices2Test {


    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .tools(new TimeTool()) // 这里将工具传入
                .build();
        // 3. 访问大模型
        String response = assistant.chat("请问洛杉矶现在几点？");
        System.out.println(response);
    }
}
