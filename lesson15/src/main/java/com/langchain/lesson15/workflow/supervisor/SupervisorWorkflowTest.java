package com.langchain.lesson15.workflow.supervisor;

import com.langchain.lesson15.workflow.sequential.agents.AudienceEditor;
import com.langchain.lesson15.workflow.sequential.agents.CreativeWriter;
import com.langchain.lesson15.workflow.sequential.agents.StyleEditor;
import com.langchain.lesson15.workflow.supervisor.agents.CreditAgent;
import com.langchain.lesson15.workflow.supervisor.agents.ExchangeAgent;
import com.langchain.lesson15.workflow.supervisor.agents.WithdrawAgent;
import com.langchain.lesson15.workflow.supervisor.tools.BankTool;
import com.langchain.lesson15.workflow.supervisor.tools.ExchangeTool;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.supervisor.SupervisorAgent;
import dev.langchain4j.agentic.supervisor.SupervisorResponseStrategy;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class SupervisorWorkflowTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        String baseUrl = "https://open.bigmodel.cn/api/paas/v4";
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName("glm-4-flash-250414")
                .build();
        BankTool bankTool = new BankTool();
        bankTool.createAccount("Mario", 1000.0);
        bankTool.createAccount("Georgios", 1000.0);
        //3.定义WithdrawAgent的agent
        WithdrawAgent withdrawAgent = AgenticServices
                .agentBuilder(WithdrawAgent.class)
                .chatModel(chatModel)
                .tools(bankTool) // 注入银行tool
                .build();
        //4.定义CreditAgent的agent
        CreditAgent creditAgent = AgenticServices
                .agentBuilder(CreditAgent.class)
                .chatModel(chatModel)
                .tools(bankTool) // 注入银行tool
                .build();
        //5.定义ExchangeAgent的agent
        ExchangeAgent exchangeAgent = AgenticServices
                .agentBuilder(ExchangeAgent.class)
                .chatModel(chatModel)
                .tools(new ExchangeTool()) // 注入汇率tool
                .build();
        //6.定义有推理能力的模型GLM-4.6（原先glm-4-flash-250414推理能力不行）
        ChatModel chatModelMax = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName("GLM-4.6")
                .build();
        //7.通过supervisorBuilder创建顺序workflow
        SupervisorAgent bankSupervisor = AgenticServices
                .supervisorBuilder()
                .chatModel(chatModelMax)
                .subAgents(withdrawAgent, creditAgent, exchangeAgent)
                .responseStrategy(SupervisorResponseStrategy.SUMMARY)
                .build();
        //8.调用workflow
        String result = bankSupervisor.invoke("从Mario的账户向Georgios的账户转账100欧元。");
        System.out.println(result);
    }

}
