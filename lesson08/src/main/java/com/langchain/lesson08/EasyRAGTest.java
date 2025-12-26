package com.langchain.lesson08;

import dev.langchain4j.data.document.DefaultDocument;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;

public class EasyRAGTest {

    public static void main(String[] args) {

        // 1. 初始化文档
        List<Document> documents = List.of(new DefaultDocument("ChatGLM3 是北京智谱华章科技有限公司和清华大学 KEG 实验室联合发布的对话预训练模型。ChatGLM3-6B 是 ChatGLM3 系列中的开源模型，在保留了前两代模型对话流畅、部署门槛低等众多优秀特性的基础上，ChatGLM3-6B 引入了如下特性：\n" +
                "\n" +
                "更强大的基础模型： ChatGLM3-6B 的基础模型 ChatGLM3-6B-Base 采用了更多样的训练数据、更充分的训练步数和更合理的训练策略。在语义、数学、推理、代码、知识等不同角度的数据集上测评显示，* ChatGLM3-6B-Base 具有在 10B 以下的基础模型中最强的性能*。\n" +
                "更完整的功能支持： ChatGLM3-6B 采用了全新设计的 Prompt 格式 ，除正常的多轮对话外。同时原生支持工具调用（Function Call）、代码执行（Code Interpreter）和 Agent 任务等复杂场景。\n" +
                "更全面的开源序列： 除了对话模型 ChatGLM3-6B 外，还开源了基础模型 ChatGLM3-6B-Base 、长文本对话模型 ChatGLM3-6B-32K 和进一步强化了对于长文本理解能力的 ChatGLM3-6B-128K。以上所有权重对学术研究完全开放 ，在填写 问卷 进行登记后亦允许免费商业使用。\n" +
                "\n" +
                "北京智谱华章科技有限公司是一家来自中国的公司，致力于打造新一代认知智能大模型，专注于做大模型创新。"));
        // 2. 将文档切分，并向量化后存入向量数据库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        EmbeddingStoreIngestor.ingest(documents, embeddingStore);
        // 3. 获取API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 4. 构建模型
        ChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .baseUrl("https://open.bigmodel.cn/api/paas/v4")
                .modelName("glm-4-flash-250414")
                .build();
        // 5. 通过AiServices，将向量数据库提供给AiServices
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.from(embeddingStore))
                .build();
        // 6. 访问
        String answer = assistant.chat("ChatGLM3是哪个公司的大模型。");
        System.out.println(answer);
    }
}
