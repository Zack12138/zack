/**  
 * @Project Name:util  
 * @File Name:SmbUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年6月22日上午10:53:41  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
*/  
/**  
 * @Project Name:util  
 * @File Name:SmbUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2021年6月22日上午10:53:41  
 * Copyright (c) 2021, Zack All Rights Reserved.  
 *  
 */  
  
package org.zack.utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.zack.constant.ReptileException;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;

/**  
 * @Package: org.zack.utils 
 * @ClassName: SmbUtil 
 * @Description: 访问通过SMB共享的路径
 * @date: 2021年6月22日 上午10:53:41  
 *  
 * @author Zack  
 * @version V2.0  
 */

public class SmbUtil {
	
	private String url;
	
	SMBClient client;
	
	Session session;
	
	String baseDir = "视频";
	
	public SmbUtil(String ip,String user,String password,String dir) {
		SmbConfig config = SmbConfig.builder().withTimeout(120, TimeUnit.SECONDS)
				.withTimeout(120, TimeUnit.SECONDS) // 超时设置读，写和Transact超时（默认为60秒）
	            .withSoTimeout(180, TimeUnit.SECONDS) // Socket超时（默认为0秒）
	            .build();
		
		// 如果不设置超时时间	SMBClient client = new SMBClient();
		client = new SMBClient(config);

		try {
			// IP 地址
			Connection connection = client.connect(ip);	
			// 用户名 密码 计算机域名
//			AuthenticationContext ac = new AuthenticationContext(user, password.toCharArray(), "zack-mi-pc");
			session = connection.authenticate(AuthenticationContext.anonymous());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public SmbUtil(String url) {
		this.url = url;
	}
	
	public void createDir(String dir) throws ReptileException {
		
		DiskShare  connectShare = (DiskShare)session.connectShare(baseDir);
		
		// 判断是否存在文件夹
		boolean folderExists = connectShare.folderExists(dir);
		// 判断是否存在文件
		boolean fileExists = connectShare.fileExists(dir);
		
		// 目录已存在创建文件夹
		if (folderExists) {
			
			System.out.println("文件夹已存在");
			
		}else if(fileExists) {
			throw new ReptileException("存在同名文件 ["+baseDir+"\\"+dir+"] 创建失败");
		} else {
			// 目录不存在的情况下，会抛出异常
			connectShare.mkdir(dir);
			System.out.println("文件夹创建成功");
		}
	}

	
	
	public static void main(String[] args) {
		SmbUtil smbUtil = new SmbUtil("127.0.0.1", "zack", "1231", "视频");
		try {
			smbUtil.createDir("test");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
 