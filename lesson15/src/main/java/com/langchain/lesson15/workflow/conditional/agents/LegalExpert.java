package com.langchain.lesson15.workflow.conditional.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface LegalExpert {

    @UserMessage("""
            您是法律专家。
            请从法律角度对以下用户请求进行分析，并给出最合适的回答。
            用户请求为 {{request}} 。
        """)
    @Agent("一位法律专家")
    String medical(@V("request") String request);

}
