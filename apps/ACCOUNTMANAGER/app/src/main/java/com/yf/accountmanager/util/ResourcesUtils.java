package com.yf.accountmanager.util;

import android.graphics.drawable.Drawable;

public class ResourcesUtils {
	public static int getDimension(int id){
		return (int)CommonUtils.context.getResources().getDimension(id);
	}
	public static String getString(int id){
		return CommonUtils.context.getResources().getString(id);
	}
	public static String[] getStringArray(int id){
		return CommonUtils.context.getResources().getStringArray(id);
	}
	public static Drawable getDrawable(int id){
		return CommonUtils.context.getResources().getDrawable(id);
	}
}
