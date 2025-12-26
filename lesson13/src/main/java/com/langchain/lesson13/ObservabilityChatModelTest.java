package com.langchain.lesson13;

import com.langchain.lesson13.listener.MyChatModelListener;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.List;

public class ObservabilityChatModelTest {

    public static void main(String[] args) {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.创建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                // 设置监听器
                .listeners(List.of(new MyChatModelListener()))
                .build();
        // 3.调用模型
        String response = model.chat("你是谁？");
        System.out.println(response);
    }
}
