package com.langchain.lesson06.advanced.error;

import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

public class AdvancedErrorTimeTool {

    @Tool("根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间")
    String getLocalTime(@P("国家或地区的名字") String zone) {
        return ToolUtils.getCurrentDateTime(zone);
    }
}
