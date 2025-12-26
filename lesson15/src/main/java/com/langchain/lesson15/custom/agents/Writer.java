package com.langchain.lesson15.custom.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface Writer {

    @UserMessage("""
            根据以下内容为{{person}}创作一篇有趣的文章：
            - 其星座运势：{{horoscope}}
            - 当前的故事：{{story}}
            """)
    @Agent(value = "根据目标人物的星座运势和当前的故事，为其撰写一篇有趣的文章。", name = "Writer")
    String write(@V("person") String person, @V("horoscope") String horoscope, @V("story") String story);

}
