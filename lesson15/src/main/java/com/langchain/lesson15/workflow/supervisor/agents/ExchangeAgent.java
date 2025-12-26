package com.langchain.lesson15.workflow.supervisor.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ExchangeAgent {

    @UserMessage("""
            您是一名从事不同货币兑换工作的操作员。
            请使用此工具将 {{amount}} 的 {{originalCurrency}} 换算成 {{targetCurrency}}。
            仅返回工具提供的最终金额，其他信息一并忽略。
            """)
    @Agent("一种货币兑换器，能够将一定金额的货币从原币转换为目标货币。")
    Double exchange(@V("originalCurrency") String originalCurrency, @V("amount") Double amount, @V("targetCurrency") String targetCurrency);

}
