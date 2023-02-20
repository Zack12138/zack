/**  
 * @Project Name:util  
 * @File Name:NumberUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年8月16日上午1:51:17  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.utils; 
public class NumberUtil {

	private NumberUtil() {}
	
	public static String getNumberString(Number num,int length) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0 ; i<length;i++) {
			sb.append("0");
		}
		
		sb.append(num);
		return sb.substring(sb.length()-length, sb.length());
	}
	
	public static void main(String[] args) {
		System.out.println(getNumberString(100,5));
	}
}
 