/**  
 * @Project Name:reptile  
 * @File Name:QQqunDowload.java  
 * @Package Name:com.dls.reptile  
 * @Date:2021年2月22日下午4:45:29  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/

package org.zack.reptile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.zack.constant.ReptileException;
import org.zack.utils.FileUtil;
import org.zack.utils.HttpsUtil;
import org.zack.utils.SysClipboardUtil;
import org.zack.utils.threadPool.ThreadPool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import org.zack.utils.NumberUtil;

@Service
public class QQqunDownload extends Reptile {

	public static void main(String[] args) {
		Scanner scanner  = null;
		String downloadPath = null;
		try {
			try {
				downloadPath = args[0];
			} catch (Exception e) {
				downloadPath = "E:/VOL/F/迅雷下载/和谐物/图片/2201";
			}
			QQqunDownload down = new QQqunDownload();
			String url = null;
			url = SysClipboardUtil.getSysClipboardText();
			
			down.download(url,downloadPath,"123");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(scanner != null) {
				scanner.close();
			}
		}
	}

	public void download(String url, String target) throws IOException{
		download(url, target, null);
	}
	
	public void download(String url,String target,String pwd) throws IOException {
		if(pwd == null || "".equals(pwd.trim())) {
			pwd = getPassword(url);
		}

		Document document = Jsoup.connect(url).cookie("pwd", pwd).get();
		
		Element titleEle = document.selectFirst("h2.note-title");

		final String title = titleEle.text().replaceAll("\\.", "");

		saveURL(url,target + File.separator + title + File.separator);
		
		Elements eles = document.select("div.note-content img");

		final List<String[]> timeoutList = new ArrayList<>();
		
		Element imgEle = null;



		for (int i = 0; i < eles.size(); i++) {

			imgEle = eles.get(i);
			String imgUrl = imgEle.absUrl("src");

			System.out.println("[" + title + "]正在下载:" + imgUrl);

			String fileName = NumberUtil.getNumberString(i, String.valueOf(eles.size()).length()) + ".jpg";

			String savePath = target + File.separator + title + File.separator+fileName;
			int finalI = i;
//			String callId = url + "/" + finalI;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//			connectPropertyMap.put("Referer",callId);
			ThreadPool.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					try {
						byte[] bytes = HttpsUtil.httpsConnection(imgUrl);
						FileUtil.saveByte(savePath, bytes);
					} catch (ReptileException e) {
						if (e.getCause() instanceof java.net.SocketTimeoutException) {
							System.out.println("读取超时" + imgUrl);
							for (int index = 0; index < timeoutTimes; index++) {
								try {
									byte[] bytes = HttpsUtil.httpsConnection(imgUrl);
									FileUtil.saveByte(savePath, bytes);
									break;
								} catch (Exception ex) {
									System.out.println("读取超时" + imgUrl);
								}
							}
						} else {
							e.printStackTrace();
						}
					}
				}
			});
		}
	}

	private String getPassword(String url) throws IOException {
		Document document = Jsoup.connect(url).get();
		Element selectFirst = document.selectFirst("meta[name=description]");
		String pwd = selectFirst.attr("content");
		return pwd.replaceAll("[^0-9]", "");
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
}
 