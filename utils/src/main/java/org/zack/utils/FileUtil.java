/**  
 * @Project Name:reptile  
 * @File Name:FileUtil.java  
 * @Package Name:com.dls.reptile.util  
 * @Date:2019年12月22日下午7:15:13  
 * Copyright (c) 2019, dongls All Rights Reserved.  
 *  
*/

package org.zack.utils;

import org.zack.constant.ReptileException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Package: com.dls.reptile.util
 * @ClassName: FileUtil
 * @Description: 文件操作类
 * @date: 2019年12月22日 下午7:55:13
 * 
 * @author dongls
 * @version V2.0
 */
public class FileUtil {

	private FileUtil() {
	}

	public static void saveFile(String path, String str) {
		saveFile(path, str, false);
	}

	public static void saveFile(String path, String str, boolean append) {
		FileWriter fileWriter = null;
		try {
			File file = new File(path);
			if (!file.exists())
				file.createNewFile();
			fileWriter = new FileWriter(path, append);
			fileWriter.write(str, 0, str.length());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static List<File> loadFileList(String path) {
		File file = new File(path);
		ArrayList<File> result = new ArrayList<>();
		if (!file.exists() || !file.canRead()) {
			throw new ReptileException("["+path+"]文件不存在或不可读");
		}
		if (file.isFile()){
			result.add(file);
		}else {
			File[] files = file.listFiles();
			result.addAll(Arrays.asList(files));
		}
		return result;
	}


	public static void saveByte(String path, byte[] bytes){
		saveByte(path,bytes,0,bytes.length);
	}
	public static void saveByte(String path, byte[] bytes,int off,int len){
		BufferedOutputStream out = null;
		try {
			File file = new File(path);
			if(!file.getParentFile().exists()){
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(bytes, off, len);
		}catch (Exception e){
			throw new ReptileException(e);
		}finally{
			try {
				out.flush();
			} catch (IOException e) {
			}
			try {
				out.close();
			} catch (IOException e) {
			}
		}


	}

}
