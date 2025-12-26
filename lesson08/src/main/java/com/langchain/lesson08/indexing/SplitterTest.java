package com.langchain.lesson08.indexing;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentByLineSplitter;
import dev.langchain4j.data.segment.TextSegment;

import java.io.File;
import java.net.URL;
import java.util.List;

public class SplitterTest {

    public static void main(String[] args) {
        URL resource = ClassLoader.getSystemResource("splitter.txt");
        File file = new File(resource.getPath());
        Document document = FileSystemDocumentLoader.loadDocument(file.toPath());
        // maxSegmentSizeInChars是每个TextSegment的最大长度；maxOverlapSizeInChars是最大重叠长度
        DocumentByLineSplitter  documentByLineSplitter = new DocumentByLineSplitter(200,20);
        List<TextSegment> segments = documentByLineSplitter.splitAll(document);
        int n = 0;
        for (TextSegment segment : segments){
            System.out.println("=======第"+(++n)+"行======");
            System.out.println(segment.text());
        }
    }
}
