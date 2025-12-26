package com.langchain.lesson05.custom;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class CustomChatMemory implements ChatMemory {

    private final Object id;
    private final ChatMemoryStore store;
    private final ChatModel chatModel;

    public CustomChatMemory(Object id, ChatMemoryStore store, ChatModel chatModel) {
        this.id = id;
        this.store = store;
        this.chatModel = chatModel;
    }

    @Override
    public Object id() {
        return id;
    }

    @Override
    public void add(ChatMessage message) {
        // 从数据库里面将上一次摘要拿出来
        List<ChatMessage> messages = messages();
        if(message instanceof SystemMessage){
            // 如果是SystemMessage，则替换List中的SystemMessage
            Optional<SystemMessage> systemMessage = SystemMessage.findFirst(messages);
            if (systemMessage.isPresent()) {
                if (systemMessage.get().equals(message)) {
                    return; // do not add the same system message
                } else {
                    messages.remove(systemMessage.get()); // need to replace existing system message
                }
            }
            messages.add(message);
        }else if (message instanceof UserMessage){
            // 如果是用户提问，直接加入List
            messages.add(message);
        }else if (message instanceof AiMessage){
            // 如果是大模型回答，则整理为摘要
            Optional<SystemMessage> systemMessage = SystemMessage.findFirst(messages);
            StringBuilder textBuilder = new StringBuilder();
            for(ChatMessage chatMessage: messages){
                if(chatMessage instanceof UserMessage){
                    textBuilder.append(((UserMessage)chatMessage).singleText()).append("\n");
                }else if(chatMessage instanceof AiMessage){
                    textBuilder.append(((AiMessage)chatMessage).text()).append("\n");
                }
            }
            textBuilder.append(((AiMessage)message).text());
            String text = textBuilder.toString();
            // 这里使用简单方式让大模型生成一个摘要
            String response = chatModel.chat("""
                            以下是之前你和用户的对话内容总结
                            =========历史记录开始=============
                            """+ text + """
                            =========历史记录结束=============
                            请重新总结一个对话摘要，以便下次作为聊天记录可供参考。
                            =========恢复格式开始=============
                            这是之前的聊天记录
                            用户：<摘要问题，保持在10个字>
                            大模型：<摘要回答，保持在20个字以内>
                            用户：<摘要问题，保持在10个字>
                            大模型：<摘要回答，保持在20个字以内>
                            ... ...
                            =========恢复格式结束=============
                            按照之前的回复记录，不做捏造
                            """);
            // 重新设置聊天记忆
            messages = new ArrayList<>();
            if (systemMessage.isPresent()) {
                messages.add(systemMessage.get());
            }
            AiMessage aiMessage = AiMessage.from(response);
            messages.add(aiMessage);
        }
        store.updateMessages(id, messages);
    }

    @Override
    public List<ChatMessage> messages() {
        return new LinkedList<>(store.getMessages(id));
    }

    @Override
    public void clear() {
        store.deleteMessages(id);
    }
}
