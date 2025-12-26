package com.langchain.lesson06.advanced.immediate;

import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.Tool;

public class AdvancedImmediateTimeTool {

    // 注意这里注入了returnBehavior属性为IMMEDIATE，表示立即返回无需大模型再次处理，默认情况下是TO_LLM
    @Tool(value = "根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间", returnBehavior = ReturnBehavior.IMMEDIATE)
    String getLocalTime(@P("国家或地区的名字") String zone) {
        return ToolUtils.getCurrentDateTime(zone);
    }
}
