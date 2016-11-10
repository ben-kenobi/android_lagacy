package fj.swsk.cn.eqapp.util;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.main.Common.PushUtils;

public class SharePrefUtil {
	public static final String RESERVED_DIMEN="user_def";
	public static final String USER_INFO="userInfo";
	public static final String USERS="users";
	
	

	
	public static boolean put(String key,String value){
		Editor editor = CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).edit();
		editor.putString(key,value);
		return editor.commit();
	}
	
	public static String get(String key,String defau){
		return CommonUtils.context.getSharedPreferences(RESERVED_DIMEN, 0).getString(key, defau);
	}



	public static boolean putByUser(String key,String value){
		if(TextUtils.isEmpty(PushUtils.userName)){
			return false;
		}
		Editor editor = CommonUtils.context.getSharedPreferences(PushUtils.userName, 0).edit();
		editor.putString(key,value);
		return editor.commit();
	}

	public static String getByUser(String key,String defau){
		if(TextUtils.isEmpty(PushUtils.userName)){
			return "";
		}
		return CommonUtils.context.getSharedPreferences(PushUtils.userName, 0).getString(key, defau);
	}

	public static boolean saveUser(String username,String pwd){
		SharedPreferences.Editor editor = CommonUtils.context.getSharedPreferences(USER_INFO, 0).edit();
		editor.putString("USER_NAME", username);
		editor.putString("PASSWORD", pwd);
		saveInUsers(username,pwd);
		return editor.commit();
	}
	public static String getPwd(){
		return CommonUtils.context.getSharedPreferences(USER_INFO, 0).getString("PASSWORD", "");
	}
	public static String getUsername(){
		return CommonUtils.context.getSharedPreferences(USER_INFO, 0).getString("USER_NAME","");

	}
	public static boolean saveInUsers(String name,String pwd){
		SharedPreferences.Editor editor = CommonUtils.context.getSharedPreferences(USERS, 0).edit();
		editor.putString(name, pwd);
		return editor.commit();
	}
	public static List<String[]> getUsers(){
		List<String[]> usernames = new ArrayList<>();
		Map<String,String> map=(Map<String,String>)CommonUtils.context.getSharedPreferences(USERS, 0).getAll();
		usernames=new ArrayList<>();
		for (String key:map.keySet()){
			usernames.add(new String[]{key,map.get(key)});
		}
		return usernames;
	}

}
