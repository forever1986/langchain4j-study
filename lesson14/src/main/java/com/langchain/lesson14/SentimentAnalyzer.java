package com.langchain.lesson14;

import dev.langchain4j.service.UserMessage;

public interface SentimentAnalyzer {

    @UserMessage("分析“{{it}}”的情感倾向")
    Sentiment analyzeSentimentOf(String text);

    @UserMessage("{{it}} 是否带有积极的情感色彩？")
    boolean isPositive(String text);

}
