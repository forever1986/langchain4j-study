package com.langchain.lesson06.advanced.dynamically;

import dev.langchain4j.service.UserMessage;

public interface AdvancedDynamicallyAssistant {

    String chat(@UserMessage String userMessage);

}
