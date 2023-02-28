/**  
 * @Project Name:util  
 * @File Name:StringUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年4月17日下午5:42:40  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	
	
	/**  
	 * @Description: 将给定字符串填充之指定长度
	 * @param src 源字符串
	 * @param length 设置长度
	 * @param ch 填充字符
	 * @param append 是否为追加模式  是:在字符串结尾追加填充字符 否:在字符串开头插入填充字符
	 * @return    
	 */
	public static String setLength(String src, int length, char ch, boolean append) {
		if (StringUtil.isBlank(src))
			src = "";
		StringBuilder sb = new StringBuilder(src);
		while (sb.length() < length) {
			if (append) {
				sb.append(ch);
			} else {
				sb.insert(0, ch);
			}
		}
		return sb.substring(0,length).toString();
	}

	public static String setLength(String src, int length, char ch ) {
		return setLength(src, length, ch,false);
	}
	
	/**
	 * @Description: 检查传入字符串是否为 null 或空字符串 如果是返回true
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	
	public static void main(String[] args) {
//		System.out.println(setLength("1234",10,'0',false));
		DBField2CamelCase("DATA_ID");
	}
	
	
	public static String DBField2CamelCase(String field) {
		if (isBlank(field))
			return "";
		Pattern pattern = Pattern.compile("_([a-zA-Z])");
		Matcher matcher = pattern.matcher(field.toLowerCase());
		while (matcher.find()) {
			field = matcher.replaceFirst(matcher.group(1).toUpperCase());
			matcher.reset(field);
		}
		return field;
	}


	/**
	 * @param name  需要匹配的名称
	 * @param match 匹配规则
	 * @return 全部匹配成功返回true 若有一项匹配失败则返回false
	 */
	public static boolean matchName(String name, String... match) {
		if (name == null | match == null)
			return false;
		for (String m : match)
			if (isRegular(m))
				if (!Pattern.matches(m, name))
					return false;
				else if (name.indexOf(m) == -1)
					return false;
		return true;
	}


	public static boolean isRegular(String str) {
		assert str != null : "输入参数不能为null";
		return str.startsWith("/") && str.endsWith("/");
	}


	public static String unicodeToString(String str) {

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}

}
