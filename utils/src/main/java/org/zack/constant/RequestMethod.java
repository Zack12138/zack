/**  
 * @Project Name:util  
 * @File Name:RequestMethod.java  
 * @Package Name:com.dls.constant  
 * @Date:2021年5月7日下午2:01:23  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/

package org.zack.constant;

/**
 * @Package: com.dls.constant
 * @ClassName: RequestMethod
 * @Description: Http请求方式
 * @date: 2021年5月7日 下午2:01:33
 * 
 * @author Zack
 * @version V2.0
 */
public enum RequestMethod {
	GET("GET"), HEAD("HEAD"), POST("POST"), PUT("PUT"), PATCH("PATCH"), DELETE("DELETE"), OPTIONS("OPTIONS"), TRACE(
			"TRACE");
	private String name;

	RequestMethod(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
