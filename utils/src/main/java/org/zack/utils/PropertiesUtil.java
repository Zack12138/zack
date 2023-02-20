/**  
 * @Project Name:util  
 * @File Name:WeCharUtil.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年1月17日上午9:47:12  
 * Copyright (c) 2020, dongls All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public final class PropertiesUtil {

	private Properties prop = new Properties();

	private File file;

	/**  
	 * @Description: 使用项目相对路径加载配置文件
	 * @param path    
	 */
	public PropertiesUtil(String path) {
		load(path);
	}

	public PropertiesUtil(URL url) {
		load(url);
	}

	@Deprecated
	public PropertiesUtil(File file) {
		load(file);
	}

	/**  
	 * @Description: 使用项目相对路径加载配置文件
	 * @param path    
	 */
	private void load(String path) {
		try {
			URL resource = PropertiesUtil.class.getClassLoader().getResource(path);
			file = new File(resource.toURI());
			FileInputStream fileInputStream = new FileInputStream(file);
			prop.load(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	private void load(URL url) {
		try {
			file = new File(url.toURI());
			FileInputStream fileInputStream = new FileInputStream(file);
			prop.load(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Deprecated
	private void load(File file) {
		try {
			this.file = new File(file.getPath());
			FileInputStream fileInputStream = new FileInputStream(this.file);
			prop.load(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void reload() {
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			prop.load(fileInputStream);
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void save(String key, String value, String comments) throws IOException {
		prop.setProperty(key, value);
		if (file != null) {
			FileWriter fileWriter = new FileWriter(file);
			prop.store(fileWriter, comments);
			fileWriter.close();
		}
	}

	public void save(String comments) throws IOException {
		if (file != null) {
			FileWriter fileWriter = new FileWriter(file);
			prop.store(fileWriter, comments);
			fileWriter.close();
		}
	}

	public Set<Map.Entry<Object, Object>> toSet(){
		return prop.entrySet();
	}

	public Map<String,String> toMap(){
		Map<String,String> result = new HashMap<>();
		Set<Map.Entry<Object, Object>> entries = prop.entrySet();

		for (Map.Entry<Object, Object> e: entries) {
			result.put((String)e.getKey(),(String)e.getValue());
		}

		return result;
	}

	public String get(String key) {
		return prop.getProperty(key);
	}

	public void set(String key, String value) {
		prop.setProperty(key, value);
	}

}
