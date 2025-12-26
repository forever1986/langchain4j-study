package com.langchain.lesson16.annotation;

import com.langchain.lesson16.annotation.agent.EveningPlannerAgent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;

public class AgenticServicesAnnotationTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.通过createAgenticSystem创建顺序workflow
        EveningPlannerAgent planner = AgenticServices
                .createAgenticSystem(EveningPlannerAgent.class, chatModel);
        //4.调用workflow
        List<EveningPlan> plans = (List<EveningPlan>)planner.plan("浪漫");
        System.out.println(plans);
    }

}
