package com.langchain.lesson10;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.output.Response;

import java.util.List;

public class ScoringRerankingModelsTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("JINA_API_KEY");
        // 2. 模拟查询得到的数据
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
        // 3. 构建模型
        JinaScoringModel jinaScoringModel = JinaScoringModel.builder()
                .apiKey(apiKey)
                .modelName("jina-reranker-v3")
                .build();
        // 4. 访问并输出结果
        Response<List<Double>> response = jinaScoringModel.scoreAll(List.of(new TextSegment(str1, new Metadata())
                        , new TextSegment(str2, new Metadata())
                        , new TextSegment(str3, new Metadata())
                        , new TextSegment(str4, new Metadata()))
                ,"ChatGLM3是哪个家公司开发的模型？");
        System.out.println(response);

    }

}
