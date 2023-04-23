package org.zack.reptile;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;
import org.zack.constant.RequestMethod;
import org.zack.utils.HttpsUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

public class BikaDownload extends Reptile{

    public static void main(String[] args) {
        String url = "https://manhuabika.com/pcomicview/?cid=641f1b09b13b5f4e477cb787";

        BikaDownload bikaDownload = new BikaDownload();
        try {
            bikaDownload.login();
//            bikaDownload.download(url,"");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


    @Override
    public void download(String url, String target) throws Exception {
        Document document = Jsoup.connect(url).get();
        Elements select = document.select(".my-cview-button");

        System.out.println(document);
        System.out.println(select.size());

        //  document.location.href = '/pchapter/?cid=' + cid + '&chapter=' + chapterId + '&chapterPage=' + 1 + "&maxchapter=" + maxchapter;

    }

    private void login() throws IOException {
        String url = "https://api.manhuabika.com/auth/sign-in";
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("email","q13002587964");
        paramMap.put("password","15105208758");
        String json = JSON.toJSONString(paramMap);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("accept" ,"application/vnd.picacomic.com.v1+json");
        headerMap.put("accept-encoding" ,"gzip, deflate, br");
        headerMap.put("accept-language" ,"zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6");
        headerMap.put("app-channel" ,"1");
        headerMap.put("app-platform" ,"android");
        headerMap.put("app-uuid" ,"webUUID");
        headerMap.put("authorization",null);
        headerMap.put("cache-control" ,"no-cache");
        headerMap.put("content-length" ,"49");
        headerMap.put("content-type" ,"application/json; charset=UTF-8");
        headerMap.put("dnt" ,"1");
        headerMap.put("image-quality" ,"medium");
        headerMap.put("nonce" ,"ptrxrnrdxe2mnhswkzhfwk7fjfibcexz");
        headerMap.put("origin" ,"https://manhuabika.com");
        headerMap.put("pragma" ,"no-cache");
        headerMap.put("sec-ch-ua" ,"\"Google Chrome\";v=\"111\", \"Not(A:Brand\";v=\"8\", \"Chromium\";v=\"111\"");
        headerMap.put("sec-ch-ua-mobile" ,"?0");
        headerMap.put("sec-ch-ua-platform" ,"\"Windows\"");
        headerMap.put("sec-fetch-dest" ,"empty");
        headerMap.put("sec-fetch-mode" ,"cors");
        headerMap.put("sec-fetch-site" ,"same-site");
        headerMap.put("signature" ,"60384734075b81eeaf433e13188f3a43e86ac7040a783cc7242cfed64a6918d7");
        headerMap.put("time" ,"1680268626");
        headerMap.put("user-agent" ,"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/111.0.0.0 Safari/537.36");

//        Connection connect = Jsoup.connect(url);
//        connect.headers(headerMap);
//        connect.data(json);
//        Document post = connect.post();
//        System.out.println(post);


        byte[] bytes = HttpsUtil.httpsConnection(url, json, RequestMethod.POST,headerMap);

        System.out.println(new String(bytes, StandardCharsets.UTF_8));

    }
}
