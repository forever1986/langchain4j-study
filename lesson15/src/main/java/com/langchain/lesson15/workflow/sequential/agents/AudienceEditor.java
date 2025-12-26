package com.langchain.lesson15.workflow.sequential.agents;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.agentic.Agent;

public interface AudienceEditor {

    @UserMessage("""
        你是一名专业的编辑。
        请对以下故事进行分析并重新编写，使其更符合{{audience}}的目标读者群体。
        仅返回故事内容，不要其他任何信息。
        故事内容为：“{{story}}”。
        """)
    @Agent("对故事进行修改，使其更符合特定的受众群体")
    String editAudienceStory(@V("story") String story, @V("audience") String audience);

}
