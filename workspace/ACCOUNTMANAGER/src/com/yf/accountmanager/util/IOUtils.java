package com.yf.accountmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.yf.accountmanager.common.IConstants;

public class IOUtils {
	public static final int BUFFER_LENGTH = 1024;

	public static void writeStringToFile(String str, File file)
			throws Exception {
		writeToFile(str.getBytes(IConstants.PLATFORM_ENCODING), file);
	}

	public static void writeToFile(byte[] content, File file) throws Exception {
		if (file.exists())
			file.delete();
		writeToOutputStream(content, new FileOutputStream(file,false));
	}
	
	public static void writeToOutputStream(byte[] content,OutputStream os) throws Exception{
		if(content==null||content.length==0||os==null) return ;
		try{
			os.write(content);
			os.flush();
		}finally{
			os.close();
		}
	}
	
	
	public static String readStringFromFile(File file) throws Exception{
		return new String(readFromFile(file),IConstants.PLATFORM_ENCODING);
	}
	
	
	public static byte[] readFromFile(File file) throws Exception {
		return readFromInputStream(new FileInputStream(file));
	}
	
	public static byte[] readFromInputStream(InputStream is) throws Exception{
		if(is==null) return null;
		byte[] content = new byte[0];
		try {
			byte[] buf = new byte[BUFFER_LENGTH];
			int count,length;
			while((count=is.read(buf))!=-1){
				length=content.length;
				content=Arrays.copyOf(content, length+count);
				System.arraycopy(buf, 0, content,length	,count);
			}
		} finally {
			is.close();
		}
		return content;
	}
}
