package com.langchain.lesson17.human;

import com.langchain.lesson17.human.agents.AstrologyAgent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.workflow.HumanInTheLoop;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Scanner;

public class HumanInTest {

    public static void main(String[] args) throws InterruptedException {


        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义AstrologyAgent的agent
        AstrologyAgent astrologyAgent = AgenticServices
                .agentBuilder(AstrologyAgent.class)
                .chatModel(chatModel)
                .build();
        //4.定义HumanInTheLoop的agent
        HumanInTheLoop humanInTheLoop = AgenticServices
                .humanInTheLoopBuilder()
                .description("一个询问用户星座的程序")
//                .async(true) // 需要用户交互的，不能设置为异步
                .outputKey("sign")
                .requestWriter(request -> {
                    System.out.println(request);
                    System.out.print("> ");
                })
                .responseReader(() -> {
                    Scanner in = new Scanner(System.in);
                    return in.nextLine();
                })
                .build();
        // 5.使用supervisorBuilder构建Supervisor workflow
        SupervisorAgent horoscopeAgent = AgenticServices
                .supervisorBuilder()
                .chatModel(chatModel)
                .subAgents(astrologyAgent, humanInTheLoop)
                .build();
        //6.运行结果
        String result = horoscopeAgent.invoke("我的名字是Mario。请问我的星座运势如何？");
        System.out.println(result);
    }
}
