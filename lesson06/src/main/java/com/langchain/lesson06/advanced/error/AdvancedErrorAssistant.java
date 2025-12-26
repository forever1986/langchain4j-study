package com.langchain.lesson06.advanced.error;

import dev.langchain4j.service.UserMessage;

public interface AdvancedErrorAssistant {

    String chat(@UserMessage String userMessage);

}
