package com.langchain.lesson06.advanced.concurrently;

import dev.langchain4j.service.UserMessage;

public interface AdvancedConcurrentlyAssistant {

    String chat(@UserMessage String userMessage);

}
