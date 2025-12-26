package com.langchain.lesson16.observability;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CreativeWriter {

    @UserMessage("""
            你是一位富有创造力的作家。
            围绕给定的主题生成一段不超过 3 句话的故事草稿。
            只返回故事内容，不要其他任何信息。
            主题为 {{topic}} 。
            """)
    @Agent("根据给定的主题生成一个故事")
    String generateStory(@V("topic") String topic);

}
