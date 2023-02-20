/**  
 * @Project Name:util  
 * @File Name:WeCharUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年1月17日上午10:14:44  
 * Copyright (c) 2020, dongls All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
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
@Component
@Configuration
@PropertySource(value= {"classpath:config/wechar.properties"})
//@ConfigurationProperties("")
public class WeCharUtil2 {

	@Autowired
	private Environment env;

	@Value("${wechar.url}")
	private  String url;

	@Value("${wechar.sckey}")
	private  String sckey;

	@Value("${wechar.suffix}")
	private  String suffix;
	
	public String send(String text, String desp) {
		StringBuilder sb = new StringBuilder(url).append(sckey).append(suffix);
		Map<String, String> param = new HashMap<String,String>();
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

}
