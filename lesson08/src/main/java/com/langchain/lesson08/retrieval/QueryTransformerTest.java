package com.langchain.lesson08.retrieval;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.transformer.ExpandingQueryTransformer;
import dev.langchain4j.rag.query.transformer.QueryTransformer;

import java.util.Collection;

public class QueryTransformerTest {
    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. 扩展query
        Query query = new Query("ChatGLM是哪个公司的大模型？");
        QueryTransformer queryTransformer = new ExpandingQueryTransformer(model);
        Collection<Query> queryList = queryTransformer.transform(query);
        System.out.println("=========扩展结果===========");
        for(Query q: queryList){
            System.out.println(q.text());
        }
    }
}
