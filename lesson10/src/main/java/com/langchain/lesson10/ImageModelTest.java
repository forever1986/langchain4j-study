package com.langchain.lesson10;

import dev.langchain4j.community.model.zhipu.ZhipuAiImageModel;
import dev.langchain4j.community.model.zhipu.image.ImageModelName;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.output.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ImageModelTest {

    public static void main(String[] args) throws Exception {
        // 1.获取环境变量中的API KEY
        String apiKey = System.getenv("ZHIPU_API_KEY");
        // 2. 构建ImageModel
        ImageModel imageModel = ZhipuAiImageModel.builder()
                .apiKey(apiKey)
                .model(ImageModelName.COGVIEW_3)
                .build();
        // 3. 调用ImageModel
        Response<Image> response = imageModel.generate("生成一只老虎的照片");
        System.out.println(response.content().url());
        // 4. 将结果下载到本地
        downloadImage(response.content().url().toString());
    }

    // 将文件写入本地
    private static void downloadImage(String url) throws Exception{
        InputStream inputStream = new URL(url).openStream();
        URL resource = ClassLoader.getSystemResource("");
        File file = new File(resource.getPath()+"test.png");
        if(!file.exists()){
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int byteRead;
        while ((byteRead=inputStream.read(buffer)) != -1){
            outputStream.write(buffer, 0, byteRead);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }
}
