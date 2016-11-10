package com.what.yunbao.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 订单详情
 */
public class OrderMoreJSONParser {

	public AppOrder parse(JSONObject jObject) throws JSONException{
		JSONArray jOrders = null;
		jOrders = jObject.getJSONArray("detailList");		
		return getOrders(jOrders);
	}
	
	private AppOrder getOrders(JSONArray jOrders) throws JSONException{
		AppOrder order = new AppOrder();
		JSONObject jOrder = null;
		jOrder = (JSONObject) jOrders.get(0);
		
		String orderTime = jOrder.optString("orderTime").replace("T","  ");
		order.setOrderTime(orderTime);
		order.setPhone(jOrder.optString("phone"));
		order.setStatus(jOrder.optInt("status"));
		order.setAddress(jOrder.optString("address"));
		order.setOrderNumber(jOrder.optString("orderNumber"));
		
		return order;
	} 
}
