package com.icanit.bdmapversion2.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.icanit.bdmapversion2.MapApplication;

import android.util.Log;

public class FileUtils {
	/**
	 * 缓存路径
	 * @param url
	 * @return
	 */
	public static String getFilePath(String url){
		String cachePath = CommonUtil.getRootFilePath() + "Android/data/"+MapApplication.mPckName+"/cache/";
		if(createDirectory(cachePath)){
			return cachePath + StringUtils.replaceUrlWithPlus(url);
		}
		return "";
	}
	
	/**
	 * 读取文件
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		if (null == filePath) {
			Log.e("","Invalid param. filePath: " + filePath);
			return null;
		}
		StringBuffer sb=new StringBuffer();
		BufferedReader reader=null;
		if (fileIsExist(filePath)) {
			File f = new File(filePath);			
			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
				sb = new StringBuffer();
				String line;
				while((line = reader.readLine())!=null){
					sb.append(line);
				}				
			} finally{
				if(reader != null){
					reader.close();
				}
			}            
		} else {
			return null;
		}
		
		return sb.toString();
	}
	
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			Log.e("","param invalid, filePath: " + filePath);
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()||!f.isFile()) {
			return false;
		}
		return true;
	}
	
	public static boolean createDirectory(String filePath){
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()){
			return true;
		}
		
		return file.mkdirs();

	}
	
//	public static void createFile(String appname){
//		if(null==appname){
//			return;
//		}
//		File file=new File();
//	}
	
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			Log.e("","Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				Log.d("","delete filePath: " + list[i].getAbsolutePath());
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		file.delete();
		return true;
	}
	/**
	 * 文本写入
	 * @param filePath
	 * @param content
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String filePath,String content) throws IOException{
		if(filePath==null||content==null||filePath.length()<1){
			return false;
		}
			

		File file=new File(filePath);
		if(!file.exists()){
			if(!file.createNewFile()){
				return false;
			}
		}
		BufferedWriter writer=null;
		//系统默认编码utf-8 故默认utf-8
		try {
			writer=new BufferedWriter(new FileWriter(file,false));
			writer.write(content);		
			writer.flush();
		} finally{
			if(writer!=null){
				writer.close();
			}
		}
		
		return true;
	}
	/**
	 * 流写入
	 * @param filePath
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static boolean writeFile(String filePath, InputStream is) throws IOException {

		if (null == filePath || filePath.length() < 1) {
			Log.e("","Invalid param. filePath: " + filePath);
			return false;
		}		
		FileOutputStream os=null;
		try {
		    File file = new File(filePath);
		    if(file.exists()){
		    	deleteDirectory(filePath);
		    }
		       
	        os = new FileOutputStream(file);
		    byte[] buf = new byte[1024];
			int c = is.read(buf);
			while(-1 != c) {
				os.write(buf, 0, c);
				c = is.read(buf);
			}	
			os.flush();
		}finally{
			if(is!=null){
				is.close();
			}
			if(os!=null){
				os.close();
			}
		}
		
		return true;
	}
	/**
	 * 最后修改时间
	 * @param filePath
	 * @return
	 */
	public static long getFileLastModifyTime(String filePath){
		if(null==filePath){
			return 0;
		}
		File file=new File(filePath);
		if(file==null||!file.exists()){
			return 0;
		}
		return file.lastModified();
	}
	
}