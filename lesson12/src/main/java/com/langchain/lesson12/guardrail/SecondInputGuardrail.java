package com.langchain.lesson12.guardrail;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.guardrail.InputGuardrail;
import dev.langchain4j.guardrail.InputGuardrailResult;

public class SecondInputGuardrail implements InputGuardrail {

    @Override
    public InputGuardrailResult validate(UserMessage userMessage) {
        System.out.println("=========SecondInputGuardrail===========");
        return failure("error second");
    }

}
