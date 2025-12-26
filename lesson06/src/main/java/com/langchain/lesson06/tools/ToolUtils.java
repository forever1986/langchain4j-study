package com.langchain.lesson06.tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

public class ToolUtils {

    public static String getCurrentDateTime(String zone) {
        Map<String,String> map = new HashMap<>();
        map.put("北京","Asia/Shanghai");
        map.put("Beijing","Asia/Shanghai");
        map.put("伦敦","Europe/London");
        map.put("London","Europe/London");
        map.put("洛杉矶","America/Los_Angeles");
        map.put("Los Angeles","America/Los_Angeles");
        ZoneId zoneIdGMT = ZoneId.of(map.get(zone)==null?"Asia/Shanghai":map.get(zone));
        String time = LocalDateTime.now(zoneIdGMT).toString();
        System.out.println(time);
        return time;
    }

}
