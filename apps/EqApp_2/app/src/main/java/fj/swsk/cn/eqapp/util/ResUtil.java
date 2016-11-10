package fj.swsk.cn.eqapp.util;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

public class ResUtil {
	public static final Typeface CSIMP = Typeface.createFromAsset(CommonUtils.context.getAssets(),
			"csimp.TTF");
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
	public static float dp2p(float dp){
		return DeviceInfoUtil.getScreenDensity()*dp;
	}
	public static int dp2Intp(float dp){
		return (int)(DeviceInfoUtil.getScreenDensity()*dp);
	}

}
