package com.langchain.lesson15.custom.planner;

import dev.langchain4j.agentic.planner.AgentInstance;

import java.util.List;
import java.util.Set;

public class GoalOrientedSearchGraph {

    private List<AgentInstance> subagents;

    public GoalOrientedSearchGraph(List<AgentInstance> subagents) {
        this.subagents = subagents;
    }

    public List<AgentInstance> search(Set<String> keys, String goal){
        /**
         * TODO: 这里直接返回subagents，是因为我们在CustomGoalOrientedWorkflowTest设置的顺序刚好是图所需的顺序
         * 在实际应用中需要一个真正的图搜索，并且可能整个workflow中有很多个agent，然后根据图数据库中的配置路径，最后搜索出一条规划路径
         * 这个规划路径可以是预先设定好的，也可以是让模型自己规划的。这样可以做到一定灵活度和一定的规范化
         */
        return subagents.stream().toList();
    }
}
