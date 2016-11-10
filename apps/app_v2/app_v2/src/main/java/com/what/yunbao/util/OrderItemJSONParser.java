package com.what.yunbao.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * 订单item列表
 */
public class OrderItemJSONParser {

	public List<AppOrderItems> parse(JSONObject jObject) throws JSONException{
		JSONArray jOrderItems = null;
		try {			
			jOrderItems = jObject.getJSONArray("itemList");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getOrderItems(jOrderItems);
	}
	
	private List<AppOrderItems> getOrderItems(JSONArray jOrderItems) throws JSONException{
		int orderItemsCount = jOrderItems.length();
		AppOrderItems orderItem = null;
		List<AppOrderItems> orderItemsList = new ArrayList<AppOrderItems>();		

		for(int i=0; i<orderItemsCount;i++){
			orderItem = getOrderItem((JSONObject)jOrderItems.get(i));
			orderItemsList.add(orderItem);
		}
		
		return orderItemsList;
	} 
	
	private AppOrderItems getOrderItem(JSONObject jOrderItem){
		AppOrderItems order = new AppOrderItems();
		order.setGoodId(jOrderItem.optLong("goodId"));
		order.setMerId(jOrderItem.optInt("merId"));
		order.setGoodName(jOrderItem.optString("goodName"));
		order.setSum(jOrderItem.optDouble("sum"));
		order.setQuantity(jOrderItem.optInt("quantity"));		
		return order;
	}
}
