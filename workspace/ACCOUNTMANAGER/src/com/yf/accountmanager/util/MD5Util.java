package com.yf.accountmanager.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5Util {
	
	public static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'a', 'b', 'c', 'd', 'e', 'f' };

	
	public final static String SGIN="_";
	
	public final static String calcs(String...strs) {
		String tempstr="";
		for (String string : strs) {
			tempstr+=string+SGIN;
		}
		return md5(tempstr);
	}

	public final static String md5(String ss) {

		String s = ss == null ? "" : ss;

		try {
			byte[] strTemp = s.getBytes("utf-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			return toHexString(md);
		} catch (Exception e) {
			return null;
		}
	}
	
	public final static String md5sum(File file){
		if(!FileUtils.isReadableFile(file)) return "";
		FileInputStream fis=null;
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			fis = new FileInputStream(file);
			long length=fis.available();
			int count ;
			byte[] buf =new byte[length>(1024*64)?1024*64:(int)length];
			while((count=fis.read(buf))!=-1){
				md.update(buf, 0, count);
			}
			return toHexStr(md.digest());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fis!=null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return "";
		
	}

	
	public static String toHexString(byte[] bytes){
		
		int j = bytes.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 =bytes[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
	
	public static String toHexStr(byte[] bytes){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<bytes.length;i++){
			sb.append(hexDigits[bytes[i]>>>4&0xf]);
			sb.append(hexDigits[bytes[i]&0x0f]);
		}
		return sb.toString();
	}
	public static void main(String[] args) throws UnsupportedEncodingException {
		// MD5_Test aa = new MD5_Test();

	}
}
