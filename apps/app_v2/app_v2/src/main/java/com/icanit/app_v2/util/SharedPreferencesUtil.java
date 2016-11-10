package com.icanit.app_v2.util;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppCommunity;

public class SharedPreferencesUtil {
	public static final String LOGININFO="loginInfo";
	public static final String COMMUNITY_RESERVED="communityReserved";
	public static final String UNCOMPLETED_FILE_LENGTH_INFO="uncompletedFileLengthInfo";
	public static final String SHAREPREF_ADDRESS_PROMPT_STATUS="addressPromptStatus";
	public static final String DEVICE_BASE_PROMPT_STATUS="deviceBasePromptStatus";
	public static final String SHAREPREF_SHORTCUT_PROMPT_STATUS="shortcutPromptStatus";
	private static SharedPreferencesUtil spu;
	private Context context;
	public  static SharedPreferencesUtil getInstance(Context context){
		if(spu==null){
			synchronized (SharedPreferencesUtil.class) {
				if(spu==null)
					spu=new SharedPreferencesUtil(context);
			}
		}
		return spu;
	}
	private SharedPreferencesUtil(Context context){
		this.context=context.getApplicationContext();
	}
//	public void put(String file,String key,String value){
//		SharedPreferences sp=context.getSharedPreferences(file, 0);
//		Editor editor=sp.edit();
//		editor.putString(key,value);
//		editor.commit();
//	}
//	public  void remove(String file,String key){
//		SharedPreferences sp=context.getSharedPreferences(file, 0);
//		Editor editor=sp.edit();
//		editor.remove(key);
//		editor.commit();
//	}
//	public String get(String file,String key){
//		SharedPreferences sp=context.getSharedPreferences(file, 0);
//		return sp.getString(key,null);
//	}
//	public void put(String file,Map<String,Object> map){
//		SharedPreferences sp=context.getSharedPreferences(file, 0);
//		Editor editor=sp.edit();
//		for(Entry<String,Object> en:map.entrySet()){
//			editor.putString(en.getKey(), en.getValue()+"");
//		}
//		editor.commit();
//	}
//	public void clear(String file){
//		SharedPreferences sp = context.getSharedPreferences(file, 0);
//		sp.edit().clear().commit();
//	}
//	public boolean contains(String file,String key){
//		return context.getSharedPreferences(file, 0).contains(key);
//	}
//	public Map<String,Object> getAll(String file){
//		return (Map<String,Object>)context.getSharedPreferences(file,0).getAll();
//	}
	public AppCommunity getReservedCommunityInfo(){
		SharedPreferences share=context.getSharedPreferences(COMMUNITY_RESERVED, 0);
		if(!share.contains("id"))return null;
		AppCommunity comm = new AppCommunity();
		comm.areaId=share.getString("areaId","");
		comm.commName=share.getString("commName","");
		comm.location=share.getString("location","");
		comm.id=share.getString("id","0");
		comm.latitudeE6=Integer.parseInt(share.getString("latitudeE6","0"));
		comm.longitudeE6=Integer.parseInt(share.getString("longitudeE6","0"));
		return comm;
	}
	public boolean hasReservedCommunityInfo(){
		SharedPreferences share=context.getSharedPreferences(COMMUNITY_RESERVED, 0);
		boolean b = share.contains("id");
		return b;
	}
	public void reserveCommunityInfo(AppCommunity info){
		if(info==null) return ;
		SharedPreferences share = context.getSharedPreferences(COMMUNITY_RESERVED, 0);
		Editor editor=share.edit();
		for(Field field:info.getClass().getDeclaredFields()){
			try {
				if("appMerchants".equals(field.getName())) continue;
				editor.putString(field.getName(), field.get(info)+"");
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		editor.commit();
	}
	public void putUncompletedFileBytes(String filename,long bytes){
		SharedPreferences share = context.getSharedPreferences(UNCOMPLETED_FILE_LENGTH_INFO, 0);
		Editor editor = share.edit();
		editor.putLong(filename,bytes);
		editor.commit();
	}
	public long getUncompletedFileBytes(String filename){
		SharedPreferences share = context.getSharedPreferences(UNCOMPLETED_FILE_LENGTH_INFO, 0);
		return share.getLong(filename, 0);
	}
	public String getReservedUserInfo(String key){
		return context.getSharedPreferences(LOGININFO, 0).getString(key, null);
	}
	public void setLoginUserInfo(String key,String value){
		SharedPreferences sp=context.getSharedPreferences(LOGININFO, 0);
		Editor editor=sp.edit();
		editor.putString(key,value);
		editor.commit();
	}
	public void saveLoginUserInfo(String phone,String password,boolean autoLogin){
		SharedPreferences sp=context.getSharedPreferences(LOGININFO, 0);
		Editor editor=sp.edit();
		editor.putString(IConstants.PHONE, phone);
		editor.putString(IConstants.PASSWORD, password);
		editor.putString(IConstants.AUTO_LOGIN, autoLogin+"");
		editor.commit();
	}
	
	public boolean getAddressPromptStatus(String phone){
		SharedPreferences sp=context.getSharedPreferences(SHAREPREF_ADDRESS_PROMPT_STATUS, 0);
		if(phone==null||"".equals(phone)) phone="000";
		//default  action is prompt
		return sp.getBoolean(phone,true);
	}
	public void setAddressPromptStatus(String phone,boolean status){
		SharedPreferences sp=context.getSharedPreferences(SHAREPREF_ADDRESS_PROMPT_STATUS, 0);
		Editor editor=sp.edit();
		if(phone==null||"".equals(phone)) phone="000";
		editor.putBoolean(phone, status);
		editor.commit();
	}
	public boolean getShortcutPromptStatus(){
		SharedPreferences sp = context.getSharedPreferences(DEVICE_BASE_PROMPT_STATUS, 0);
		//default  action is prompt
		return sp.getBoolean(SHAREPREF_SHORTCUT_PROMPT_STATUS,true);
	}
	public void setShortcutPromptStatus(boolean status){
		SharedPreferences sp=context.getSharedPreferences(DEVICE_BASE_PROMPT_STATUS, 0);
		Editor editor=sp.edit();
		editor.putBoolean(SHAREPREF_SHORTCUT_PROMPT_STATUS, status);
		editor.commit();
	}
}
