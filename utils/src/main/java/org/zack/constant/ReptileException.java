/**  
 * @Project Name:util  
 * @File Name:ReptileException.java  
 * @Package Name:com.dls.constant  
 * @Date:2021年5月7日上午11:53:41  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/  
  
package org.zack.constant;
public class ReptileException extends RuntimeException{

	public ReptileException() {
		super();
	}

	public ReptileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReptileException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReptileException(String message) {
		super(message);
	}

	public ReptileException(Throwable cause) {
		super(cause);
	}

	/**  
	 * serialVersionUID : TODO
	 * @version V2.0 
	 */
	private static final long serialVersionUID = 1L;

}
 