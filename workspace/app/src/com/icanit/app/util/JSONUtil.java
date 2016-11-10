package com.icanit.app.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final  class JSONUtil {
	private static JSONUtil self = new JSONUtil();
	private JSONUtil(){}
	public static JSONUtil getInstance(){
		return self;
	}
	public static  void jsonArrayToList(JSONArray ja) throws JSONException{
		
	}
	public static  void jsonObjectToList(JSONObject jo,List list) throws JSONException{
		
	}
}
