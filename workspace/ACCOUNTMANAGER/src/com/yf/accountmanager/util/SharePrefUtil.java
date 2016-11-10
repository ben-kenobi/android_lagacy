package com.yf.accountmanager.util;

import android.content.SharedPreferences.Editor;

public class SharePrefUtil {
	public static final String RESERVED_DIMEN="reserved_dimen";
	
	
	public static boolean saveFileEditorTextsize(int textsize){
		return putReservedDimen("fileEditorTextsize",textsize);
	}
	
	public static int getFileEditorTextsize(){
		return getReservedDimen("fileEditorTextsize", 17);
	}

	public static boolean  saveFileEditorPagesize(int pagesize){
		return putReservedDimen("fileEditorPagesize",pagesize);
	}
	
	public static int getFileEditorPagesize(){
		return getReservedDimen("fileEditorPagesize", 0);
	}
	
	public static boolean saveFileSystemGridColumnNum(int cnum){
		return putReservedDimen("fileSystemGridColumnNum", cnum);
	}
	
	public static int getFileSystemGridColumnNum(){
		return getReservedDimen("fileSystemGridColumnNum", 5);
	}
	
	public static boolean saveMaxFileSystemSearchResultCount(int count){
		return putReservedDimen("maxFileSystemSearchResultCount", count);
	}
	
	public static int getMaxFileSystemSearchResultCount(){
		return getReservedDimen("maxFileSystemSearchResultCount", 500);
	}
	
	public static boolean putReservedDimen(String key,int value){
		Editor editor = CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	public static int getReservedDimen(String key,int defau){
		return CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).getInt(key, defau);
	}
	
}
