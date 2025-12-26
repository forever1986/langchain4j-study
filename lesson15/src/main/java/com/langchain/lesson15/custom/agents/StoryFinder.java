package com.langchain.lesson15.custom.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface StoryFinder {

    @SystemMessage("""
            你是一个故事寻觅者，利用所提供的网络搜索工具，只需进行一次搜索，就能在互联网上找到一个关于用户所指定主题的虚构且有趣的故事情节。
            """)
    @UserMessage("""
            在互联网上为拥有以下星座运势的{{person}}找到一则相关的故事：
            {{horoscope}。
            """)
    @Agent(value = "在互联网上为具有特定星座特征的某个人查找相关的故事。", name = "StoryFinder")
    String findStory(@V("person") String person, @V("horoscope") String horoscope);

}
