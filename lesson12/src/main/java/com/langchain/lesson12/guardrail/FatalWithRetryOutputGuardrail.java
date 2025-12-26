package com.langchain.lesson12.guardrail;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailResult;

public class FatalWithRetryOutputGuardrail implements OutputGuardrail {

    private int i = 0; // 模拟失败重新

    @Override
    public OutputGuardrailResult validate(AiMessage responseFromLLM) {
        // 模拟失败重试一次
        if(i<1){
            i++;
            System.out.println("=========FatalWithRetryOutputGuardrail retry===========");
            return retry("error first");
        }else{
            System.out.println("=========FatalWithRetryOutputGuardrail success===========");
            return success();
        }
    }
}
