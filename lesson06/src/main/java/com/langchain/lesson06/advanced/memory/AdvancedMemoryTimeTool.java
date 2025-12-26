package com.langchain.lesson06.advanced.memory;

import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;

public class AdvancedMemoryTimeTool {

    // 增加了@ToolMemoryId注解的参数memoryId，用于传递聊天记忆ID
    @Tool("根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间")
    String getLocalTime(@P("国家或地区的名字") String zone, @ToolMemoryId Long memoryId) {
        // 这里的memoryId就是从AiServices定义的接口传过来的
        System.out.println("memoryId:"+memoryId);
        return ToolUtils.getCurrentDateTime(zone);
    }
}
