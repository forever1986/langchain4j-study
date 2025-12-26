package com.langchain.lesson06.advanced.memory;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public interface AdvancedMemoryAssistant {

    String chat(@MemoryId Long memoryId, @UserMessage String userMessage);

}
