package com.langchain.lesson05.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface AssistantWithId {

    // @MemoryId注解是用于聊天记忆会话id
    String chat(@MemoryId Long id, @UserMessage String userMessage);

}
