/**  
 * @Project Name:util  
 * @File Name:HttpsUtils.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年2月24日上午11:33:51  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.utils; 
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsRequest {

  private final Logger logger = LoggerFactory.getLogger(HttpsRequest.class);
  
  private final String charset = "UTF-8";

/**
* 处理https GET/POST请求
* @param requestUrl 请求地址
* @param requestMethod 请求方法
* @param requestStr 请求参数
* @return
*/
  public String httpsRequest(String requestUrl, String requestMethod, String requestStr) {
    logger.info("req---->:" + requestMethod + requestStr);
    HttpsURLConnection httpsConnection = null;
    try {
      // 创建SSLContext
      SSLContext sslContext = SSLContext.getInstance("SSL");
      TrustManager[] tm = { new TrustManager() };
      // 初始化
      sslContext.init(null, tm, new java.security.SecureRandom());

      // 获取SSLSocketFactory对象
      SSLSocketFactory ssf = sslContext.getSocketFactory();
      HostnameVerifier HostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String var1, SSLSession var2) {
          return true;
        }

      };

      URL url = new URL(requestUrl);
      httpsConnection = (HttpsURLConnection) url.openConnection();
      httpsConnection.setDoOutput(false);
      httpsConnection.setDoInput(true);
      httpsConnection.setConnectTimeout(3 * 1000);
      httpsConnection.setReadTimeout(15 * 1000);
//      httpsConnection.setRequestProperty("Content-Type", "application/json");
      httpsConnection.setRequestProperty("Charset", charset);
      httpsConnection.setRequestProperty("Authorization", "Basic aXdidXNlcjp0ZXN0MDAwMA==");
      httpsConnection.setRequestProperty("User-Agent", "Client identifier");
      httpsConnection.setRequestMethod(requestMethod);
      /*
      * httpsConnection.setUseCaches(false);
      * httpsConnection.setRequestMethod(requestMethod);
      */
      // 设置当前实例使用的SSLSoctetFactory
      httpsConnection.setSSLSocketFactory(ssf);
      httpsConnection.setHostnameVerifier(HostnameVerifier);
      httpsConnection.connect();
      // 往服务器端写内容
      // 读取服务器端返回的内容
      InputStream inputStream = httpsConnection.getInputStream();
      if (httpsConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        logger.error("connect ezcs service failed: " + httpsConnection.getResponseCode());
        return httpsConnection.getResponseCode()+"HTTPS访问错误";
      }
      String response = StreamUtil.convertStreamToString(inputStream, charset);
      logger.debug("访问返回数据: " + response);
      return response;
    } catch (Exception e) {
      logger.debug("连接时出现异常: " + e.getMessage());
     return "访问错误:"+e.getMessage();
    } finally {
      if (httpsConnection != null) {
        httpsConnection.disconnect();
      }
    }
  }

  class TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

    }  

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return new X509Certificate[] {};
    }

  }

  public static class ResponseKey {
    public static final String KEY_RESULT = "result";
    public static final String KEY_REASON = "reason";
    public static final String KEY_DATA = "data";
    public static final String KEY_EXTRA = "extra";
  }

}
 