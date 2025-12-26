package com.langchain.lesson16.annotation.agent;

import com.langchain.lesson16.annotation.EveningPlan;
import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.declarative.ParallelExecutor;
import dev.langchain4j.service.V;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public interface EveningPlannerAgent {

    // 设置为Parallel workflow
    @ParallelAgent( outputKey = "plans",
            subAgents = { FoodExpert.class, MovieExpert.class })
    List<EveningPlan> plan(@V("mood") String mood);

    // 设置执行器
    @ParallelExecutor
    static Executor executor() {
        return Executors.newFixedThreadPool(2);
    }

    // 设置output
    @Output
    static List<EveningPlan> createPlans(@V("movies") List<String> movies, @V("meals") List<String> meals) {
        List<EveningPlan> moviesAndMeals = new ArrayList<>();
        for (int i = 0; i < movies.size(); i++) {
            if (i >= meals.size()) {
                break;
            }
            moviesAndMeals.add(new EveningPlan(movies.get(i), meals.get(i)));
        }
        return moviesAndMeals;
    }

}
