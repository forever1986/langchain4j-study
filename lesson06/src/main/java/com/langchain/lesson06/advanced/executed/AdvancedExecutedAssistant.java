package com.langchain.lesson06.advanced.executed;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;

public interface AdvancedExecutedAssistant {

    // 注意这里的返回值被Result给封装了
    Result<String> chat(@UserMessage String userMessage);

}
