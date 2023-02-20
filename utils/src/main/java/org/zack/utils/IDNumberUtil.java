/**  
 * @Project Name:util  
 * @File Name:IDNumberUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年3月13日上午11:37:58  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.util.regex.Pattern;

/**
 * 身份证校验工具类
 */
public final class IDNumberUtil {

	private final static int[] arr = init();

	private final static int[] init() {

		final int length = 17;
		int[] result = new int[length];

		for (int i = 0; i < result.length; i++) {
			result[i] = power(2, result.length - i);
		}
		return result;
	}

	/**
	 * @Description: 验证传入身份证校验位是否正确
	 * @return
	 */
	public final static boolean changeIDNumber(String id) {

		if (!checkForLegitimacy(id)) {
			throw new RuntimeException("传入参数[" + id + "]不是有效的身份证");
		}

		// 截取身份证号码数字位
		String prefix = id.substring(0, 17);

		// 截取身份证号码校验位
		String suffix = id.substring(17, 18);

		int sum = 0;

		// 将数字位按位乘以每位权重 取和
		for (int i = 0; i < arr.length; i++) {
			sum += (prefix.charAt(i) - 48) * arr[i];
		}

		// 将按位权重之和模除11后得到余额 为校验码
		int checkCode = sum % 11;

		// 将校验码处理为最终校验码
		checkCode = (12 - checkCode) % 11;

		int inputCheckCode = suffix.charAt(0) - 48;
		
		if(inputCheckCode>10) {
			inputCheckCode = 10;
		}
		
		
		System.out.println("校验码:"+checkCode);
		// 判断是否与所给出的校验码一致
		return checkCode == inputCheckCode;
	}

	/**
	 * @Description: 验证输入身份证合规
	 * @return 若参数为18位身份证 则返回true 否则返回false
	 */
	private final static boolean checkForLegitimacy(String id) {
		if (id == null)
			return false;
		String pattern = "^[0-9]{17}[0-9xX]$";
		return Pattern.matches(pattern, id);
	}

	private static int power(int num, int pow) {
		int power = 1;
		for (int i = 0; i < pow; i++)
			power *= num;
		return power;
	}

	public static void main(String[] args) {
		System.out.println(changeIDNumber("320302199805181115"));
	}
}
