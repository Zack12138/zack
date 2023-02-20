package org.zack.reptile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.*;

/**  
 * @Package: com.dls.reptile 
 * @ClassName: Reptile 
 * @Description: 爬取文件父类
 * @date: 2021年11月13日 下午11:25:39  
 *  
 * @author Zack  
 * @version V2.0  
 */
public abstract class Reptile {
	

	/**  
	 * url : 访问URL
	 * @version V2.0 
	 */
	protected String url;

	/**  
	 * target : 保存路径
	 * @version V2.0 
	 */
	protected String target;

	/**  
	 * manager : cookie管理器
	 * @version V2.0 
	 */
	protected CookieManager manager;

	/**  
	 * proxy : HTTP代理
	 * @version V2.0 
	 */
	protected Proxy proxy;
	
	/**  
	 * isProxy : 是否使用代理
	 * @version V2.0 
	 */
	protected boolean isProxy;


	/**
	 *  isProxy : 超时次数
	 */
	protected int timeoutTimes = 3;

	protected Map<String,String> connectPropertyMap = new HashMap<>();

	protected ExecutorService pool = Executors.newFixedThreadPool(10);

	public Reptile() {
		manager = new CookieManager();
		// 设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
		manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
		CookieHandler.setDefault(manager);
		proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080));
	}


	public abstract void download(String url,String target) throws Exception;

	@Deprecated
	protected void setConnectInfo (HttpsURLConnection urlCon){
		urlCon.setConnectTimeout(10000);
		urlCon.setReadTimeout(10000);
		urlCon.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36");

		for(Map.Entry<String,String> entry: connectPropertyMap.entrySet()){
			urlCon.setRequestProperty(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * TODO 下载文件到本地
	 *
	 * @param fileUrl
	 *            远程地址
	 * @param fileLocal
	 *            本地路径
	 * @throws Exception
	 */
	@Deprecated
	protected void downloadFile(String fileUrl, String fileLocal, String filename, String callId) throws Exception {
		SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
		sslcontext.init(null, new TrustManager[] { new X509TrustUtil() }, new java.security.SecureRandom());
		URL url = new URL(null, fileUrl);
		HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
			public boolean verify(String s, SSLSession sslsession) {
				System.out.println("WARNING: Hostname is not matched for cert.");
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
		HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
		setConnectInfo(urlCon);
		int code = urlCon.getResponseCode();
		if (code != HttpURLConnection.HTTP_OK) {
			throw new Exception("文件读取失败 " + code);
		}
		File file = new File(fileLocal);
		if (!file.exists()) {
			file.mkdirs();
		}



		// 读文件流
		DataInputStream in = new DataInputStream(urlCon.getInputStream());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal + File.separator + filename));
		byte[] buffer = new byte[2048];
		int count = 0;
//		printCookie(manager.getCookieStore());
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		out.close();
		in.close();
	}

	public static void main(String[] args) {
		StringBuilder className =  new StringBuilder();
		Class<?> clazz ;
		try {
			String name = Reptile.class.getName();
			className.append(name.substring(0,name.lastIndexOf("."))).append(".").append(args[0]);
			clazz = Class.forName(className.toString());
		} catch (ClassNotFoundException e) {
			System.out.println("参数有误");
			e.printStackTrace();
			return;
		}
		Object newInstance;
		try {
			newInstance = clazz.newInstance();
			if(!(newInstance instanceof Reptile)) {
				throw new Exception("参数错误 非本类子类不予加载");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Reptile rep = (Reptile)newInstance;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("请输入url");
		String url = sc.nextLine();
		
		
		try {
			rep.download(url, args[1]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	

}


class X509TrustUtil implements X509TrustManager {

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