package com.langchain.lesson16.error;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.agentic.agent.MissingArgumentException;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class AgenticServicesErrorHandlerTest {

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
        //4.通过sequenceBuilder创建顺序workflow
        UntypedAgent novelCreator = AgenticServices
                .sequenceBuilder()
                .subAgents(creativeWriter)
                .errorHandler( errorContext -> {
                    System.out.println("===========有异常！===========");
                    // 从errorContext中判断错误根因
                    if (errorContext.agentName().equals("generateStory") &&
                            errorContext.exception() instanceof MissingArgumentException mEx && mEx.argumentName().equals("topic")) {
                        // 重新设置参数
                        errorContext.agenticScope().writeState("topic", "海贼王");
                        // 重试
                        return ErrorRecoveryResult.retry();
                    }
                    return ErrorRecoveryResult.throwException();
                })
                .outputKey("story")
                .build();
        //5.调用workflow
        Map<String, Object> input = Map.of(
                // 故意不传入topic参数
//                "topic", "龙与巫师",
                "style", "幻想作品",
                "audience", "幼儿园"
        );
        String story = (String) novelCreator.invoke(input);
        System.out.println(story);
    }

}
