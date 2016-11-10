package com.what.yunbao.util;

import java.io.File;  

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.util.AppUtil;

import android.content.Context;  
import android.os.Environment;  
  
/** 
 * 本应用数据清除管理器 
 */  
public class DataCleanManager {  
    /** 
     * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) 
     *  
     * @param context 
     */  
    public static void cleanInternalCache(Context context) {  
        AppUtil.deleteFileRecursively(context.getCacheDir(),true);  
    }  
    
  
    /** 
     * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) 
     *  
     * @param context 
     */  
    public static void cleanDatabases(Context context) {  
    	AppUtil.deleteFileRecursively(new File("/data/data/"  
                + context.getPackageName() + "/databases"),true);  
    }  
  
    /** 
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) 
     *  
     * @param context 
     */  
    public static void cleanSharedPreference(Context context) {  
    	AppUtil.deleteFileRecursively(new File("/data/data/"  
                + context.getPackageName() + "/shared_prefs"),true);  
    }  
  
    /** 
     * 按名字清除本应用数据库 
     *  
     * @param context 
     * @param dbName 
     */  
    public static void cleanDatabaseByName(Context context, String dbName) {  
        context.deleteDatabase(dbName);  
    }  
  
    /** 
     * 清除/data/data/com.xxx.xxx/files下的内容 
     *  
     * @param context 
     */  
    public static void cleanFiles(Context context) {  
    	AppUtil.deleteFileRecursively(context.getFilesDir(),true);  
    }  
  
    /** 
     * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) 
     *  
     * @param context 
     */  
    public static void cleanExternalCache(Context context) {  
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
        	AppUtil.deleteFileRecursively(context.getExternalCacheDir(),true);  
        }  
    }  
    
    public static void cleanExternalFileDir(Context context) {  
        if (Environment.getExternalStorageState().equals(  
                Environment.MEDIA_MOUNTED)) {  
        	AppUtil.deleteFileRecursively(context.getExternalFilesDir(null),true);  
        }  
    }  
  
    /** 
     * 清除自定义路径下的文件
     *  
     * @param filePath 
     */  
    public static void cleanCustomCache(String filePath) {  
    	AppUtil.deleteFileRecursively(new File(filePath),true);  
    }  
  
    /** 
     * 清除本应用所有的数据 
     *  
     * @param context 
     * @param filepath 
     */  
    public static void cleanApplicationData(Context context, String... filepath) {  
        cleanInternalCache(context);  
        cleanExternalCache(context);  
        cleanDatabases(context);  
        cleanSharedPreference(context);  
        cleanFiles(context);  
        cleanExternalFileDir(context);
        cleanCustomCache(CommonUtil.getRootFilePath(context) + IConstants.FILE_PATH);
		cleanCustomCache(Environment.getDataDirectory().getAbsolutePath() + "/data/"
				+context.getPackageName()+"/" + IConstants.FILE_PATH);
        for (String filePath : filepath) {  
            cleanCustomCache(filePath);  
        }   
    }  
    
    public static void cleanApplicationCacheData(Context context){
    	cleanInternalCache(context);
		cleanExternalCache(context);
		cleanCustomCache(CommonUtil.getRootFilePath(context) + IConstants.FILE_PATH);
		cleanCustomCache(Environment.getDataDirectory().getAbsolutePath() + "/data/"
				+context.getPackageName()+"/" + IConstants.FILE_PATH);
//		cleanFiles(context);
//		cleanExternalFileDir(context);
		
    }
    
    
    public static long getApplicationCacheBytes(Context context){
    	long bytes=0;
    	File file = context.getCacheDir();
    	bytes+=AppUtil.getDirBytes(file);
    	file=new File(Environment.getDataDirectory().getAbsolutePath() + "/data/"
				+context.getPackageName()+"/" + IConstants.FILE_PATH);
    	bytes+=AppUtil.getDirBytes(file);
    	if (CommonUtil.hasSDCard()) {
			// getExternalCacheDir()
			file =	context.getExternalCacheDir().getParentFile();
			bytes+= AppUtil.getDirBytes(file);
		}
    	return bytes;
    }
  
} 
