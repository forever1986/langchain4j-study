package com.langchain.lesson05.simple;

import com.langchain.lesson05.service.AssistantWithId;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class ChatMemoryMessageWindowWithIdTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AssistantWithId assistantWithId = AiServices.builder(AssistantWithId.class)
                .chatModel(model)
                // 3. 构建聊天记忆的存储，采用chatMemoryProvider方式构建
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.builder().maxMessages(3).build())
                .build();
        // 4. 访问大模型
        String movies = assistantWithId.chat(1l,"给我推荐5部电影，直接说出电影名称即可");
        System.out.println(movies);
        System.out.println("=============================");
        // 注意，这里第二个问题使用的id=2，与前面并不是一个id
        String best_movie = assistantWithId.chat(2l,"在里面挑出你认为最好的一部，直接说出电影名称即可");
        System.out.println(best_movie);
        System.out.println("=============================");
        String worst_movie = assistantWithId.chat(1l,"在里面挑出你认为最差的一部，直接说出电影名称即可");
        System.out.println(worst_movie);
    }
}
