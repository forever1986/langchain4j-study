package com.langchain.lesson10;

import dev.langchain4j.community.model.dashscope.QwenLanguageModel;
import dev.langchain4j.community.model.dashscope.QwenModelName;
import dev.langchain4j.model.output.Response;

public class LanguageModelTest {
    public static void main(String[] args) {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("QWEN_API_KEY");
        // 2.创建模型
        QwenLanguageModel model = QwenLanguageModel.builder()
                .apiKey(apiKey)
                // 由于目前很多都没有开放基座模型，因此还是使用一个普通微调过的模型进行测试
                .modelName(QwenModelName.QWEN2_5_32B_INSTRUCT)
                .build();
        // 3.访问
        Response<String> response = model.generate("叫我打乒乓球？");
        System.out.println(response.content());
    }
}
