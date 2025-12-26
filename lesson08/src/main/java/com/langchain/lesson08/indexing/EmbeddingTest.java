package com.langchain.lesson08.indexing;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.spi.model.embedding.EmbeddingModelFactory;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;

import java.io.File;
import java.net.URL;
import java.util.Collection;

import static dev.langchain4j.spi.ServiceHelper.loadFactories;

public class EmbeddingTest {
    public static void main(String[] args) {
        // 1. 读取文档：指定TextDocumentParser来格式化
        URL resource = ClassLoader.getSystemResource("splitter.txt");
        File file = new File(resource.getPath());
        Document document = FileSystemDocumentLoader.loadDocument(file.toPath(), new TextDocumentParser());
        // 2. 定义文档切分器
        DocumentByLineSplitter documentByLineSplitter = new DocumentByLineSplitter(200,20);
        // 3. 定义文档存储器
        ChromaEmbeddingStore store = ChromaEmbeddingStore.builder()
                .apiVersion(ChromaApiVersion.V1)
                .baseUrl("http://localhost:8000")
                .tenantName("test")
                .databaseName("langchain_test")
                .collectionName("demo")
                .build();
        // 4. 存入文档到chroma数据库
        EmbeddingStoreIngestor.builder()
                .embeddingStore(store)
                .documentSplitter(documentByLineSplitter)
                .build()
                .ingest(document);
        // 5. 查询文档
        EmbeddingModel embeddingModel = loadEmbeddingModel();
        Response<Embedding> res =  embeddingModel.embed("Once your Documents are loaded, it is time");
        EmbeddingSearchRequest request = new EmbeddingSearchRequest(res.content(),10,0.8, null);
        EmbeddingSearchResult<TextSegment> result =  store.search(request);
        for(EmbeddingMatch embeddingMatch : result.matches()){
            System.out.println("========找到一条符合要求========");
            System.out.println(embeddingMatch);
        }

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

