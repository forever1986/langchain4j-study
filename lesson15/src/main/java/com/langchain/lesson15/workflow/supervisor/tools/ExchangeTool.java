package com.langchain.lesson15.workflow.supervisor.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class ExchangeTool {

    @Tool("将给定金额从原币种兑换为目标币种")
    Double exchange(@P("originalCurrency") String originalCurrency, @P("amount") Double amount, @P("targetCurrency") String targetCurrency) {
        System.out.println("originalCurrency="+originalCurrency+",targetCurrency="+targetCurrency+",amount="+amount);
        // 这里只是简单模拟欧元与美元的对话
        if(("EUR".equals(originalCurrency)||"欧元".equals(originalCurrency))
                &&("USD".equals(targetCurrency)||"美元".equals(targetCurrency))){
            return amount * 1.178;
        }else if(("USD".equals(originalCurrency)||"美元".equals(originalCurrency))
                &&("EUR".equals(targetCurrency)||"欧元".equals(targetCurrency))){
            return amount * 0.8489;
        }else{
            return amount;
        }
    }

}
