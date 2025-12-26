package com.langchain.lesson08.retrieval;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.DefaultContent;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.query.Query;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentAggregatorTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("JINA_API_KEY");
        // 2. 用户的却容易
        UserMessage chatMessage = new UserMessage("ChatGLM3有哪些模型？");
        // 3. 模拟查询得到的数据
        Map<Query, Collection<List<Content>>> queryToContents = new HashMap<>();
        String str1 = """
                ChatGLM3 是北京智谱华章科技有限公司和清华大学 KEG 实验室联合发布的对话预训练模型。
                """;
        String str2 = """
                ChatGLM3-6B 的基础模型 ChatGLM3-6B-Base 采用了更多样的训练数据、更充分的训练步数和更合理的训练策略。在语义、数学、推理、代码、知识等不同角度的数据集上测评显示，* ChatGLM3-6B-Base 具有在 10B 以下的基础模型中最强的性能*。
                """;
        String str3 = """
                ChatGLM3-6B 采用了全新设计的 Prompt 格式 ，除正常的多轮对话外。同时原生支持工具调用（Function Call）、代码执行（Code Interpreter）和 Agent 任务等复杂场景。
                """;
        String str4 = """
                除了对话模型 ChatGLM3-6B 外，还开源了基础模型 ChatGLM3-6B-Base 、长文本对话模型 ChatGLM3-6B-32K 和进一步强化了对于长文本理解能力的 ChatGLM3-6B-128K。以上所有权重对学术研究完全开放 ，在填写 问卷 进行登记后亦允许免费商业使用
                """;
        queryToContents.put(new Query(chatMessage.singleText()), List.of(List.of(
                new DefaultContent(str1), new DefaultContent(str2), new DefaultContent(str3), new DefaultContent(str4))));
        // 4.调用ReRankingContentAggregator进行重新排序
        ReRankingContentAggregator aggregator = new ReRankingContentAggregator(JinaScoringModel.builder().apiKey(apiKey).modelName("jina-reranker-v3").build());
        // 5. 输出结果
        List<Content> results = aggregator.aggregate(queryToContents);
        System.out.println("========重排结果=========");
        for(Content content : results){
            System.out.println(content);
        }
        ContentInjector contentInjector = DefaultContentInjector.builder().build();
        ChatMessage newChatMessage = contentInjector.inject(results, chatMessage);
        System.out.println("========组装最后的query=========");
        System.out.println(((UserMessage)newChatMessage).singleText());
    }

}
