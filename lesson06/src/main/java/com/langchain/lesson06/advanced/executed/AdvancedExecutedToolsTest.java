package com.langchain.lesson06.advanced.executed;

import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;

import java.util.List;

public class AdvancedExecutedToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AdvancedExecutedAssistant advancedAssistant = AiServices.builder(AdvancedExecutedAssistant.class)
                .chatModel(model)
                .tools(new AdvancedExecutedTimeTool())
                .build();
        // 3. 访问大模型
        Result<String> result = advancedAssistant.chat("请问洛杉矶现在几点？");
        String answer = result.content();
        System.out.println(answer);
        // 4. 可以获取到工具调用的结果进行分析
        List<ToolExecution> toolExecutions = result.toolExecutions();
        ToolExecution toolExecution = toolExecutions.get(0); // 工具执行器
        ToolExecutionRequest request = toolExecution.request(); // 工具请求参数
        System.out.println(request);
        String text = toolExecution.result(); // 将工具执行的结果转换为text
        System.out.println(text);
        Object resultObject = toolExecution.resultObject(); // 实际上工具返回的对象
        System.out.println(resultObject.getClass());
    }
}
