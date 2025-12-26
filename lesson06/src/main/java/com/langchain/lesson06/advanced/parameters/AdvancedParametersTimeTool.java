package com.langchain.lesson06.advanced.parameters;

import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.invocation.InvocationParameters;

public class AdvancedParametersTimeTool {

    // 注意工具方法的定义，增加了InvocationParameters，用于接受来自外部的参数
    @Tool("根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间")
    String getLocalTime(@P("国家或地区的名字") String zone, InvocationParameters parameters) {
        // 这里的parameters就是从AiServices定义的接口传过来的
        System.out.println("parameters:"+parameters);
        return ToolUtils.getCurrentDateTime(zone);
    }
}
