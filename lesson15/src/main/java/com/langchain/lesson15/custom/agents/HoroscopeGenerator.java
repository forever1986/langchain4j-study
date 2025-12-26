package com.langchain.lesson15.custom.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface HoroscopeGenerator {

    @SystemMessage("你是一名占星师，能够根据用户的姓名和星座来生成星座爱情运势，仅返回简单的运势描述，其他信息一并忽略。")
    @UserMessage("为属于{{sign}}的{{person}}生成星座爱情运势。")
    @Agent(value = "一位根据用户姓名和星座来生成星座运势的占星师。", name = "HoroscopeGenerator")
    String horoscope(@V("person") String person, @V("sign") String sign);

}
