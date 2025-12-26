package com.langchain.lesson03.prompt.highlevel.service;

import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface Assistant {

    // 注意接口的入参是一个List<ChatMessage
    String chat(List<ChatMessage> list);

}
