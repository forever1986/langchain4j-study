package com.langchain.lesson15.workflow.parallel;

import com.langchain.lesson15.workflow.parallel.agent.FoodExpert;
import com.langchain.lesson15.workflow.parallel.agent.MovieExpert;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelWorkflowTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义FoodExpert的agent
        FoodExpert foodExpert = AgenticServices
                .agentBuilder(FoodExpert.class)
                .chatModel(chatModel)
                .outputKey("meals")
                .build();
        //4.定义MovieExpert的agent
        MovieExpert movieExpert = AgenticServices
                .agentBuilder(MovieExpert.class)
                .chatModel(chatModel)
                .outputKey("movies")
                .build();
        //5.通过parallelBuilder创建顺序workflow
        ExecutorService executorService = Executors.newFixedThreadPool(2); // 设置线程池
        UntypedAgent planner = AgenticServices
                .parallelBuilder()
                .subAgents(foodExpert, movieExpert)
                // 设置线程池
                .executor(executorService)
                // 指定输出值的name
                .outputKey("plans")
                // 指定输出的值
                .output(agenticScope -> {
                    List<String> movies = agenticScope.readState("movies", List.of());
                    List<String> meals = agenticScope.readState("meals", List.of());

                    List<EveningPlan> moviesAndMeals = new ArrayList<>();
                    for (int i = 0; i < movies.size(); i++) {
                        if (i >= meals.size()) {
                            break;
                        }
                        moviesAndMeals.add(new EveningPlan(movies.get(i), meals.get(i)));
                    }
                    return moviesAndMeals;
                })
                .build();
        //6.调用workflow
        Map<String, Object> input = Map.of(
                "mood", "浪漫"
        );
        List<EveningPlan> plans = (List<EveningPlan>)planner.invoke(input);
        System.out.println(plans);
        executorService.close();
    }

}
