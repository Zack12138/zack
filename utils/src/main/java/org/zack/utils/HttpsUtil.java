/**  
 * @Project Name:util  
 * @File Name:HttpsUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年2月24日上午11:21:09  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.zack.constant.ReptileException;
import org.zack.constant.RequestMethod;

public class HttpsUtil {

	private static final Log log = LogFactory.getLog(HttpsUtil.class);

	public static byte[] httpsConnection(String httpsUrl) throws ReptileException {

		return httpsConnection(httpsUrl, null,RequestMethod.GET,null);
	}

	public static byte[] httpsConnection(String httpsUrl, Map<String, ?> param) throws ReptileException {
		
		return httpsConnection(httpsUrl, param,RequestMethod.GET,null);
	}
	
	public static byte[] httpsConnection(String httpsUrl, Map<String, ?> param,RequestMethod method) throws ReptileException {
		return httpsConnection(httpsUrl, param, method, null);
	}
	
	public static byte[] httpsConnection(String httpsUrl, Map<String, ?> param,RequestMethod method,Map<String,String> requestHead) throws ReptileException {

		DataInputStream in = null;
		DataOutputStream out = null;
		ByteArrayOutputStream arrayOut = null;
		HttpsURLConnection urlCon = null;
		StringBuilder paramStr = new StringBuilder();

		if (param != null) {
			Iterator<?> iterator = param.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator.next();
				paramStr.append("&").append(entry.getKey()).append("=").append(entry.getValue());
			}
			if(RequestMethod.POST.equals(method)) {
				// 将参数中的第一个符号删除
				paramStr.delete(0, 1);
			}else {
				// 判断URL中是否包含? 若包含直接追加参数 不包含则把第一个&更改为?
				if (httpsUrl.indexOf("?") == -1) {
					paramStr.replace(0, 1, "?");
				}
				paramStr.insert(0, httpsUrl);
				httpsUrl = paramStr.toString();
			}
		}

		try {
			SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
			sslcontext.init(null, new TrustManager[] { new X509TrustUtiil() }, new java.security.SecureRandom());
			@SuppressWarnings("restriction")
			URL url = new URL(null, httpsUrl);//, new sun.net.www.protocol.https.Handler()
			HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
				public boolean verify(String s, SSLSession sslsession) {
					System.out.println("WARNING: Hostname is not matched for cert.");
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
			if("https".equalsIgnoreCase(url.getProtocol())){
				ignoreSsl();
			}
			urlCon = (HttpsURLConnection) url.openConnection();
			urlCon.setConnectTimeout(120000);
			urlCon.setReadTimeout(120000);
			if(method == null)
				method = RequestMethod.GET;
			urlCon.setRequestMethod(method.getName());
			
			// 判断是否需要添加请求头部信息
			if (requestHead != null) {
				Set<Entry<String, String>> entrySet = requestHead.entrySet();
				for (Entry<String, String> entry : entrySet) {
					urlCon.setRequestProperty(entry.getKey(), entry.getValue());
				}
			} else {
				urlCon.setRequestProperty("user-agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36");
			}
			
			if(RequestMethod.POST.equals(method)) {
				urlCon.setDoOutput(true);
				out = new DataOutputStream(urlCon.getOutputStream());
				out.write(paramStr.toString().getBytes());
				out.flush();
			}
			
			int code = urlCon.getResponseCode();
			if (code != HttpURLConnection.HTTP_OK) {
				throw new ReptileException("文件读取失败 " + code);
			}
			
			
			in = new DataInputStream(urlCon.getInputStream());

			arrayOut = new ByteArrayOutputStream();

			int index = -1;
			byte[] cache = new byte[1024];
			while ((index = in.read(cache)) != -1) {
				arrayOut.write(cache, 0, index);
			}

			return arrayOut.toByteArray();

		} catch (Exception e) {
			throw new ReptileException(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("关闭HTTPS返回数据流失败",e);
				}
			}

			if (arrayOut != null) {
				try {
					arrayOut.close();
				} catch (IOException e) {
					log.error("关闭返回结果数组流失败",e);
				}
			}
			
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error("关闭HTTPS发送数据流失败",e);
				}
			}
			
			if(urlCon != null) {
				urlCon.disconnect();
			}
		}
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new miTM();
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

	static class miTM implements TrustManager,X509TrustManager {
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public boolean isServerTrusted(X509Certificate[] certs) {
			return true;
		}

		public boolean isClientTrusted(X509Certificate[] certs) {
			return true;
		}

		public void checkServerTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}

		public void checkClientTrusted(X509Certificate[] certs, String authType)
				throws CertificateException {
			return;
		}
	}

	/**
	 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
	 * @throws Exception
	 */
	public static void ignoreSsl() throws Exception{
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

}




class X509TrustUtiil implements X509TrustManager {

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		// TODO Auto-generated method stub

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		// TODO Auto-generated method stub
		return null;
	}

}
