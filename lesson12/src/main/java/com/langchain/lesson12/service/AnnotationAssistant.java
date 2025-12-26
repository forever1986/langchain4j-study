package com.langchain.lesson12.service;

import com.langchain.lesson12.guardrail.FirstInputGuardrail;
import com.langchain.lesson12.guardrail.FourthInputGuardrail;
import com.langchain.lesson12.guardrail.SecondInputGuardrail;
import dev.langchain4j.service.guardrail.InputGuardrails;

// 优先级最低
//@InputGuardrails({FirstInputGuardrail.class, FourthInputGuardrail.class}) // @InputGuardrails注解类，类下面所有方法都生效
public interface AnnotationAssistant {

    // 方法上的注解优先级比类的注解高
    @InputGuardrails({FirstInputGuardrail.class, SecondInputGuardrail.class, FourthInputGuardrail.class})
    String chat(String userMessage);

    String message(String userMessage);
}
