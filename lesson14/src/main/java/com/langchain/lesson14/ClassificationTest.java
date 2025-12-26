package com.langchain.lesson14;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

public class ClassificationTest {

    public static void main(String[] args) {
        //1.获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        //2.加载大模型
        ChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        //3.定义AiServices
        SentimentAnalyzer sentimentAnalyzer = AiServices.create(SentimentAnalyzer.class, chatModel);

        //4.测试
        Sentiment sentiment = sentimentAnalyzer.analyzeSentimentOf("我喜欢这个产品！");
        System.out.println(sentiment); // Output: POSITIVE

        boolean positive = sentimentAnalyzer.isPositive("这是一个糟糕的体验。");
        System.out.println(positive); // Output: false
    }

}
