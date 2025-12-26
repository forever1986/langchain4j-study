package com.langchain.lesson15.workflow.sequential.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface StyleEditor {

    @UserMessage("""
         你是一名专业的编辑。
         对以下故事进行分析并重新编写，使其更符合并更具连贯性地符合{{style}}风格。
         仅返回故事内容，不要其他任何内容。
         故事内容为：“{{story}}”。
        """)
    @Agent("对故事进行修改，使其更符合特定的风格")
    String editStyleStory(@V("story") String story, @V("style") String style);

}
