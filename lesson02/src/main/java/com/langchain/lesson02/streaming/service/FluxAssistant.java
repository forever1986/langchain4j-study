package com.langchain.lesson02.streaming.service;

import reactor.core.publisher.Flux;

public interface FluxAssistant {

    Flux<String> chat(String userMessage);

}
