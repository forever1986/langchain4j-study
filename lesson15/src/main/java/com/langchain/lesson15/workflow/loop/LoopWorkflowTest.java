package com.langchain.lesson15.workflow.loop;

import com.langchain.lesson15.workflow.loop.agents.StyleEditor;
import com.langchain.lesson15.workflow.loop.agents.StyleScorer;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Map;

public class LoopWorkflowTest {

    public static void main(String[] args) {

        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义创作agent
        StyleEditor styleEditor = AgenticServices
                .agentBuilder(StyleEditor.class)
                .chatModel(chatModel)
                .outputKey("story")
                .build();
        //4.定义风格打分agent
        StyleScorer styleScorer = AgenticServices
                .agentBuilder(StyleScorer.class)
                .chatModel(chatModel)
                .outputKey("score")
                .build();
        //5.通过loopBuilder创建循环workflow
        UntypedAgent styleReviewLoop = AgenticServices
                .loopBuilder()
                .subAgents(styleScorer, styleEditor)
                // 最大重试次数
                .maxIterations(5)
                // 判断条件
                .exitCondition( agenticScope -> agenticScope.readState("score", 0.0) >= 0.8)
                // 输出结果
                .output(agenticScope -> agenticScope.readState("score"))
                .build();
        //7.调用workflow
        Map<String, Object> input = Map.of(
                "story", """
                        夜幕低垂，繁星点点，仿佛无数璀璨的钻石洒落天际，将幽暗的夜空点缀得熠熠生辉。一只体型庞大的银翼巨龙，鳞片闪烁着月华般的光泽，缓缓自云端俯冲而下，盘旋于繁星之间，龙吟声似低沉的号角，回荡在静谧的夜空。 \s
                        不远处，一座古老的石台上，一位身着长袍的巫师端坐其上，手中握着一根泛着微光的魔杖，目光深邃地凝视着天际的巨龙。巫师的声音低沉而悠扬，仿佛蕴含着某种古老的咒语，在夜风中轻轻回荡。 \s
                        巨龙与巫师遥遥相望，眼中各自闪烁着智慧与力量的光芒，似在无声地较量，比试谁更为高明、更为强大。天空中，星光与龙鳞交相辉映，一场无声的对峙，在静谧的夜色中悄然展开。
                        """,
                "style", "幻想作品"
        );
        double score = (double) styleReviewLoop.invoke(input);
        System.out.println(score);
    }
}
