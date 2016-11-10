package com.what.yunbao.util;

import java.util.Iterator;
import java.util.Set;

import android.content.ContentValues;


public class AppUtils {
	public static String getLoginPhoneNum() throws Exception{
		return (String)Class.forName("com.icanit.app_v2.util.AppUtil").getMethod("getLoginPhoneNum", new Class[]{})
				.invoke(null, new Object[]{});
	}
	public static ContentValues createContentValues(String[] columns,String[] values){
		ContentValues cv = new ContentValues();
		for(int i=0;i<columns.length;i++){
			cv.put(columns[i], values[i]);
		}
		return cv;
	}
	public static String concatenateSet(Set set){
		if(set==null||set.isEmpty()) return "";
		StringBuffer sb = new StringBuffer();
		for(Iterator it=set.iterator();it.hasNext();){
			sb.append(it.next()+",");
		}
		return sb.deleteCharAt(sb.length()-1).toString();
	}
}
