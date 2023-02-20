package org.zack.utils.rpc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * RPC请求默认head信息
 */
@PropertySource(value = "classpath:/rpc/head.properties",encoding = "utf-8")
public class Head {

    @Value("${headPro.Accept}")
    private String accept;

    @Value("${headPro.Content-Type}")
    private String contentType;

    @Value("${headPro.User-Agent}")
    private String userAgent;



    public void test(){

        System.out.println(this);

    }

    @Override
    public String toString() {
        return "Head{" +
                "accept='" + accept + '\'' +
                ", contentType='" + contentType + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
