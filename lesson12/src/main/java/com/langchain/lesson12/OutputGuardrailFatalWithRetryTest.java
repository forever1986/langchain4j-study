package com.langchain.lesson12;

import com.langchain.lesson12.guardrail.FatalWithRetryOutputGuardrail;
import com.langchain.lesson12.service.Assistant;
import dev.langchain4j.guardrail.InputGuardrailException;
import dev.langchain4j.guardrail.config.OutputGuardrailsConfig;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class OutputGuardrailFatalWithRetryTest {

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
                // Fatal With Retry情况下，必须设置聊天记忆（因为重试会将聊天记忆发送给大模型）
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                // 设置FatalWithRetryOutputGuardrail
                .outputGuardrails(new FatalWithRetryOutputGuardrail())
                // 设置重试次数
                .outputGuardrailsConfig(OutputGuardrailsConfig.builder().maxRetries(3).build())
                .build();
        // 3.访问大模型
        try {
            String response = assistant.chat("直接给我推荐一本书");
            System.out.println(response);
        }catch (InputGuardrailException ge){
            // 异常处理
            System.out.println(ge.getMessage());
        }

    }

}
