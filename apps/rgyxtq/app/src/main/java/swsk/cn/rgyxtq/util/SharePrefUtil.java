package swsk.cn.rgyxtq.util;

import android.content.SharedPreferences.Editor;

public class SharePrefUtil {
	public static final String RESERVED_DIMEN="user_def";
	
	

	
	public static boolean put(String key,String value){
		Editor editor = CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).edit();
		editor.putString(key,value);
		return editor.commit();
	}
	
	public static String get(String key,String defau){
		return CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).getString(key, defau);
	}
	
}
