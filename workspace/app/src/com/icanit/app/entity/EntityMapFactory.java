package com.icanit.app.entity;

import java.util.HashMap;
import java.util.Map;

import com.icanit.app.common.IConstants;
import com.icanit.app.service.DataService;

public class EntityMapFactory {
	public static  Map<String,Object> generateEntity(int id0,String title,int num,int id1){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("pic", id0);map.put("title", title);map.put("num", num);
		map.put("arrow", id1);
		return map;
	}
	
	
	public static Map<String,Object> generateUser(String phone,String password){
		Map<String,Object> user = new HashMap<String,Object>();
		user.put(IConstants.PHONE, phone);
		user.put(IConstants.PASSWORD, password);
		return user;
	}
	
}
