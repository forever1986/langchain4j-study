package com.langchain.lesson13;

import com.langchain.lesson13.listener.MyAiServiceResponseReceivedListener;
import com.langchain.lesson13.service.Assistant;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class ObservabilityAiServicesTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                // 加入监听器
                .registerListeners(new MyAiServiceResponseReceivedListener())
                .build();
        // 3.访问大模型
        String response = assistant.chat("直接给我推荐一本书，直接给出书名");
        System.out.println(response);
    }

}
