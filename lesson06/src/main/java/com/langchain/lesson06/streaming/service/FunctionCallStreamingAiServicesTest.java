package com.langchain.lesson06.streaming.service;

import com.langchain.lesson06.tools.TimeTool;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import java.util.concurrent.CompletableFuture;

public class FunctionCallStreamingAiServicesTest {

    public static void main(String[] args) throws InterruptedException {
        // 1. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建模型
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        StreamAssistant streamAssistant = AiServices.builder(StreamAssistant.class)
                .streamingChatModel(model)
                .tools(new TimeTool())
                .build();
        // 3. 访问大模型
        TokenStream tokenStream = streamAssistant.chat("请问洛杉矶现在几点？");
        // 4.异步输出处理
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>();
        tokenStream
//                .onPartialResponse(System.out::println) //用于每一段的返回
//                .onPartialThinking(System.out::println) //用于大模型COT的思考过程
//                .onRetrieved(System.out::println) //用于RAG的检索
//                .onIntermediateResponse(System.out::println)
                .beforeToolExecution(System.out::println) //用于工具调用之前
                .onToolExecuted(System.out::println)//用于工具调用
                .onCompleteResponse((ChatResponse response) -> {
                    System.out.println(response.aiMessage().text());
                    futureResponse.complete(response);
                })
                .onError(futureResponse::completeExceptionally)
                .start();
        futureResponse.join();
    }
}
