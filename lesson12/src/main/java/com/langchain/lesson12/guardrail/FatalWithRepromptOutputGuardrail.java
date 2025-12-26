package com.langchain.lesson12.guardrail;

import com.langchain.lesson12.service.Book;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import dev.langchain4j.internal.Json;

public class FatalWithRepromptOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        try {
            Json.fromJson(responseFromLLM.text(), Book.class);
        }catch (Exception e){
            System.out.println("=========FatalWithRepromptOutputGuardrail reprompt ===========");
            // 此处重新更新提示词
            return reprompt("error format", """
                请按照以下json格式返回结果，直接返回结果，前后不要增加其它注释：
                {
                  "bookName" : "aaa",
                  "author" : "bbbb"
                }
                """);
        }
        System.out.println("=========FatalWithRepromptOutputGuardrail success ===========");
        return success();
    }

}