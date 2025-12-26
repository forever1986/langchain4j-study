package com.langchain.lesson01;

import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.concurrent.CompletableFuture;

public class StreamingChatModelTest {

    public static void main(String[] args) throws InterruptedException {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.创建模型：注意这里使用的是OpenAiStreamingChatModel，而非OpenAiChatModel
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 3.使用StreamingChatResponseHandler监听获取流式数据
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>(); // 异步结果处理
        model.chat("你是谁？", new StreamingChatResponseHandler() {

            // 过程中输出的内容
            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.println("onPartialResponse: " + partialResponse);
            }

            // 最后完成输出的内容
            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                System.out.println("onCompleteResponse: " + completeResponse);
                futureResponse.complete(completeResponse);
            }

            // 报错的内容
            @Override
            public void onError(Throwable error) {
                error.printStackTrace();
                futureResponse.completeExceptionally(error);
            }
        });
        futureResponse.join();
    }
}
