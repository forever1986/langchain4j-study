package com.langchain.lesson03.prompt.lowlevel;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.List;

public class PromptChatModelTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        SystemMessage systemMessage = SystemMessage.from("你现在的身份是一个精通Java的专家，专门解决Java遇到的问题。");
        UserMessage userMessage = UserMessage.from("你是谁？");
        List<ChatMessage> list = new ArrayList<>();
        list.add(systemMessage);
        list.add(userMessage);
        ChatResponse response = model.chat(list);
        System.out.println(response.aiMessage().text());
    }
}
