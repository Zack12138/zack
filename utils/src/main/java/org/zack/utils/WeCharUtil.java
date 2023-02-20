/**  
 * @Project Name:util  
 * @File Name:WeCharUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年1月17日上午10:14:44  
 * Copyright (c) 2020, dongls All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.PropertySource;

import org.zack.constant.ReptileException;
import org.zack.constant.RequestMethod;

/**  
 * @Package: org.zack.utils 
 * @ClassName: WeCharUtil 
 * @Description: 微信通知工具类
 * @date: 2020年7月15日 下午6:08:22  
 *  
 * @author Zack  
 * @version V2.0  
 */
//@PropertySource("classpath:config/WeChar.properties")
public final class WeCharUtil {

	private static final String path = "config/wechar.properties";

	private static final PropertiesUtil prop = new PropertiesUtil(path);

	private static String url;

	private static String sckey;

	private static String suffix;

	static {
		loadProperties();
	}
	
	/**  
	 * Creates a new instance of WeCharUtil.  
	 * 
	 * 私有化构造器 不允许创建对象
	 *    
	 */  
	private WeCharUtil() {
	}

	private static void loadProperties() {
		url = prop.get("wechar.url");
		sckey = prop.get("wechar.sckey");
		suffix = prop.get("wechar.suffix");
	}

	public static void reload() {
		prop.reload();
		loadProperties();
	}

	public static String send(String text, String desp) {
		StringBuilder sb = new StringBuilder(url).append(sckey).append(suffix);
		Map<String, String> param = new HashMap<String, String>();
		param.put("text", text);
		param.put("desp", desp);
		byte[] httpConnection = null;
		try {
			httpConnection = HttpsUtil.httpsConnection(sb.toString(), param, RequestMethod.POST);
		} catch (ReptileException e) {
			e.printStackTrace();
		}
		return new String(httpConnection);
	}
	
	public static void main(String[] args) {
		send("test", "测试数据4");
	}

}
