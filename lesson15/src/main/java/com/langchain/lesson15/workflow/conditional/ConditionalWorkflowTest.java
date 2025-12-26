package com.langchain.lesson15.workflow.conditional;

import com.langchain.lesson15.workflow.conditional.agents.CategoryRouter;
import com.langchain.lesson15.workflow.conditional.agents.LegalExpert;
import com.langchain.lesson15.workflow.conditional.agents.MedicalExpert;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class ConditionalWorkflowTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义CategoryRouter的agent
        CategoryRouter routerAgent = AgenticServices
                .agentBuilder(CategoryRouter.class)
                .chatModel(chatModel)
                .outputKey("category")
                .build();
        //4.定义MedicalExpert的agent
        MedicalExpert medicalExpert = AgenticServices
                .agentBuilder(MedicalExpert.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();
        //5.定义LegalExpert的agent
        LegalExpert legalExpert = AgenticServices
                .agentBuilder(LegalExpert.class)
                .chatModel(chatModel)
                .outputKey("response")
                .build();
        //6.通过conditionalBuilder创建条件的workflow
        UntypedAgent expertsAgent = AgenticServices.conditionalBuilder()
                .subAgents( agenticScope -> agenticScope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.MEDICAL, medicalExpert)
                .subAgents( agenticScope -> agenticScope.readState("category", RequestCategory.UNKNOWN) == RequestCategory.LEGAL, legalExpert)
                .build();
        //7.通过sequenceBuilder创建顺序workflow（将routerAgent和expertsAgent组合顺序执行）
        UntypedAgent expertRouterAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(routerAgent, expertsAgent)
                .outputKey("response")
                .build();
        //8.调用workflow
        Map<String, Object> input = Map.of(
                "request", "我的腿骨折了，我该怎么办？"
        );
        String response = (String)expertRouterAgent.invoke(input);
        System.out.println(response);
    }

}
