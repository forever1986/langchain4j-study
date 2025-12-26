package com.langchain.lesson07;

import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.McpReadResourceResult;
import dev.langchain4j.mcp.client.McpResource;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;

import java.util.List;

public class MCPResourceTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 通过StreamableHttpMcpTransport连接魔塔中启动的MCP服务
        McpTransport transport = StreamableHttpMcpTransport.builder()
                .url("https://mcp.api-inference.modelscope.net/41adfcbff68348/mcp")
                .logRequests(true) // if you want to see the traffic in the log
                .logResponses(true)
                .build();
        // 3. 定义McpClient
        McpClient mcpClient = DefaultMcpClient.builder()
                .key("12306-mcp")
                .transport(transport)
                .build();
        // 4. 获得MCP中的资源列表
        List<McpResource> listResource = mcpClient.listResources();
        System.out.println(listResource);
        // 5. 请求其中第一个资源
        McpReadResourceResult result = mcpClient.readResource(listResource.get(0).uri());
        System.out.println(result.contents());
    }
}
