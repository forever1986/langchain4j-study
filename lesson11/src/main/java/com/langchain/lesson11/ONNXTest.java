package com.langchain.lesson11;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.onnx.OnnxEmbeddingModel;
import dev.langchain4j.model.embedding.onnx.PoolingMode;
import dev.langchain4j.model.output.Response;

import java.io.File;
import java.util.Arrays;

public class ONNXTest {

    public static void main(String[] args) {
        OnnxEmbeddingModel embeddingModel = new OnnxEmbeddingModel(
                new File(ClassLoader.getSystemResource("bge-small-zh/model.onnx").getPath()).toPath(),
                new File(ClassLoader.getSystemResource("bge-small-zh/tokenizer.json").getPath()).toPath(),
                PoolingMode.CLS);
        Response<Embedding> embeddingResponse =  embeddingModel.embed("测试进行嵌入");
        System.out.println("维度= "+embeddingResponse.content().dimension());
        System.out.println(Arrays.toString(embeddingResponse.content().vector()));
    }
}
