package com.langchain.lesson13;

import com.arize.instrumentation.langchain4j.LangChain4jInstrumentor;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;

import java.util.List;

public class ArizePhoenixChatModelTest {

    public static void main(String[] args) throws InterruptedException {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.创建OtlpGrpcSpanExporter，连接ArizePhoenix
        OtlpGrpcSpanExporter otlpExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint("http://localhost:4317")  // ArizePhoenix默认gRPC端口
                .build();
        // 3.创建Trace类型的Provider
        SdkTracerProvider sdkTracerProvider =SdkTracerProvider.builder().addSpanProcessor(BatchSpanProcessor.builder(otlpExporter).build()).build();
        // 4.创建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                // 通过LangChain4jInstrumentor创建监听器，将监听器加入到ChatModel中
                .listeners(List.of(LangChain4jInstrumentor.instrument(sdkTracerProvider).createModelListener()))
                .build();
        // 5.调用模型
        String response = model.chat("你是谁？");
        System.out.println(response);
        // 这里让线程睡眠是因为数据上报到ArizePhoenix是一个异步过程，如果主线程结束，可能还没有上报，因此为了演示效果，睡眠10秒钟
        Thread.sleep(10000);
    }
}
