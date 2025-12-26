package com.langchain.lesson07;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class MCPClientStreamableHttpTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 通过McpTransport定义高德地图的MCP服务
        McpTransport transport = StreamableHttpMcpTransport.builder()
                .url("https://mcp.api-inference.modelscope.net/100452eabd6641/mcp")
                .logRequests(true) // if you want to see the traffic in the log
                .logResponses(true)
                .build();
        // 3. 定义McpClient
        McpClient mcpClient = DefaultMcpClient.builder()
                .key("amap")
                .transport(transport)
                .build();
        // 4. 将McpClient封装为一个McpToolProvider
        McpToolProvider toolProvider = McpToolProvider.builder()
                .mcpClients(mcpClient)
                .build();
        // 5. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 6. 通过AiServices，将McpToolProvider作为一个工具提供者放进去使用
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .toolProvider(toolProvider)
                .build();
        // 7. 访问大模型
        String result = assistant.chat("帮我规划从天安门驾车到首都机场T1航站楼");
        System.out.println(result);
    }
}
