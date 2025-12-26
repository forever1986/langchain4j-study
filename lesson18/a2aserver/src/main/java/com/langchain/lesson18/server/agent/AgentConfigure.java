package com.langchain.lesson18.server.agent;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgentConfigure {

    // 创建ChatModel
    @Bean
    public ChatModel chatModel(){
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        return chatModel;
    }

    // 创建Agent
    @Bean
    public CreativeWriter creativeWriter(ChatModel chatModel){
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class) // 代理的类
                .chatModel(chatModel) // 大语言模型
                .build();
        return creativeWriter;
    }
}
