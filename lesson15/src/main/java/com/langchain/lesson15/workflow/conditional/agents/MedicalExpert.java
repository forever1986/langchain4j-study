package com.langchain.lesson15.workflow.conditional.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface MedicalExpert {

    @UserMessage("""
            您是医学专家。
            请从医学角度对以下用户请求进行分析，并给出最合适的回答。
            用户请求为 {{request}} 。
        """)
    @Agent("一位医学专家")
    String medical(@V("request") String request);

}
