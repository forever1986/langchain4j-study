package com.langchain.lesson16.observability;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class AgenticServicesObservabilityTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义CreativeWriter的agent
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class)
                // 设置agent的beforeListener
                .beforeAgentInvocation(agentRequest -> {
                    System.out.println("Invoking CreativeWriter with topic: " + agentRequest.inputs().get("topic"));
                })
                // 设置agent的afterListener
                .afterAgentInvocation(agentResponse -> {
                    System.out.println("Current creativeWriter: " + agentResponse.output());
                })
                .chatModel(chatModel)
                .outputKey("story")
                .build();
        //4.通过sequenceBuilder创建顺序workflow
        UntypedAgent  novelCreator = AgenticServices
                .sequenceBuilder()
                .subAgents(creativeWriter)
                .outputKey("story")
                .build();
        //5.调用workflow
        Map<String, Object> input = Map.of(
                "topic", "龙与巫师"
        );
        String story = (String) novelCreator.invoke(input);
        System.out.println(story);
    }

}
