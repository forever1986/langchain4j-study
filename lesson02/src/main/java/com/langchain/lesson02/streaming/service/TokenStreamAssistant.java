package com.langchain.lesson02.streaming.service;

import dev.langchain4j.service.TokenStream;

public interface TokenStreamAssistant {

    TokenStream chat(String userMessage);

}
