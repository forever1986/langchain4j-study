package com.langchain.lesson06.advanced.parameters;

import dev.langchain4j.invocation.InvocationParameters;
import dev.langchain4j.service.UserMessage;

public interface AdvancedParametersAssistant {

    // 注意，这里的接口定义多了一个InvocationParameters参数，就是为了传递外部参数到工具中
    String chat(@UserMessage String userMessage, InvocationParameters parameters);

}
