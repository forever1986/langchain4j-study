package com.langchain.lesson15.simpleagent;

import com.langchain.lesson15.workflow.sequential.agents.CreativeWriter;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class AgentTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. Agent创建
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class) // 代理的类
                .chatModel(chatModel) // 大语言模型
//                .name("name") // agent的名称
//                .description("description") // agent的描述
//                .async(false) // 是否异步执行
//                .outputKey("story") // 输出参数名称
                .build();
        // 4. Agent执行
        String story =creativeWriter.generateStory("龙与巫师");
        System.out.println(story);
    }

}
