package swsk.cn.rgyxtq.util;

import android.graphics.drawable.Drawable;

public class ResUtil {
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
	public static int getColor(int id){
		return CommonUtils.context.getResources().getColor(id);
	}
}
