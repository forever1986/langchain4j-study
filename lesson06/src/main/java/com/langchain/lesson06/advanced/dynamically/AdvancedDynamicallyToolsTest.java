package com.langchain.lesson06.advanced.dynamically;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.service.tool.ToolProviderResult;

import java.util.HashMap;
import java.util.Map;

public class AdvancedDynamicallyToolsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.定义工具执行器
        ToolExecutor toolExecutor = (request, memoryId) -> {
            // 这里为打印为了识别最终调用了哪个工具
            System.out.println("===========执行getWeather工具============");
            String city ="";
            try {
                JsonNode jsonNode = new ObjectMapper().readTree(request.arguments());
                city = jsonNode.get("city").textValue();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            // 这里就模拟获取天气情况
            Map<String,String> map = new HashMap<>();
            map.put("北京","晴，气温2℃-10℃");
            map.put("Beijing","晴，气温2℃-10℃");
            map.put("伦敦","小雨，气温-4℃-6℃");
            map.put("London","小雨，气温-4℃-6℃");
            map.put("洛杉矶","多云，气温4℃-12℃");
            map.put("Los Angeles","多云，气温4℃-12℃");
            return map.get(city)==null?map.get("北京"):map.get(city);
        };
        // 3. 自定义ToolProvider，可以编程方式决定使用工具，也可以加入外部工具
        ToolProvider toolProvider = (toolProviderRequest) -> {
            if (toolProviderRequest.userMessage().singleText().contains("天气")) {
                System.out.println("动态加载获取天气的工具");
                ToolSpecification toolSpecification = ToolSpecification.builder()
                        .name("getWeather")
                        .description("根据传入城市的名字，获取所在城市的天气情况")
                        .parameters(JsonObjectSchema.builder()
                                .addStringProperty("city", "城市名字")
                                .build())
                        .build();
                return ToolProviderResult.builder()
                        .add(toolSpecification, toolExecutor)
                        .build();
            } else {
                return null;
            }
        };
        // 4. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AdvancedDynamicallyAssistant advancedAssistant = AiServices.builder(AdvancedDynamicallyAssistant.class)
                .chatModel(model)
                .tools(new AdvancedDynamicallyTimeTool())
                .toolProvider(toolProvider)
                .build();
        // 4. 访问大模型
        System.out.println("==================问题1:请问洛杉矶现在天气如何？本地时间现在几点？==========================");
        String result = advancedAssistant.chat("请问洛杉矶现在天气如何？本地时间现在几点？");
        System.out.println(result);
        System.out.println("==================问题2:请问洛杉矶现在下雨吗？请问洛杉矶现在几点？==========================");
        result = advancedAssistant.chat("请问洛杉矶现在下雨吗？请问洛杉矶现在几点？");
        System.out.println(result);
    }
}
