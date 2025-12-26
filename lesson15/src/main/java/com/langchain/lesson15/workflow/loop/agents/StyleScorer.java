package com.langchain.lesson15.workflow.loop.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface StyleScorer {

    @UserMessage("""
            您是一名专业的评论者。
             请根据故事与“{{style}}”风格的契合程度，为其给出一个介于 0.0 到 1.0 之间的评分。
             仅返回评分，不要包含其他任何内容。
             故事内容为：“{{story}}”
            """)
    @Agent("根据一个故事与特定风格的契合程度来给其打分")
    double scoreStyle(@V("story") String story, @V("style") String style);

}
