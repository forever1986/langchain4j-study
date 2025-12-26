package com.langchain.lesson15.workflow.sequential;

import com.langchain.lesson15.workflow.sequential.agents.AudienceEditor;
import com.langchain.lesson15.workflow.sequential.agents.CreativeWriter;
import com.langchain.lesson15.workflow.sequential.agents.StyleEditor;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.scope.DefaultAgenticScope;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class SequentialWorkflowTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义CreativeWriter的agent
        CreativeWriter creativeWriter = AgenticServices
                .agentBuilder(CreativeWriter.class)
                .chatModel(chatModel)
                .outputKey("story")
                .build();
        //4.定义AudienceEditor的agent
        AudienceEditor audienceEditor = AgenticServices
                .agentBuilder(AudienceEditor.class)
                .chatModel(chatModel)
                .outputKey("story")
                .build();
        //5.定义StyleEditor的agent
        StyleEditor styleEditor = AgenticServices
                .agentBuilder(StyleEditor.class)
                .chatModel(chatModel)
                .outputKey("story")
                .build();
        //6.通过sequenceBuilder创建顺序workflow
        UntypedAgent novelCreator = AgenticServices
                .sequenceBuilder()
                .subAgents(creativeWriter, audienceEditor, styleEditor)
                .outputKey("story")
                .build();
        //7.调用workflow
        Map<String, Object> input = Map.of(
                "topic", "龙与巫师",
                "style", "幻想作品",
                "audience", "幼儿园"
        );
        String story = (String) novelCreator.invoke(input);
        System.out.println(story);
    }

}
