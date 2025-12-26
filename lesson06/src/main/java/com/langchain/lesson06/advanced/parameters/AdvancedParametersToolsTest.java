package com.langchain.lesson06.advanced.parameters;

import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Map;

public class AdvancedParametersToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AdvancedParametersAssistant advancedAssistant = AiServices.builder(AdvancedParametersAssistant.class)
                .chatModel(model)
                .tools(new AdvancedParametersTimeTool())
                .build();
        // 3. 访问大模型：注意这里设置了一个参数进行传递
        InvocationParameters parameters = InvocationParameters.from(Map.of("userId", "12345"));
        String result = advancedAssistant.chat("请问洛杉矶现在几点？", parameters);
        System.out.println(result);
    }
}
