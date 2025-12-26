package com.langchain.lesson08.indexing;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentTransformer;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;

import java.net.MalformedURLException;

public class DocumentTest {

    public static void main(String[] args) throws MalformedURLException {
        Document document = UrlDocumentLoader.load("https://docs.langchain4j.dev/tutorials/rag", new TextDocumentParser());
        DocumentTransformer documentTransformer =  new HtmlToTextDocumentTransformer();
        System.out.println(document.text().substring(0,400)); // 说出前300个字符
        System.out.println("========转换后=========");
        document = documentTransformer.transform(document);
        System.out.println(document.text().substring(0,50)); // 说出前50个字符
    }
}
