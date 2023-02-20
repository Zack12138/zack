/**  
 * @Project Name:reptile  
 * @File Name:MeiZiTuDowload.java  
 * @Package Name:com.dls.reptile  
 * @Date:2020年9月10日下午9:15:24  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/

package org.zack.reptile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.zack.utils.NumberUtil;

public class Nvshens extends  Reptile{


	 private CookieManager manager;
	
	public Nvshens() {
        manager = new CookieManager();
        //设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(manager);
	}

	/**  
	 * @Description: TODO
	 * @param args
	 * @throws IOException    
	 */
	public static void main(String[] args){
		Nvshens meiZiTuDowload = new Nvshens();
		String url = "https://www.nvshens.org/g/34266/";
//		String url = "https://www.mzitu.com/235970";
		meiZiTuDowload.download(url, "D:\\迅雷下载\\和谐物\\2101\\美图");
	}

	public void download(String url, String target) {
		
		Document document;
		try {
			Response response = Jsoup.connect(url)
//					.header("Accept", "*/*")
//            .header("Accept-Encoding", "gzip, deflate")
//            .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
////            .header("Referer", url)
//            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
//            .timeout(10*1000)
            .execute();
			document = response.parse();
		} catch (IOException e1) {
			e1.printStackTrace();
			return ;
		}
		Element titleEle = document.selectFirst("#htilte");

		String title = titleEle.text();
		
		saveURL(url,target + File.separator + title + File.separator);

		Element totalEle = document.selectFirst("#dinfo span");

		int total = Integer.valueOf(totalEle.text().replaceAll("[^0-9]", ""));
		
		List<String[]> timeoutList = new ArrayList<>();
		
		Element ImgEle = document.selectFirst("#hgallery img");
		

		for (int i = 0; i < total; i++) {
			
			
			String path = ImgEle.absUrl("src");
			
//			String path = "";

			if(i != 0) {
				int index = path.lastIndexOf(".");
				String subStr = path.substring(index);
				path = path.substring(0, index);
				
				StringBuilder sb = new StringBuilder(path);
				if(i>=100) {
					sb.setLength(sb.length()-1);
				}
				sb.append(String.valueOf(i).replaceAll("^(\\d)$", "0$1")).append(subStr);
				path = sb.toString();
			}
			System.out.println("正在下载:" + path);
			
			String fileName = NumberUtil.getNumberString(i, String.valueOf(total).length()) + ".jpg";
			try {
				downloadFile(path, target + File.separator + title + File.separator, fileName, url + "/" + i);
			} catch (java.net.SocketTimeoutException e) {
				System.out.println("读取超时" + path);
				timeoutList.add(new String[] { String.valueOf(i), path });
			} catch (Exception e) {
				System.out.println("下载失败 失败原因" + e.getMessage());
			}
		}

		// 将连接超时的重新下载三次
		for (int index = 0; index < 3; index++) {
			System.out.println("下载完成。" + "下载失败" + timeoutList.size() + "个");
			if (timeoutList.isEmpty()) {
				break;
			}
			System.out.println("正在尝试第" + (index + 1) + "次重新下载超时的文件");
			List<String[]> timeoutListTmp = new ArrayList<>();
			for (int i = 0; i < timeoutList.size(); i++) {
				String[] arr = timeoutList.get(i);
				int j = Integer.valueOf(arr[0]);
				String path = arr[1];
				System.out.println("正在下载:" + path);
				String fileName = NumberUtil.getNumberString(j, String.valueOf(total).length()) + ".jpg";
				try {
					downloadFile(path, target + File.separator + title + File.separator, fileName, "");
				} catch (java.net.SocketTimeoutException e) {
					System.out.println("读取超时" + path);
					timeoutListTmp.add(new String[] { String.valueOf(j), path });
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("下载失败 失败原因" + e.getMessage());
				}
			}

			timeoutList = timeoutListTmp;
		}

	}

	private void saveURL(String url, String target) {
		File path = new File(target);
		if(!path.exists()) {
			path.mkdirs();
		}
		File file = new File(target+File.separator+"URL.txt");
		
		try(FileWriter writer = new FileWriter(file)){
			writer.write(url);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

//	/**
//	 * TODO 下载文件到本地
//	 *
//	 * @param fileUrl
//	 *            远程地址
//	 * @param fileLocal
//	 *            本地路径
//	 * @throws Exception
//	 */
//	public void downloadFile(String fileUrl, String fileLocal, String filename, String callId) throws Exception {
//		SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
//		sslcontext.init(null, new TrustManager[] { new X509TrustUtiil() }, new java.security.SecureRandom());
//		URL url = new URL(fileUrl);
//		HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
//			public boolean verify(String s, SSLSession sslsession) {
//				System.out.println("WARNING: Hostname is not matched for cert.");
//				return true;
//			}
//		};
//		HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
//		HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
//		HttpsURLConnection urlCon = (HttpsURLConnection) url.openConnection();
//		urlCon.setConnectTimeout(10000);
//		urlCon.setReadTimeout(10000);
//		urlCon.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.83 Safari/537.36");
//		urlCon.setRequestProperty("Referer", callId);
//		int code = urlCon.getResponseCode();
//		if (code != HttpURLConnection.HTTP_OK) {
//			throw new Exception("文件读取失败 " + code);
//		}
//		File file = new File(fileLocal);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//
//
//
//		// 读文件流
//		DataInputStream in = new DataInputStream(urlCon.getInputStream());
//		DataOutputStream out = new DataOutputStream(new FileOutputStream(fileLocal + File.separator + filename));
//		byte[] buffer = new byte[2048];
//		int count = 0;
//		printCookie(manager.getCookieStore());
//		while ((count = in.read(buffer)) > 0) {
//			out.write(buffer, 0, count);
//		}
//		out.close();
//		in.close();
//	}

	
	

//	public static boolean downloadImage(String imageUrl, String formatName, File localFile) {
//		boolean isSuccess = false;
//		URL url = null;
//		try {
//			url = new URL(imageUrl);
//			isSuccess = ImageIO.write(ImageIO.read(url), formatName, localFile);
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return isSuccess;
//	}
//
	

    //打印cookie信息
    public void printCookie(CookieStore cookieStore){
        List<HttpCookie> listCookie = cookieStore.getCookies();
        listCookie.forEach(httpCookie -> {
            System.out.println("--------------------------------------");
            System.out.println("class      : "+httpCookie.getClass());
            System.out.println("comment    : "+httpCookie.getComment());
            System.out.println("commentURL : "+httpCookie.getCommentURL());
            System.out.println("discard    : "+httpCookie.getDiscard());
            System.out.println("domain     : "+httpCookie.getDomain());
            System.out.println("maxAge     : "+httpCookie.getMaxAge());
            System.out.println("name       : "+httpCookie.getName());
            System.out.println("path       : "+httpCookie.getPath());
            System.out.println("portlist   : "+httpCookie.getPortlist());
            System.out.println("secure     : "+httpCookie.getSecure());
            System.out.println("value      : "+httpCookie.getValue());
            System.out.println("version    : "+httpCookie.getVersion());
            System.out.println("httpCookie : "+httpCookie);
        });
    }

}
