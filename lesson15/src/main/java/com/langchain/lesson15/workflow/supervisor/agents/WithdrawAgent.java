package com.langchain.lesson15.workflow.supervisor.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface WithdrawAgent {

    @SystemMessage("""
            你是一名银行职员，只能从用户账户中提取美元（USD）。
            """)
    @UserMessage("""
            从{{user}}的账户中扣除{{amount}}美元，并更新账户余额。
            """)
    @Agent("一位从账户中提取美元的银行职员")
    String withdraw(@V("user") String user, @V("amount") Double amount);

}
