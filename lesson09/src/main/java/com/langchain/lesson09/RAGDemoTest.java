package com.langchain.lesson09;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.jina.JinaScoringModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.rag.query.router.LanguageModelQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.rag.query.transformer.ExpandingQueryTransformer;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.web.search.tavily.TavilyWebSearchEngine;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.spi.ServiceHelper.loadFactories;

public class RAGDemoTest {

    public static void main(String[] args) {

        // 1. 加载文档
        URL resource = ClassLoader.getSystemResource("zhipu.txt");
        File file = new File(resource.getPath());
        System.out.println(file.getPath());
        Document document = FileSystemDocumentLoader.loadDocument(file.toPath(), new TextDocumentParser());

        // 2. 定义文档存储器
        ChromaEmbeddingStore embeddingStore = ChromaEmbeddingStore.builder()
                .apiVersion(ChromaApiVersion.V1)
                .baseUrl("http://localhost:8000")
                .tenantName("rag_test")
                .databaseName("langchain_test")
                .collectionName("collection_test")
                .build();

        // 3. 将文档切分，并向量化后存入向量数据库
        DocumentByLineSplitter documentByLineSplitter = new DocumentByLineSplitter(200,20);
        EmbeddingStoreIngestor.builder()
                .documentSplitter(documentByLineSplitter)
                .embeddingStore(embeddingStore)
                .build()
                .ingest(List.of(document));

        // 4. 构建模型
        String modelApiKey = System.getenv("ZHIPU_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(modelApiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();

        // 5. 定义RetrievalAugmentor
        // 5.1 定义检索器，分别定义网络检索器和向量数据库检索器
        ContentRetriever contentRetriever1 = new WebSearchContentRetriever(
                TavilyWebSearchEngine.builder()
                        .apiKey(System.getenv("TAVILY_API_KEY"))
                        .build(), 3);
        ContentRetriever contentRetriever2 = new EmbeddingStoreContentRetriever(embeddingStore, loadEmbeddingModel());
        Map<ContentRetriever, String> map = new HashMap<>();
        map.put(contentRetriever1, "它是一个基于互联网搜索的查询，具有实时性的数据可以根据它来检索（比如查询时间、日期、新闻等）");
        map.put(contentRetriever2, "它是一个基于存储智谱公司信息的查询，关于智谱公司的内容可以根据它来检索");
        // 5.2 定义QueryRouter，通过大模型决定使用哪个检索器
        QueryRouter queryRouter = new LanguageModelQueryRouter(model, map);
        // 5.3 定义重排器
        String jinaApiKey = System.getenv("JINA_API_KEY");
        ReRankingContentAggregator aggregator = ReRankingContentAggregator.builder()
                .scoringModel(JinaScoringModel.builder()
                        .apiKey(jinaApiKey)
                        .modelName("jina-reranker-v3")
                        .build())
                // 由于JINA重排模型只支持一个query，因此设置了querySelector，永远返回第一条记录
                .querySelector((queryToContents) -> queryToContents.keySet().iterator().next())
                .minScore(0.2)
                .build();
        // 5.4 最后组装RetrievalAugmentor
        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .queryTransformer(new ExpandingQueryTransformer(model))
                .queryRouter(queryRouter)
                .contentAggregator(aggregator)
                .build();

        // 6.定义AiServices，将RetrievalAugmentor传入
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        // 7. 测试结果
        System.out.println("=======第一次查询===========");
        String result = assistant.chat("智谱公司2023年3月发生了什么事情？");
        System.out.println(result);
        System.out.println("=======第二次查询===========");
        result = assistant.chat("今天是几号？");
        System.out.println(result);
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
