package com.langchain.lesson03.prompt.highlevel;

import com.langchain.lesson03.prompt.highlevel.service.Assistant;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.ArrayList;
import java.util.List;

public class PromptAiServicesTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 这条SystemMessage，你可以将其放到Assistant接口的chat方法@SystemMessage注解中
        SystemMessage systemMessage = SystemMessage.from("你现在的身份是一个精通Java的专家，专门解决Java遇到的问题。");
        UserMessage userMessage = UserMessage.from("你是谁？");
        List<ChatMessage> list = new ArrayList<>();
        list.add(systemMessage);
        list.add(userMessage);
        Assistant assistant = AiServices.create(Assistant.class, model);
        String response = assistant.chat(list);
        System.out.println(response);
    }
}
