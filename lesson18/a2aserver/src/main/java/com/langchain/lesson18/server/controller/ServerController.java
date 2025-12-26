package com.langchain.lesson18.server.controller;

import com.langchain.lesson18.server.agent.CreativeWriter;
import io.a2a.spec.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class ServerController {

    @Autowired
    private CreativeWriter creativeWriter;

    /**
     * 该方法就是对你的Agent Server提供的能力的描述
     */
    @GetMapping("/.well-known/agent-card.json")
    public AgentCard agentCard() {
        return new AgentCard.Builder()
                // agent的名称
                .name("富有创造力作家的Agent")
                // Agent的描述
                .description("""
                    你是一位富有创造力的作家。
                    围绕给定的主题生成一段不超过 3 句话的故事草稿。
                    只返回故事内容，不要其他任何信息。
                    主题为 {{topic}} 。
                """)
                // URL是与Agent进行交互的基 URL 端点，这里设置a2a是因为我们下面的url地址是这个
                .url("http://localhost:8080/a2a")
                // Agent服务的提供方的相关信息
                .provider(new AgentProvider(
                        "langchain lesson",
                        "http://localhost:8080/"
                ))
                // 指该Agent或其 API 的版本标识符。
                .version("1.0.0")
                // 该Agent所支持的各种能力
                .capabilities(new AgentCapabilities.Builder()
                        .streaming(false) // 是否支持Streaming with Server-Sent Events (SSE)
                        .pushNotifications(false) // 是否支持Push Notifications
                        .stateTransitionHistory(false) //是否支持“state转换历史记录”，表明该agent是否支持提供state转换历史信息
                        .build())
                // 默认输入格式
                .defaultInputModes(Collections.singletonList("text"))
                // 默认输出格式
                .defaultOutputModes(Collections.singletonList("text"))
                // 该Agent所提供的具体skill列表。（skill是Anthropic的Claude模型提供一种使用大模型能力的范式，有兴趣的朋友可以去了解一下）
                .skills(Collections.emptyList())
                .build();
    }


    /**
     * 这里只提供一个非streaming方式的访问接口，也就是Request/Response (Polling)方式
     */
    @PostMapping(
            path = "/a2a",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public JSONRPCResponse handleJsonRpcRequest(@RequestBody NonStreamingJSONRPCRequest request) {

        // 1. 获取client传过来的信息
        Message message = ((MessageSendParams)request.getParams()).message();
        TextPart textPart = (TextPart)message.getParts().get(message.getParts().size()-1);

        // 2. 调用agent回复内容
        String result = creativeWriter.generateStory(textPart.getText());
        System.out.println(result);

        // 3.返回结果信息
        return new SendMessageResponse(
                "2.0",
                request.getId(),
                new Task.Builder()
                        .id(request.getId().toString())
                        .contextId("contextId")
                        .status(new TaskStatus(TaskState.COMPLETED))
//                        .history(resultMessage) // 历史结果，这里就不做演示，可以将历次聊天加入历史中，这样实现多轮聊天
                        .artifacts(Collections.singletonList(
                                new Artifact.Builder()
                                        .artifactId("artifactId")
                                        .name("name")
                                        .description("description")
                                        .parts(new TextPart(result)) // 将结果放入artifacts返回
                                        .build()
                        ))
                        .build(),
                null
        );
    }

}
