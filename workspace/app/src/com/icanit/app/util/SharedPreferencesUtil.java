package com.icanit.app.util;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.icanit.app.entity.AppCommunity;

public class SharedPreferencesUtil {
	public static final String LOGININFO="loginInfo";
	public static final String COMMUNITY_RESERVED="communityReserved";
	public static final String UNCOMPLETED_FILE_LENGTH_INFO="uncompletedFileLengthInfo";
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
	public void put(String file,String key,String value){
		file=convertFileToDefault(file);
		SharedPreferences sp=context.getSharedPreferences(file, 0);
		Editor editor=sp.edit();
		editor.putString(key,value);
		editor.commit();
	}
	public  void remove(String file,String key){
		file=convertFileToDefault(file);
		SharedPreferences sp=context.getSharedPreferences(file, 0);
		Editor editor=sp.edit();
		editor.remove(key);
		editor.commit();
	}
	public String get(String file,String key){
		file=convertFileToDefault(file);
		SharedPreferences sp=context.getSharedPreferences(file, 0);
		return sp.getString(key,null);
	}
	public void put(String file,Map<String,Object> map){
		file=convertFileToDefault(file);
		SharedPreferences sp=context.getSharedPreferences(file, 0);
		Editor editor=sp.edit();
		for(Entry<String,Object> en:map.entrySet()){
			editor.putString(en.getKey(), en.getValue()+"");
		}
		editor.commit();
	}
	public void clear(String file){
		file=convertFileToDefault(file);
		SharedPreferences sp = context.getSharedPreferences(file, 0);
		sp.edit().clear().commit();
	}
	public boolean contains(String file,String key){
		file=convertFileToDefault(file);
		return context.getSharedPreferences(file, 0).contains(key);
	}
	public Map<String,Object> getAll(String file){
		file=convertFileToDefault(file);
		return (Map<String,Object>)context.getSharedPreferences(file,0).getAll();
	}
	private String  convertFileToDefault(String file){
		if(file==null||"".equals(file))
			return LOGININFO;
		return file;
	}
	public AppCommunity getReservedCommunityInfo(){
		SharedPreferences share=context.getSharedPreferences(COMMUNITY_RESERVED, 0);
		Log.d("sharePreferences",share.toString()+"  @SharedPreferencesUtil  getReservedCommunityInfo");
		if(!share.contains("id"))return null;
		AppCommunity comm = new AppCommunity();
		comm.cityName=share.getString("cityName","");
		comm.commName=share.getString("commName","");
		comm.location=share.getString("location","");
		comm.id=Integer.parseInt(share.getString("id","0"));
		comm.latitudeE6=Integer.parseInt(share.getString("latitudeE6","0"));
		comm.longitudeE6=Integer.parseInt(share.getString("longitudeE6","0"));
		return comm;
	}
	public boolean hasReservedCommunityInfo(){
		SharedPreferences share=context.getSharedPreferences(COMMUNITY_RESERVED, 0);
		boolean b = share.contains("id");
		Log.d("shareInfo","hasReservedCommunityInfo="+b+"  @SharePreferencesUtil");
		return b;
	}
	public void reserveCommunityInfo(AppCommunity info){
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
	
	
}
