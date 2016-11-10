package com.icanit.bdmapversion2.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.icanit.bdmapversion2.entity.Merchant;

/*
 * 商户列表
 */
public class CommunityMarkJSONParser {
	/** Receives a JSONObject and returns a list */
	public List<Merchant> parse(JSONObject jObject){		
		
		JSONArray jMercs = null;
		try {			
			/** Retrieves all the elements in the 'merchants' array */
			jMercs = jObject.getJSONArray("mercList");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getMercs(jMercs);
	}
	
	
	private List<Merchant> getMercs(JSONArray jMercs){
		int mercsCount = jMercs.length();
		List<Merchant> mercsList = new ArrayList<Merchant>();
		Merchant merc = null;
		/** Taking each merchant, parses and adds to list object */
		for(int i=0; i<mercsCount;i++){
			try {
				/** Call getMerchant with merchant JSON object to parse the merchant */
				merc = getMerc((JSONArray)jMercs.get(i));
				mercsList.add(merc);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return mercsList;
	}
	
	/** Parsing the Merchant JSON object */
	private Merchant getMerc(JSONArray jMerc){

		Merchant merc = new Merchant();
		long id = 0;//商户id
		int typeId = 0;//商户所属类别id代号
		String merName = "";//商户名称
		String map;//商户纬经坐标
		double minCost = 0;//商户最低消费
		String detail = "";//商户描述
		GeoPoint geoPoint;//商户纬经坐标换算
		long commuId = 1;//商户所属社区id		

		try {
			//服务器端返回的list<Object[]>解析
			//merc.typeId,merc.merName,merc.map,merc.detail,merc.minCost,merc.id,commuMerc.id.commId
			typeId = jMerc.optInt(0);
			merName = jMerc.optString(1);
			map = jMerc.optString(2);
			detail = jMerc.optString(3);
			minCost = jMerc.optDouble(4);
			id = jMerc.optLong(5);
			commuId = jMerc.optLong(6);
			//一个是null值，一个是字符串对象"null"
			if("null" == map){
				geoPoint = new GeoPoint(0,0);
			}else{
				String[] arr = map.split(",");
				geoPoint = new GeoPoint(Integer.valueOf(arr[0]),Integer.valueOf(arr[1]));
			}			
			merc.setGeoPoint(geoPoint);
			
			merc.setTypeId(typeId);
			merc.setMerName(merName);
			merc.setMinCost(minCost);
			merc.setDetail(detail);
			merc.setId(id);
			merc.setCommuId(commuId);
			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}		
		return merc;
	}
}
