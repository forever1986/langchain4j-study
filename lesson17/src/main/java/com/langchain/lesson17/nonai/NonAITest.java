package com.langchain.lesson17.nonai;

import com.langchain.lesson17.nonai.agents.ExchangeOperator;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;

import java.util.Map;

public class NonAITest {

    public static void main(String[] args) throws InterruptedException {

        // 1.定义sequenceBuilder
        UntypedAgent horoscopeAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(new ExchangeOperator())
                .outputKey("exchange")
                .build();
        // 2.调用结果
        Map<String, Object> input = Map.of(
                "originalCurrency", "美元",
                "targetCurrency", "欧元",
                "amount", 100
        );
        Double result = (Double) horoscopeAgent.invoke(input);
        System.out.println(result);

//         //使用规划SupervisorAgent也能实现
//        //1.获取API KEY
//        String apiKey = System.getenv("ZHIPU_API_KEY");
//        //2.加载大模型
//        ChatModel chatModel = OpenAiChatModel.builder()
//                .apiKey(apiKey)
//                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
//                .modelName("glm-4-flash-250414")
//                .build();
//        SupervisorAgent horoscopeAgent = AgenticServices
//                .supervisorBuilder()
//                .chatModel(chatModel)
//                .subAgents(new ExchangeOperator())
//                .outputKey("exchange")
//                .build();
//        //3.调用结果
//        String result = horoscopeAgent.invoke("100美元可以兑换多少欧元？");
//        System.out.println(result);
    }
}
