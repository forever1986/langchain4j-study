package com.langchain.lesson05.simple;

import com.langchain.lesson05.service.Assistant;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.TokenWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiTokenCountEstimator;
import dev.langchain4j.service.AiServices;

public class ChatMemoryTokenWindowTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. 构建聊天记忆的存储：TokenWindowChatMemory，不超过80个token
        // 注意这里的OpenAiTokenCountEstimator
        ChatMemory chatMemory = TokenWindowChatMemory.builder().maxTokens(80,new OpenAiTokenCountEstimator("gpt-5")).build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();
        // 4. 访问大模型
        String movies = assistant.chat("给我推荐5部电影，直接说出电影名称即可");
        System.out.println(movies);
        System.out.println("=============================");
        String best_movie = assistant.chat("在里面挑出你认为最好的一部，直接说出电影名称即可");
        System.out.println(best_movie);
        System.out.println("=============================");
        String worst_movie = assistant.chat("在里面挑出你认为最差的一部，直接说出电影名称即可");
        System.out.println(worst_movie);
    }
}
