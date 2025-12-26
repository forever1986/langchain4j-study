package com.langchain.lesson10;

import dev.langchain4j.model.mistralai.MistralAiModerationModel;
import dev.langchain4j.model.moderation.Moderation;
import dev.langchain4j.model.output.Response;

public class ModerationModelTest {

    public static void main(String[] args) {
        String apiKey = System.getenv("MISTRAL_API_KEY");
        MistralAiModerationModel moderationModel = new MistralAiModerationModel.Builder()
                .apiKey(apiKey)
                .modelName("mistral-moderation-latest")
                .build();
        System.out.println("========测试一===========");
        // 说明一下，这个message只是为了测试效果，并不代表作者任何观点
        Response<Moderation> response = moderationModel.moderate("这个事情只有男的可以做，女的做不到！");
        System.out.println(response.content().flagged());
        System.out.println(response.content().flaggedText());
        System.out.println("========测试二===========");
        response = moderationModel.moderate("这件事情无论给谁都比较难办！");
        System.out.println(response.content().flagged());
        System.out.println(response.content().flaggedText());
    }

}
