package com.langchain.lesson03.parameter;

import com.langchain.lesson03.parameter.service.ParameterAssistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class ParameterAiServicesTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .maxTokens(20) // 可生成的最大token数
                .temperature(1.0) // 指定大模型采用何种采样温度，范围在 0 到 2 之间。数值越高会使输出结果更加随机
                .build();
        ParameterAssistant parameterAssistant = AiServices.create(ParameterAssistant.class, model);
        // 3.访问大模型
        String response = parameterAssistant.chat("直接给我推荐一本书");
        System.out.println(response);
    }
}
