package com.langchain.lesson02.streaming;

import com.langchain.lesson02.streaming.service.FluxAssistant;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import reactor.core.publisher.Flux;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AiServicesStreamingFluxTest {

    public static void main(String[] args) throws InterruptedException {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        FluxAssistant assistant = AiServices.create(FluxAssistant.class, model);
        // 3.访问大模型
        Flux<String> response = assistant.chat("直接给我推荐一本书，只说出书名即可");
        CompletableFuture<String> futureResponse = new CompletableFuture<>();
        response.collect(Collectors.joining())
                .subscribe(str ->{
                    System.out.println(str);
                    futureResponse.complete(str);
                });
        futureResponse.join();
    }
}
