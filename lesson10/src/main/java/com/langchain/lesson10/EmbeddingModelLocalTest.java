package com.langchain.lesson10;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.onnx.bgesmallzhv15.BgeSmallZhV15EmbeddingModel;
import dev.langchain4j.model.output.Response;

import java.util.Arrays;

public class EmbeddingModelLocalTest {

    public static void main(String[] args) {
        BgeSmallZhV15EmbeddingModel embeddingModel = new BgeSmallZhV15EmbeddingModel();
        Response<Embedding> embeddingResponse =  embeddingModel.embed("测试进行嵌入");
        System.out.println("维度= "+embeddingResponse.content().dimension());
        System.out.println(Arrays.toString(embeddingResponse.content().vector()));
    }
}
