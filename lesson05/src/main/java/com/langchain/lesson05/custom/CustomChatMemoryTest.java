package com.langchain.lesson05.custom;

import com.langchain.lesson05.service.AssistantWithId;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import redis.clients.jedis.Jedis;

public class CustomChatMemoryTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 连接redis数据库
        Jedis jedis = new Jedis("localhost", 6379);
        // 3. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AssistantWithId assistantWithId = AiServices.builder(AssistantWithId.class)
                .chatModel(model)
                // 4. 构建聊天记忆的存储，采用chatMemoryProvider方式构建自定义的CustomChatMemory和CustomChatMemoryStore
                .chatMemoryProvider(memoryId -> new CustomChatMemory(memoryId,new CustomChatMemoryStore(jedis),model))
                .build();
        // 5. 访问大模型
        String movies = assistantWithId.chat(1l,"给我推荐5部电影，直接说出电影名称即可");
        System.out.println(movies);
        System.out.println("=============================");
        String best_movie = assistantWithId.chat(1l,"在里面挑出你认为最好的一部，直接说出电影名称即可");
        System.out.println(best_movie);
        System.out.println("=============================");
        String worst_movie = assistantWithId.chat(1l,"在里面挑出你认为最差的一部，直接说出电影名称即可");
        System.out.println(worst_movie);
    }
}
