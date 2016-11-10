package com.icanit.bdmapversion2.util;

import java.io.File;
import java.io.IOException;

import android.util.Log;

	 
public class ConfigCache {
 
    public static final int CONFIG_CACHE_MOBILE_TIMEOUT  = 3600000;  //1 hour
    public static final int CONFIG_CACHE_WIFI_TIMEOUT    = 100000;   //10 minute
 
    public static String getUrlCache(String url,String netWorkState) {
        if (url == null) {  
            return null;
        }
        
        String filePath = FileUtils.getFilePath(url);
        File file = new File(filePath);
        long expiredTime = System.currentTimeMillis()-file.lastModified();
        
        if("GPRS".equals(netWorkState) && expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT ){
        	return null;
        }else if("WIFI".equals(netWorkState) && expiredTime > CONFIG_CACHE_WIFI_TIMEOUT){
        	return null;
        }       
        
        String result = null;
		try {
			result = FileUtils.readFile(filePath);
		} catch (IOException e) {
			return null;
		}
        Log.e("网络状态",netWorkState);
        return result;
    }
 
    public static void setUrlCache(String url,String content) {
//        File file = new FileCache().getFile(url);
    	String filePath = FileUtils.getFilePath(url);
        //创建缓存数据到磁盘，就是创建文件
		try {
			FileUtils.writeFile(filePath, content);
		} catch (IOException e) {
			FileUtils.deleteDirectory(filePath);
			e.printStackTrace();
		}
    }
}
