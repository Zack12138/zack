package org.zack.reptile;

import org.zack.constant.RequestMethod;
import org.zack.utils.HttpsUtil;

import java.util.Date;
import java.util.HashMap;

public class BikaDownload extends Reptile{

    public static void main(String[] args) {
        String url = "https://api.manhuabika.com/comics/63e3c27cc0d70f4660709b9c/recommendation";

        HashMap<String, String> herder = new HashMap<>();
//        herder.put(":authority","api.manhuabika.com");
//        herder.put(":method","GET");
//        herder.put(":path","/comics/63e3c27cc0d70f4660709b9c/recommendation");
//        herder.put(":scheme","https");
        herder.put("accept","application/vnd.picacomic.com.v1+json");
        herder.put("accept-encoding","gzip, deflate, br");
        herder.put("accept-language","zh,zh-TW;q=0.9,en-US;q=0.8,en;q=0.7,zh-CN;q=0.6");
        herder.put("app-channel","1");
        herder.put("app-platform","android");
        herder.put("app-uuid","webUUID");
        herder.put("authorization","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1OTk4NmU0ZWI2NzhlMjZmMzlkMjEzNjgiLCJlbWFpbCI6InExMzAwMjU4Nzk2NCIsInJvbGUiOiJtZW1iZXIiLCJuYW1lIjoi5aSn546L5oiR5Y-r5L6G5beh5bGxIiwidmVyc2lvbiI6IjIuMi4xLjMuMy40IiwiYnVpbGRWZXJzaW9uIjoiNDUiLCJwbGF0Zm9ybSI6ImFuZHJvaWQiLCJpYXQiOjE2NzYzMDgwMDYsImV4cCI6MTY3NjkxMjgwNn0.PsNA2bazKeqmeLUyh-3q9HQ4cImBNtGyIHkpIRaEVdo");
        herder.put("cache-control","no-cache");
        herder.put("content-type","application/json; charset=UTF-8");
        herder.put("dnt","1");
        herder.put("image-quality","medium");
        herder.put("nonce","mzbtjhbtdhmpepjkcpinaxbdcxyyc2s5");
        herder.put("origin","https://manhuabika.com");
        herder.put("pragma","no-cache");
        herder.put("sec-ch-ua","\"Not_A Brand\";v=\"99\", \"Google Chrome\";v=\"109\", \"Chromium\";v=\"109\"");
        herder.put("sec-ch-ua-mobile","?0");
        herder.put("sec-ch-ua-platform","Windows");
        herder.put("sec-fetch-dest","empty");
        herder.put("sec-fetch-mode","cors");
        herder.put("sec-fetch-site","same-site");
        herder.put("signature","1fa29f1cd4891d5097b005539f3c7bac9616ad70d02dd786d27a4675662b18a3");
        herder.put("time","1676879065");
        herder.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        byte[] bytes = HttpsUtil.httpsConnection(url, null, RequestMethod.POST, herder);

        System.out.println(new String(bytes));

    }


    @Override
    public void download(String url, String target) throws Exception {

    }
}
