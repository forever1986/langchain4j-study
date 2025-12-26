package com.langchain.lesson13.listener;

import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.listener.AiServiceResponseReceivedListener;

public class MyAiServiceResponseReceivedListener implements AiServiceResponseReceivedListener {

    @Override
    public void onEvent(AiServiceResponseReceivedEvent event) {
        System.out.println("invocationContext上下文内容："+event.invocationContext());
        System.out.println("大模型回复内容："+event.response().aiMessage().text());
        System.out.println("本次调用大模型名称："+event.response().modelName());
        System.out.println("本次消耗的token数："+event.response().tokenUsage());
        System.out.println("其它一些元数据："+event.response().metadata());
    }

}
