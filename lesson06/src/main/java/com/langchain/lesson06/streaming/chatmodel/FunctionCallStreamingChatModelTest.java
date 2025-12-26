package com.langchain.lesson06.streaming.chatmodel;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langchain.lesson06.tools.ToolUtils;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.CompleteToolCall;
import dev.langchain4j.model.chat.response.PartialToolCall;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FunctionCallStreamingChatModelTest {

    public static void main(String[] args) throws InterruptedException {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2.定义工具
        ToolSpecification toolSpecification = ToolSpecification.builder()
                .name("getLocalTime")
                .description("根据传入国家或地区的名字，获取所在国家或地区时区的当前日期和时间")
                .parameters(JsonObjectSchema.builder()
                        .addStringProperty("zone", "国家或地区的名字")
                        .required("zone") // 必填的属性应明确加以说明
                        .build())
                .build();
        // 3.定义请求消息，将工具的定义放到请求消息中
        UserMessage userMessage = UserMessage.from("请问洛杉矶现在几点？");
        ChatRequest request = ChatRequest.builder()
                .messages(userMessage)
                .toolSpecifications(toolSpecification)
                .build();
        // 4.创建模型
        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 5.调用模型
        CompletableFuture<ChatResponse> futureResponse = new CompletableFuture<>(); // 异步结果处理
        model.chat(request, new StreamingChatResponseHandler(){

            @Override
            public void onPartialToolCall(PartialToolCall partialToolCall) {
                System.out.println("result:"+partialToolCall);
            }

            @Override
            public void onCompleteToolCall(CompleteToolCall completeToolCall) {
                StreamingChatResponseHandler.super.onCompleteToolCall(completeToolCall);
            }

            @Override
            public void onCompleteResponse(ChatResponse completeResponse) {
                // 6.结果处理，通过hasToolExecutionRequests判断模型是否要调用工具
                if(completeResponse.aiMessage().hasToolExecutionRequests()){
                    // 6.1 需要调用工具-这里做一个比较简单的处理
                    // 获取大模型回复结果中的ToolExecutionRequest（其实就是工具的请求）
                    ToolExecutionRequest toolExecutionRequest = completeResponse.aiMessage().toolExecutionRequests().get(0);
                    System.out.println(toolExecutionRequest.arguments());
                    // 解析大模型给出的调用工具的入参，这里因为只有一个工具，所以不做太复杂的处理，实际复杂过程后面将AiServices的源码再说明
                    String zoneId ="";
                    try {
                        JsonNode jsonNode = new ObjectMapper().readTree(toolExecutionRequest.arguments());
                        zoneId = jsonNode.get("zone").textValue();
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    // 从大模型得到的参数，手动调用工具
                    String functionCallResult = ToolUtils.getCurrentDateTime(zoneId);
                    // 将结果以及之前的message一同给大模型，再次得到最新结果。
                    ToolExecutionResultMessage resultMessage = ToolExecutionResultMessage.from(toolExecutionRequest, functionCallResult);
                    ChatRequest newChatRequest = ChatRequest.builder()
                            .messages(Arrays.asList(userMessage,completeResponse.aiMessage(),resultMessage))
                            .build();
                    // 输出最终结果
                    model.chat(newChatRequest, new StreamingChatResponseHandler() {
                        @Override
                        public void onCompleteResponse(ChatResponse completeResponse) {
                            // 最终输出
                            System.out.println(completeResponse.aiMessage().text());
                            futureResponse.complete(completeResponse);
                        }

                        @Override
                        public void onError(Throwable error) {
                            futureResponse.completeExceptionally(error);
                        }
                    });
                }else {
                    //6.2 不需要调用工具，则直接输出
                    System.out.println(completeResponse.aiMessage());
                    futureResponse.complete(completeResponse);
                }
            }

            @Override
            public void onError(Throwable error) {
                futureResponse.completeExceptionally(error);
            }
        });
        futureResponse.join();
    }
}
