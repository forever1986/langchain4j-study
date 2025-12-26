package com.langchain.lesson15.custom.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface PersonExtractor {

    @UserMessage("从以下提示中提取出内容：{{prompt}}，仅返回人名，其他信息一并忽略。")
    @Agent(value = "从用户的提示中提取出一个人物名称", name = "PersonExtractor")
    String extractPerson(@V("prompt") String prompt);

}
