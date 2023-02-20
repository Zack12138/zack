/**  
 * @Project Name:util  
 * @File Name:StreamUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年2月24日上午11:43:04  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.utils;

import java.io.IOException;
import java.io.InputStream;

public final class StreamUtil {
	
	public static String convertStreamToString (InputStream in,String charset) throws IOException {
		if (in == null) 
			throw new NullPointerException("输入流为NULL");
		if (StringUtil.isBlank(charset)) 
			charset = "UTF-8";
		
		StringBuilder result = new StringBuilder();
		int index = -1;
		
		byte[] b = new byte[4*1024];
		while((index = in.read(b)) != -1) {
			result.append(new String(b,0,index));
		}
		return result.toString();
	}
}
 