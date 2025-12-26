package com.langchain.lesson17.nonai.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public class ExchangeOperator {

    @Agent(value = "一种货币兑换器，能够将一定金额的货币从原币转换为目标货币。",
            outputKey = "exchange")
    public Double exchange(@V("originalCurrency") String originalCurrency, @V("amount") Double amount, @V("targetCurrency") String targetCurrency) {
        System.out.println("originalCurrency="+originalCurrency+",targetCurrency="+targetCurrency+",amount="+amount);
        // 这里只是简单模拟欧元与美元的兑换
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
