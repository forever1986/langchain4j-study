package com.langchain.lesson15.custom;

import com.langchain.lesson15.custom.agents.*;
import com.langchain.lesson15.custom.planner.GoalOrientedPlanner;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.web.search.WebSearchTool;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;

import java.util.Map;

public class CustomGoalOrientedWorkflowTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        String baseUrl = "https://open.bigmodel.cn/api/paas/v4";
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .modelName("glm-4-flash-250414")
                .build();
        PersonExtractor personExtractor = AgenticServices.agentBuilder(PersonExtractor.class)
                .chatModel(chatModel)
                .outputKey("person")
                .build();
        SignExtractor signExtractor = AgenticServices.agentBuilder(SignExtractor.class)
                .chatModel(chatModel)
                .outputKey("sign")
                .build();
        HoroscopeGenerator horoscopeGenerator = AgenticServices.agentBuilder(HoroscopeGenerator.class)
                .chatModel(chatModel)
                .outputKey("horoscope")
                .build();
        StoryFinder storyFinder = AgenticServices.agentBuilder(StoryFinder.class)
                .chatModel(chatModel)
                .tools(new WebSearchTool(TavilyWebSearchEngine.builder()
                        .apiKey(System.getenv("TAVILY_API_KEY"))
                        .build()))
                .outputKey("story")
                .build();
        Writer writer = AgenticServices.agentBuilder(Writer.class)
                .chatModel(chatModel)
                .outputKey("writeup")
                .build();
        UntypedAgent horoscopeAgent = AgenticServices
                .plannerBuilder()
                .subAgents(personExtractor, signExtractor, horoscopeGenerator, storyFinder, writer)
                .outputKey("writeup")
                .planner(GoalOrientedPlanner::new)
                .build();
        //8.调用workflow
        Map<String, Object> input = Map.of("prompt", "我的名字是Mario，我的星座是双鱼座。");
        String writeup = (String) horoscopeAgent.invoke(input);
        System.out.println(writeup);
    }

}
