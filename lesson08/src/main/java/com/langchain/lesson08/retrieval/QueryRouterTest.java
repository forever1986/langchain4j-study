package com.langchain.lesson08.retrieval;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static dev.langchain4j.spi.ServiceHelper.loadFactories;

public class QueryRouterTest {

    public static void main(String[] args) {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3. 构建查询器
        // 3.1 查询器1：基于Tavily的网络搜索
        ContentRetriever contentRetriever1 = new WebSearchContentRetriever(
                TavilyWebSearchEngine.builder()
                        .apiKey(System.getenv("TAVILY_API_KEY"))
                        .build(), 3);
        // 3.2 查询器2：基于Embeding存储的搜索
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingModel embeddingModel = loadEmbeddingModel();
        String content1 = """
                ChatGLM3是智谱公司和清华大学一起发布的大语言模型。
                """;
        embeddingStore.add(embeddingModel.embed(content1).content(), new TextSegment(content1, new Metadata()));
        String content2 = """
                ChatGLM3 是由清华大学技术成果转化企业智谱AI研发的支持中英双语的对话机器人，基于千亿参数基座模型GLM架构开发。该模型通过多阶段训练流程形成通用对话能力，具备问答交互、代码生成、创意写作等功能，其开源版本ChatGLM-6B自2023年3月启动内测以来已形成广泛影响力。\n
                截至2024年3月，智谱AI通过该技术实现了2000多家生态合作伙伴的应用落地，并在多模态技术上持续突破，推出了视频生成等创新功能。清华大学是中国排名top2的学校。
                """;
        embeddingStore.add(embeddingModel.embed(content2).content(), new TextSegment(content2, new Metadata()));
        ContentRetriever contentRetriever2 = new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel);
        Map<ContentRetriever, String> map = new HashMap<>();
        map.put(contentRetriever1, "它是一个基于互联网搜索的查询，具有实时性的数据可以根据它来检索");
        map.put(contentRetriever2, "它是一个基于嵌入数据库的查询，关于智谱公司的内容可以根据它来检索");

        // 4. 构建检索路由
        QueryRouter queryRouter = new LanguageModelQueryRouter(model, map);

        // 5. 测试结果
        System.out.println("=======第一次查询===========");
        Query query = new Query("智谱公司研发的大模型是哪个？");
        Collection<ContentRetriever> contentRetrievers = queryRouter.route(query);
        System.out.println("使用的检索器" + contentRetrievers);
        System.out.println("本次检索结果" + contentRetrievers.iterator().next().retrieve(query));
        System.out.println("=======第二次查询===========");
        query = new Query("现在是哪年月？");
        contentRetrievers = queryRouter.route(query);
        System.out.println("使用的检索器" + contentRetrievers);
        System.out.println("本次检索结果" + contentRetrievers.iterator().next().retrieve(query));
    }

    private static EmbeddingModel loadEmbeddingModel() {
        Collection<EmbeddingModelFactory> factories = loadFactories(EmbeddingModelFactory.class);
        if (factories.size() > 1) {
            throw new RuntimeException("Conflict: multiple embedding models have been found in the classpath. "
                    + "Please explicitly specify the one you wish to use.");
        }

        for (EmbeddingModelFactory factory : factories) {
            EmbeddingModel embeddingModel = factory.create();
            return embeddingModel;
        }

        return null;
    }
}
