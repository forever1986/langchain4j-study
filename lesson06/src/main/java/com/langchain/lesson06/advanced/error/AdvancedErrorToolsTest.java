package com.langchain.lesson06.advanced.error;

import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolErrorHandlerResult;

import java.util.Map;

public class AdvancedErrorToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AdvancedErrorAssistant advancedAssistant = AiServices.builder(AdvancedErrorAssistant.class)
                .chatModel(model)
                .tools(new AdvancedErrorTimeTool())
                // 处理没有对应工具名称的
                .hallucinatedToolNameStrategy(toolExecutionRequest -> ToolExecutionResultMessage.from(
                        toolExecutionRequest, "错误: 找不到工具名称为： " + toolExecutionRequest.name()))
                // 处理调用工具方式参数错误的
                .toolArgumentsErrorHandler((error, errorContext) -> ToolErrorHandlerResult.text(
                        "错误: 调用工具 " + errorContext.toolExecutionRequest().name() + " 发生参数错误，错误原因：" + error.getMessage()))
                .toolExecutionErrorHandler((error, errorContext) -> ToolErrorHandlerResult.text(
                        "错误：调用工具 " + errorContext.toolExecutionRequest().name() + " 执行过程中发生错误，错误原因: " + error.getMessage()))
                .build();
        // 3. 访问大模型：注意这里设置了一个参数进行传递
        String result = advancedAssistant.chat("请问洛杉矶现在几点？");
        System.out.println(result);
    }
}
