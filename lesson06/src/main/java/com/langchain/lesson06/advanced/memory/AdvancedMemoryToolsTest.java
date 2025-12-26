package com.langchain.lesson06.advanced.memory;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class AdvancedMemoryToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AdvancedMemoryAssistant advancedAssistant = AiServices.builder(AdvancedMemoryAssistant.class)
                .chatModel(model)
                // 构建聊天记忆的存储，采用chatMemoryProvider方式构建
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder().maxMessages(3).build())
                .tools(new AdvancedMemoryTimeTool())
                .build();
        // 3. 访问大模型
        String result = advancedAssistant.chat(1l,"请问洛杉矶现在几点？");
        System.out.println(result);
    }
}
