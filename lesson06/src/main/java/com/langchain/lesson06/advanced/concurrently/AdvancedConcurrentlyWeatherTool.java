package com.langchain.lesson06.advanced.concurrently;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class AdvancedConcurrentlyWeatherTool {

    @Tool("根据传入城市的名字，获取所在城市的天气情况")
    String getWeather(@P("城市名字") String city) {
        // 打印当前线程名称
        System.out.println("getWeather Thread Name="+Thread.currentThread().getName());
        // 这里就模拟获取天气情况
        Map<String,String> map = new HashMap<>();
        map.put("北京","晴，气温2℃-10℃");
        map.put("Beijing","晴，气温2℃-10℃");
        map.put("伦敦","小雨，气温-4℃-6℃");
        map.put("London","小雨，气温-4℃-6℃");
        map.put("洛杉矶","多云，气温4℃-12℃");
        map.put("Los Angeles","多云，气温4℃-12℃");
        String weather = map.get(city)==null?map.get("北京"):map.get(city);
        return weather;
    }
}
