package com.langchain.lesson06.advanced.concurrently;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdvancedConcurrentlyToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. 自定义一个执行线程池
        ExecutorService executor = Executors.newFixedThreadPool(5);
        AdvancedConcurrentlyAssistant advancedAssistant = AiServices.builder(AdvancedConcurrentlyAssistant.class)
                .chatModel(model)
                // 传入两个工具
                .tools(new AdvancedConcurrentlyTimeTool(), new AdvancedConcurrentlyWeatherTool())
                .executeToolsConcurrently(executor)
                .build();
        // 4. 访问大模型
        String result = advancedAssistant.chat("请问洛杉矶现在天气如何？本地时间现在几点？");
        System.out.println(result);
        executor.close();
    }
}
