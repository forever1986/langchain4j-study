//package com.langchain.lesson10;
//
//import dev.langchain4j.data.audio.Audio;
//import dev.langchain4j.data.message.AudioContent;
//import dev.langchain4j.data.message.TextContent;
//import dev.langchain4j.data.message.UserMessage;
//import dev.langchain4j.model.chat.response.ChatResponse;
//import dev.langchain4j.model.openai.OpenAiChatModel;
//
//import java.io.File;
//import java.net.URL;
//import java.nio.file.Files;
//import java.util.Base64;
//
//public class AudioModelTest {
//
//    public static void main(String[] args) throws Exception {
//        // glm-4-voice 语音生成
//        // glm-asr-2512 语音转文本
//
//        // 1.获取环境变量中的API KEY
//        String apiKey = System.getenv("ZHIPU_API_KEY");
//        // 2. 构建ImageModel
//        OpenAiChatModel audioModel = OpenAiChatModel.builder()
//                .apiKey(apiKey)
//                .modelName("glm-asr-2512")
//                .baseUrl("https://open.bigmodel.cn/api/paas/v4/")
//                .temperature(0.0)
//                .build();
//        UserMessage multiModalMessage = UserMessage.from(
//                TextContent.from("请将该音频转换为文字"),
//                fromFile()
//        );
//        // 3. 调用AudioModel
//        ChatResponse response = audioModel.chat(multiModalMessage);
//        System.out.println(response.aiMessage().text());
//    }
//
//    public static AudioContent fromFile() throws Exception {
//        URL resource = ClassLoader.getSystemResource("test.wav");
//        File file = new File(resource.getPath());
//        // 读取音频文件并编码为Base64
//        byte[] audioBytes = Files.readAllBytes(file.toPath());
//        String base64Audio = Base64.getEncoder().encodeToString(audioBytes);
//
//        // 创建Audio对象
//        Audio audio = Audio.builder()
//                .base64Data(base64Audio)
//                .mimeType("audio/wav")
//                .build();
//        return AudioContent.from(audio);
//    }
//}
