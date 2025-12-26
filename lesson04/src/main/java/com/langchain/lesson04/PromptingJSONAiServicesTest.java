package com.langchain.lesson04;

import com.langchain.lesson04.service.Book;
import com.langchain.lesson04.service.FormatAssistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class PromptingJSONAiServicesTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        FormatAssistant formatAssistant = AiServices.create(FormatAssistant.class,model);
        Book book = formatAssistant.chat("《活着》是余华在1993年写的书，书中描述主人公在特殊时期经历的故事");
        System.out.println(book);
    }
}
