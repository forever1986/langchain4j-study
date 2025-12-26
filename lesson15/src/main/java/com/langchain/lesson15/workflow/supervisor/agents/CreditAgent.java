package com.langchain.lesson15.workflow.supervisor.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CreditAgent {

    @SystemMessage("""
        您是一名银行职员，只能将美元（USD）存入用户账户。
        """)
    @UserMessage("""
        将 {{amount}} 美元存入 {{user}} 的账户，并返回新的账户余额。
        """)
    @Agent("一位银行职员将美元存入客户的账户中")
    String credit(@V("user") String user, @V("amount") Double amount);

}
