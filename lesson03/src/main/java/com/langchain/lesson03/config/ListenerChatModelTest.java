package com.langchain.lesson03.config;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.ChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatRequestParameters;
import dev.langchain4j.model.openai.OpenAiChatResponseMetadata;
import dev.langchain4j.model.openai.OpenAiTokenUsage;
import dev.langchain4j.model.output.TokenUsage;

import java.util.List;
import java.util.Map;

public class ListenerChatModelTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("ZHIPU_API_KEY");
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey) // API KEY 访问大模型的认证密钥
                .baseUrl("https://open.bigmodel.cn/api/paas/v4") // 设置模型访问URL，默认是openAI的地址
                .modelName("glm-4-flash-250414") // 设置模型名称
                .listeners(List.of(getListener()))
                .build();
        String response = model.chat("直接给我推荐一本书？");
        System.out.println(response);
    }

    private static ChatModelListener getListener(){
        return new ChatModelListener() {

            /**
             * 在请求之前监听的方法，可以在这里查看大模型参数情况，也可以设置新的参数
             */
            @Override
            public void onRequest(ChatModelRequestContext requestContext) {
                System.out.println("==============onRequest start=========================");
                // 获取请求Request
                ChatRequest chatRequest = requestContext.chatRequest();

                // 获得请求的message
                List<ChatMessage> messages = chatRequest.messages();
                System.out.println(messages);

                // 获取请求的各种参数
                ChatRequestParameters parameters = chatRequest.parameters();
                System.out.println(parameters.modelName());
                System.out.println(parameters.temperature());
                System.out.println(parameters.topP());
                System.out.println(parameters.topK());
                System.out.println(parameters.frequencyPenalty());
                System.out.println(parameters.presencePenalty());
                System.out.println(parameters.maxOutputTokens());
                System.out.println(parameters.stopSequences());
                System.out.println(parameters.toolSpecifications());
                System.out.println(parameters.toolChoice());
                System.out.println(parameters.responseFormat());

                if (parameters instanceof OpenAiChatRequestParameters openAiParameters) {
                    System.out.println(openAiParameters.maxCompletionTokens());
                    System.out.println(openAiParameters.logitBias());
                    System.out.println(openAiParameters.parallelToolCalls());
                    System.out.println(openAiParameters.seed());
                    System.out.println(openAiParameters.user());
                    System.out.println(openAiParameters.store());
                    System.out.println(openAiParameters.metadata());
                    System.out.println(openAiParameters.serviceTier());
                    System.out.println(openAiParameters.reasoningEffort());
                }

                // 获得模型的供应商
                System.out.println(requestContext.modelProvider());

                // 设置自己的参数
                Map<Object, Object> attributes = requestContext.attributes();
                attributes.put("my-attribute", "my-value");
                System.out.println("==============onRequest end=========================");
            }

            /**
             * 在请求之后监听的方法，可以在这里查看本次调用结果，包括返回的消息、消耗token数目、以及之前在请求前自己设置的参数等
             */
            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                System.out.println("==============onResponse start=========================");
                // 获取ChatResponse
                ChatResponse chatResponse = responseContext.chatResponse();

                // 获得返回的message
                AiMessage aiMessage = chatResponse.aiMessage();
                System.out.println(aiMessage);

                // 获得本次调用的元数据，模型、会话id等
                ChatResponseMetadata metadata = chatResponse.metadata();
                System.out.println(metadata.id());
                System.out.println(metadata.modelName());
                System.out.println(metadata.finishReason());

                if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
                    System.out.println(openAiMetadata.created());
                    System.out.println(openAiMetadata.serviceTier());
                    System.out.println(openAiMetadata.systemFingerprint());
                }

                // 获取本次消耗的token
                TokenUsage tokenUsage = metadata.tokenUsage();
                System.out.println(tokenUsage.inputTokenCount()); // 输入token
                System.out.println(tokenUsage.outputTokenCount()); // 输出token
                System.out.println(tokenUsage.totalTokenCount()); // 总token数目
                if (tokenUsage instanceof OpenAiTokenUsage openAiTokenUsage) {
                    //这里注释掉，是因为我们使用Zhipu的模型，没有返回这两个数据
//                    System.out.println(openAiTokenUsage.inputTokensDetails().cachedTokens());
//                    System.out.println(openAiTokenUsage.outputTokensDetails().reasoningTokens());
                }

                ChatRequest chatRequest = responseContext.chatRequest();
                System.out.println(chatRequest);

                // 模型供应商
                System.out.println(responseContext.modelProvider());

                // 之前在调用之前自己设置的参数
                Map<Object, Object> attributes = responseContext.attributes();
                System.out.println(attributes.get("my-attribute"));
                System.out.println("==============onResponse end=========================");
            }

            /**
             * 在请求错误监听的方法，可以获取错误信息、请求的ChatRequest、之前在请求之前自己设置的参数等
             */
            @Override
            public void onError(ChatModelErrorContext errorContext) {
                System.out.println("==============onError start=========================");
                // 错误信息
                Throwable error = errorContext.error();
                error.printStackTrace();

                // 请求ChatRequest
                ChatRequest chatRequest = errorContext.chatRequest();
                System.out.println(chatRequest);

                // 模型提供商
                System.out.println(errorContext.modelProvider());

                // 之前在调用之前自己设置的参数
                Map<Object, Object> attributes = errorContext.attributes();
                System.out.println(attributes.get("my-attribute"));
                System.out.println("==============onError end=========================");
            }
        };
    }
}
