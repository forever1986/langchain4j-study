package com.langchain.lesson15.custom.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface SignExtractor {

    @UserMessage("从以下提示中提取某人的星座：{{prompt}}，仅返回星座名，其他信息一并忽略。")
    @Agent(value = "从用户的提示中提取出一个人物的星座", name = "SignExtractor")
    String extractSign(@V("prompt") String prompt);

}
