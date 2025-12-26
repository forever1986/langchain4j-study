package com.langchain.lesson06.streaming.service;

import dev.langchain4j.service.TokenStream;

public interface StreamAssistant {

    TokenStream chat(String userMessage);

}
