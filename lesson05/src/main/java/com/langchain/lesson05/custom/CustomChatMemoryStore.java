package com.langchain.lesson05.custom;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import redis.clients.jedis.Jedis;

import java.util.List;

public class CustomChatMemoryStore implements ChatMemoryStore {

    private static final String REDIS_KEY_PREFIX = "chatmemory:";

    private Jedis jedis;

    public CustomChatMemoryStore(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = this.jedis.get(REDIS_KEY_PREFIX+memoryId);
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = ChatMessageSerializer.messagesToJson(messages);
        this.jedis.set(REDIS_KEY_PREFIX+memoryId, json);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        this.jedis.del(REDIS_KEY_PREFIX+memoryId);
    }
}
