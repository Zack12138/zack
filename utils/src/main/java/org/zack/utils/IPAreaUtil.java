/**  
 * @Project Name:util  
 * @File Name:Test.java  
 * @Package Name:org.zack.utils  
 * @Date:2020年7月28日下午4:48:14  
 * Copyright (c) 2020, Zack All Rights Reserved.  
 *  
*/

package org.zack.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbMakerConfigException;
import org.lionsoul.ip2region.DbSearcher;

public final class IPAreaUtil {
	
	
	private static  PropertiesUtil ip2region ;
	private static  DbConfig dbConfig ;
	private static  String dbPath;
	private static  URL resource ;
	private static  String algosign;
	private static  DbSearcher searcher;
	
	static {
		init();
	}
	
	private static final void init() {
		 try {
			ip2region = new PropertiesUtil("ip2region.properties"); 
			 dbConfig = new DbConfig();                                    
			 dbPath = ip2region.get("dbPath");                               
			 resource = IPAreaUtil.class.getClassLoader().getResource(dbPath);  
			 algosign = ip2region.get("algoName");                           
			 searcher = new DbSearcher(dbConfig, resource.getPath());
		} catch (FileNotFoundException |DbMakerConfigException e) {
			e.printStackTrace();
		}    
	}
	
	
	public static DataBlock getIPArea(String ip) {
		int algorithm = DbSearcher.BTREE_ALGORITHM;
		if (algosign.equalsIgnoreCase("binary")) {
			algorithm = DbSearcher.BINARY_ALGORITHM;
		} else if (algosign.equalsIgnoreCase("memory")) {
			algorithm = DbSearcher.MEMORY_ALGORITYM;
		}
		DataBlock result = null;
		try {
			switch (algorithm) {
			case DbSearcher.BTREE_ALGORITHM:
				result = searcher.btreeSearch(ip);
				break;
			case DbSearcher.BINARY_ALGORITHM:
				result = searcher.binarySearch(ip);
				break;
			case DbSearcher.MEMORY_ALGORITYM:
				result = searcher.memorySearch(ip);
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static void main(String[] args) {
		DataBlock ipArea = getIPArea("114.221.204.245");
		System.out.println(ipArea);
		System.out.println(ipArea.getCityId());
		System.out.println(ipArea.getDataPtr());
		System.out.println(ipArea.getRegion());
		
		
		
		
	}
}
