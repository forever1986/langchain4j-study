package com.langchain.lesson12;

import com.langchain.lesson12.guardrail.FirstInputGuardrail;
import com.langchain.lesson12.guardrail.FourthInputGuardrail;
import com.langchain.lesson12.guardrail.SecondInputGuardrail;
import com.langchain.lesson12.guardrail.ThirdInputGuardrail;
import com.langchain.lesson12.service.AnnotationAssistant;
import com.langchain.lesson12.service.Assistant;
import dev.langchain4j.guardrail.InputGuardrailException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class InputGuardrailAnnotationTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        AnnotationAssistant assistant = AiServices.builder(AnnotationAssistant.class)
                .chatModel(model)
                // 代码配置的优先级比注解的高
//                .inputGuardrails(new FirstInputGuardrail(), new SecondInputGuardrail(), new ThirdInputGuardrail(), new FourthInputGuardrail())
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
