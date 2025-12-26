package com.langchain.lesson06.advanced.immediate;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;

public interface AdvancedImmediateAssistant {

    // 注意，如果是直接返回工具内容，则返回值一定要是Result
    Result<String> chat(@UserMessage String userMessage);

}
