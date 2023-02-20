/**  
 * @Project Name:reptile  
 * @File Name:MeiZiTuDowload.java  
 * @Package Name:com.dls.reptile  
 * @Date:2020年9月10日下午9:15:24  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/

package org.zack.reptile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.zack.utils.NumberUtil;

public class MeiZiTuDownload extends Reptile{
	
	public MeiZiTuDownload() {
        manager = new CookieManager();
        //设置cookie策略，只接受与你对话服务器的cookie，而不接收Internet上其它服务器发送的cookie
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(manager);
        proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 1080));
	}
	
	/**  
	 * @Description: TODO
	 * @param args
	 * @throws IOException    
	 */
	public static void main(String[] args)  {
		MeiZiTuDownload meiZiTuDowload = new MeiZiTuDownload();
		String[] arr = new String[] {
			"https://www.mzitu.com/238409",
			"https://www.mzitu.com/238802",
			"https://www.mzitu.com/45866",
			"https://www.mzitu.com/238207",
			"https://www.mzitu.com/233543",
			"https://www.mzitu.com/230117",
			"https://www.mzitu.com/236663",
			"https://www.mzitu.com/235125",
			"https://www.mzitu.com/232240",
			"https://www.mzitu.com/140526",
			"https://www.mzitu.com/228896"
		};
		for(int i = 0;i<arr.length;i++)
			meiZiTuDowload.download(arr[i], "D:\\迅雷下载\\和谐物\\2105\\美图");
	}

	public void download(String url, String target) {
		
		Document document;
		try {
			Response response = Jsoup.connect(url).proxy(proxy)
//					.header("Accept", "*/*")
//            .header("Accept-Encoding", "gzip, deflate")
//            .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
////            .header("Referer", url)
            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
//            .timeout(10*1000)
            .execute();
			document = response.parse();
		} catch (IOException e1) {
			e1.printStackTrace();
			return ;
		}
		Element titleEle = document.selectFirst("h2.main-title");

		String title = titleEle.text();
		
		saveURL(url,target + File.separator + title + File.separator);

		Element totalEle = document.selectFirst(".dots").nextElementSibling();

		int total = Integer.valueOf(totalEle.text());

		List<String[]> timeoutList = new ArrayList<>();

		for (int i = 1; i <= total; i++) {
			
			Document documentInner;
			try {
				documentInner = Jsoup.connect(url + "/" + i).proxy(proxy).get();
			} catch (IOException e1) {
				e1.printStackTrace();
				continue;
			}
			
			String path = documentInner.selectFirst("img.blur").attr("src");
			
//			String path = "";
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
}
