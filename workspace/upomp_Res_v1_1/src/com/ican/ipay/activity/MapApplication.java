package com.ican.ipay.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;

import com.ican.ipay.util.AppUtil;

public class MapApplication  extends Application{
	private Map<String, Object> map = new HashMap<String, Object>();
	
	public void put(String key, Object value) {
		map.put(key, value);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void onCreate() {
		super.onCreate();
		AppUtil.appContext = this;
		// loadShoppingCartInfo();
	}
}
