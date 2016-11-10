package com.what.yunbao.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 订单详情
 */
public class OrderJSONParser {

	public List<AppOrder> parse(JSONObject jObject) throws JSONException{
		JSONArray jOrders = null;
		jOrders = jObject.getJSONArray("orderList");		
		return getOrders(jOrders);
	}
	
	private List<AppOrder> getOrders(JSONArray jOrders) throws JSONException{
		int orderCount = jOrders.length();
		AppOrder order = null;
		List<AppOrder> orderList = new ArrayList<AppOrder>();		

		for(int i=0; i<orderCount;i++){
			order = getOrderItem((JSONObject)jOrders.get(i));
			orderList.add(order);			
		}
		
		return orderList;
	} 
	private AppOrder getOrderItem(JSONObject jOrder) throws JSONException{
		AppOrder order = new AppOrder();
		order.setAddress(jOrder.optString("address"));
		order.setSum(jOrder.optInt("sum"));
		String orderTime = jOrder.optString("orderTime").replace("T","  ");
		order.setOrderTime(orderTime);
		order.setId(jOrder.optLong("id"));
		order.setCount(jOrder.optString("count"));
		return order;
	} 
}
