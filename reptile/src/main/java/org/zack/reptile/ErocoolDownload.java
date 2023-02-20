/**  
 * @Project Name:reptile  
 * @File Name:ErocoolDownload.java  
 * @Package Name:com.dls.reptile  
 * @Date:2020年8月16日上午1:26:46  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/

package org.zack.reptile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.zack.utils.NumberUtil;

public  class ErocoolDownload extends Reptile{
	
	/**  
	 * @Description: TODO
	 * @param args    
	 */
	/**  
	 * @Description: TODO
	 * @param args    
	 */
	public static void main(String[] args) {
		Scanner sc = null;
		while(true) {
			 sc = new Scanner(System.in);
			 System.out.println("请输入需要下载的URL");
			String url = sc.nextLine();
			
			if("exit".equals(url))
				break;

			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					ErocoolDownload erocoolDownload = new ErocoolDownload();
					erocoolDownload.url = url;
				}
			});
			thread.start();
		}
		if(sc != null) {
			sc.close();
		}
	}
	
	public void download(String url, String target) throws Exception {
		Document document = Jsoup.connect(url).get();
		Element mainDiv = document.selectFirst("#comicdetail");

		String name = mainDiv.select("h1").text();
		name = name.replaceAll("\\?|:", "");
		Elements imgDiv = mainDiv.select("div>img");
		System.out.println("URL:"+url);
		System.out.println("需要下载的漫画名:" + name);
		int size = imgDiv.size();
		System.out.println("漫画页数:" + imgDiv.size());

		List<String> eachAttr = imgDiv.eachAttr("data-src");

		List<String[]> timeoutList = new ArrayList<>();

		for (int i = 0; i < eachAttr.size(); i++) {
			String path = eachAttr.get(i);
			System.out.println("正在下载:" + path);
			String fileName = NumberUtil.getNumberString(i, String.valueOf(size).length()) + ".jpg";
			try {
				downloadFile(path, target + File.separator + name + File.separator, fileName, "");
			} catch (java.net.SocketTimeoutException e) {
				System.out.println("读取超时" + path);
				timeoutList.add(new String[] { String.valueOf(i), path });
			} catch (Exception e) {
				System.out.println("下载失败 失败原因"+e.getMessage());
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
				String fileName = NumberUtil.getNumberString(j, String.valueOf(size).length()) + ".jpg";
				try {
					downloadFile(path, target + File.separator + name + File.separator, fileName, "");
				} catch (java.net.SocketTimeoutException e) {
					System.out.println("读取超时" + path);
					timeoutListTmp.add(new String[] { String.valueOf(j), path });
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("下载失败 失败原因"+e.getMessage());
				}
			}

			timeoutList = timeoutListTmp;
		}

	}


}
