/**  
 * @Project Name:util  
 * @File Name:DateUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年8月26日上午11:53:44  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.utils;

import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>();
	
	{
		DATE_FORMAT_REGEXPS.put("^\\d{8}$", "yyyyMMdd");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
		DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
		DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
		DATE_FORMAT_REGEXPS.put("^\\d{12}$", "yyyyMMddHHmm");
		DATE_FORMAT_REGEXPS.put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
		DATE_FORMAT_REGEXPS.put("^\\d{14}$", "yyyyMMddHHmmss");
		DATE_FORMAT_REGEXPS.put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
		DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
		DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
		DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
	}
	

	/**
	 * * Determine SimpleDateFormat pattern matching with the given date string.
	 * Returns null if * format is unknown. You can simply extend DateUtil with more
	 * formats if needed. * @param dateString The date string to determine the
	 * SimpleDateFormat pattern for. * @return The matching SimpleDateFormat
	 * pattern, or null if format is unknown. * @see SimpleDateFormat
	 */
	public static String determineDateFormat(String dateString) {
		for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
			if (dateString.toLowerCase().matches(regexp)) {
				return DATE_FORMAT_REGEXPS.get(regexp);
			}
		}
		return null; // Unknown format.}
	}
}
 