package com.icanit.app;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.app.Application;
import android.util.Log;

import com.icanit.app.entity.CartItem;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;

public class MapApplication extends Application {
	private Map<String,Object> map=new HashMap<String,Object>();
	public Map<Integer,CartItem> shoppingCartMap;
	public Set<Integer> reservedProdIdSet;
	public List<CartItem> shoppingCartList;
	public void put(String key,Object	value){
		map.put(key,value);
	}
	public Object get(String key){
		return map.get(key);
	}
	public void remove(String key){
		map.remove(key);
	}
	@Override
	public void onCreate() {
		super.onCreate();
		AppUtil.appContext=this;
		try {
			shoppingCartList=AppUtil.getServiceFactory().getShoppingCartDaoInstance(this).
					findAllItemsByPhone(AppUtil.getLoginPhoneNum());
			shoppingCartMap=listToMap(shoppingCartList);
			reservedProdIdSet=new HashSet(shoppingCartMap.keySet());
			Log.w("appInfo","shoppingCart="+shoppingCartMap+"\nprodIdSet="+reservedProdIdSet+" @MapApplication onCreate");
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	private Map<Integer,CartItem> listToMap(List<CartItem> items){
		Map<Integer,CartItem> map = new HashMap<Integer,CartItem>();
		CartItem item;
		for(int i=0;i<items.size();i++){
			 item = items.get(i);
			map.put(item.prod_id, item);
		}
		return map;
	}
}
