package com.langchain.lesson03.prompt.highlevel;

import com.langchain.lesson03.prompt.highlevel.service.Assistant;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromptTemplateAiServicesTest {

    public static void main(String[] args) {
        // 1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 定义模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. 提示词模板
        PromptTemplate promptTemplate = PromptTemplate.from("你的名字叫{{name}}，你是一个{{type}}风格的旅游博主，专门介绍和推荐{{type}}风格相关的旅游内容。在每次回答用户的问题之前先说:'你好，我叫{{name}}，今天是{{current_date}}，很高兴回答你的问题'，再回答用户的问题");
        // 4. 设置模版中的参数
        Prompt prompt = promptTemplate.apply(Map.of("name","周星驰","type","小众"));
        SystemMessage systemMessage = prompt.toSystemMessage();
        UserMessage userMessage = UserMessage.from("你是谁？");
        List<ChatMessage> list = new ArrayList<>();
        list.add(systemMessage);
        list.add(userMessage);
        // 5. 访问大模型
        Assistant assistant = AiServices.create(Assistant.class, model);
        String response = assistant.chat(list);
        System.out.println(response);

    }
}
