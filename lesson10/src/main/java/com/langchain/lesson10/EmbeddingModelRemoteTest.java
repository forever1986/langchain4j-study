package com.langchain.lesson10;

import dev.langchain4j.community.model.zhipu.ZhipuAiEmbeddingModel;
import dev.langchain4j.community.model.zhipu.embedding.EmbeddingModel;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.output.Response;

import java.util.Arrays;

public class EmbeddingModelRemoteTest {

    public static void main(String[] args) {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.构建embeddingModel
        ZhipuAiEmbeddingModel embeddingModel = ZhipuAiEmbeddingModel.builder()
                .apiKey(apiKey)
                .model(EmbeddingModel.EMBEDDING_2)
                .build();
        // 3. 测试
        Response<Embedding> embeddingResponse =  embeddingModel.embed("测试进行嵌入");
        System.out.println("维度= "+embeddingResponse.content().dimension());
        System.out.println(Arrays.toString(embeddingResponse.content().vector()));
    }
}
