package com.langchain.lesson06.sync.chatmodel;

import com.langchain.lesson06.tools.TimeTool;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.tool.ToolService;

import java.util.Arrays;
import java.util.List;

public class FunctionCallChatModel2Test {

    public static void main(String[] args) {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.定义工具-这里通过ToolService简便将注解解析为ToolSpecification和ToolExecutor
        ToolService toolService = new ToolService();
        toolService.tools(List.of(new TimeTool()));
        // 3.定义请求消息，将工具的定义放到请求消息中
        UserMessage userMessage = UserMessage.from("请问洛杉矶现在几点？");
        ChatRequest request = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolService.toolSpecifications())
                .build();
        // 4.创建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 5.调用模型
        ChatResponse response = model.chat(request);
        // 6.结果处理，通过hasToolExecutionRequests判断模型是否要调用工具
        if(response.aiMessage().hasToolExecutionRequests()){
            // 6.1 需要调用工具-这里做一个比较简单的处理
            // 获取大模型回复结果中的ToolExecutionRequest（其实就是工具的请求）
            ToolExecutionRequest toolExecutionRequest = response.aiMessage().toolExecutionRequests().get(0);
            System.out.println(toolExecutionRequest.arguments());
            // 使用ToolExecutor调用工具（这个也是AiServices的实现逻辑，这里手动复用）
            String functionCallResult = toolService.toolExecutors().get(toolExecutionRequest.name()).execute(toolExecutionRequest, 1);
            // 将结果以及之前的message一同给大模型，再次得到最新结果。
            ToolExecutionResultMessage resultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, functionCallResult);
            ChatRequest newChatRequest = ChatRequest.builder()
                    .messages(Arrays.asList(userMessage,response.aiMessage(),resultMessage))
                    .build();
            // 输出最终结果
            System.out.println(model.chat(newChatRequest).aiMessage().text());
        }else {
            //6.2 不需要调用工具，则直接输出
            System.out.println(response.aiMessage());
        }
    }
}
