package com.langchain.lesson15.workflow.parallel.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.List;

public interface FoodExpert {

    @UserMessage("""
            你是一位出色的晚间规划师。
             请根据给定的氛围列出三道适合的餐食。
             氛围为 {{mood}} 。
             对于每道餐食，只需给出其名称。
             请提供包含这三道菜品的清单，不要添加其他内容。
        """)
    @Agent
    List<String> findMeal(@V("mood") String mood);

}
