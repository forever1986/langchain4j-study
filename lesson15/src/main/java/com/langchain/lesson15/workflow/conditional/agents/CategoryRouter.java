package com.langchain.lesson15.workflow.conditional.agents;

import com.langchain.lesson15.workflow.conditional.RequestCategory;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CategoryRouter {

    @UserMessage("""
            分析以下用户请求，并将其归类为“legal”、“medical”或“technical”类别。
           如果该请求不属于上述任何类别，则将其归类为“未知”。
           仅回复其中一个类别名称，不要添加其他内容。
           用户请求为： '{{request}}' 。
        """)
    @Agent("对用户请求进行分类")
    RequestCategory classify(@V("request") String request);

}
