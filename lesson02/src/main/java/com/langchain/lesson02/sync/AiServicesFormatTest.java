package com.langchain.lesson02.sync;

import com.langchain.lesson02.sync.service.Book;
import com.langchain.lesson02.sync.service.FormatAssistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

/**
 * 格式化测试
 */
public class AiServicesFormatTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        FormatAssistant formatAssistant = AiServices.create(FormatAssistant.class, model);
        // 3.访问大模型
        Book response = formatAssistant.chat("给我推荐一本书");
        System.out.println(response);
    }
}
