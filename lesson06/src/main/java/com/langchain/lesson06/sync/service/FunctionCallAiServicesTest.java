package com.langchain.lesson06.sync.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langchain.lesson06.sync.service.Assistant;
import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolExecutor;

import java.util.Map;

public class FunctionCallAiServicesTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.定义工具
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("getLocalTime")
                .description("根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("zone", "国家或地区的名字")
                        .required("zone") // 必填的属性应明确加以说明
                        .build())
                .build();
        // 3.定义工具执行器
        ToolExecutor toolExecutor = (request, memoryId) -> {
            String zoneId ="";
            try {
                JsonNode jsonNode = new ObjectMapper().readTree(request.arguments());
                zoneId = jsonNode.get("zone").textValue();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            return ToolUtils.getCurrentDateTime(zoneId);
        };
        // 4. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .tools(Map.of(toolSpecification,toolExecutor)) // 传入工具描述以及工具执行器
                .build();
        // 5. 访问大模型
        String response = assistant.chat("请问洛杉矶现在几点？");
        System.out.println(response);
    }
}
