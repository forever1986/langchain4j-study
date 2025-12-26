package com.langchain.lesson16.annotation.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface MovieExpert {

    @UserMessage("""
            你是一位出色的晚间节目策划者。
         请根据给定的氛围列出三部合适的电影。
         氛围为 {{mood}} 。
         请提供包含这三部影片的清单，不要添加其他内容。
        """)
    // 采用全注解方式，则需要在这里设置注解内容
    @Agent(outputKey = "movies")
    List<String> findMovie(@V("mood") String mood);

}
