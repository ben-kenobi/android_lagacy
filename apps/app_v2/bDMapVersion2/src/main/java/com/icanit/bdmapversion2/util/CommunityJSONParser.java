package com.icanit.bdmapversion2.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.icanit.bdmapversion2.entity.Community;

/*
 * 社区列表
 */
public class CommunityJSONParser {

	/** Receives a JSONObject and returns a list 
	 * @throws JSONException */
	public List<Community> parse(JSONObject jObject) throws JSONException{
		JSONArray jCommus = null;
		/** Retrieves all the elements in the 'commuList' array */
		jCommus = jObject.getJSONArray("commuList");
		return getCommus(jCommus);
	}
	
	private List<Community> getCommus(JSONArray jCommus) throws JSONException{
		int commusCount = jCommus.length();
		Community commu = null;
		List<Community> commusList = new ArrayList<Community>();		
		String [] arr = new String[]{"——社区选择——","离我最近"};
		for(int i=0;i<2;i++){
			commu = new Community();
			commu.setCommunityName(arr[i]); 
			commusList.add(commu);
		}
		for(int i=0; i<commusCount;i++){
			commu = getCommu((JSONObject)jCommus.get(i));
			commusList.add(commu);
		}
		
		return commusList;
	} 
	
	/** Parsing the Community JSON object 
	 * @throws JSONException */
	private Community getCommu(JSONObject jCommu) throws JSONException{
		Community commu = new Community();
		String communityName = "";
		GeoPoint communityGeoPoint = null;
		int latitude = 0;
		int longitude = 0;
		long id = 0;
		
		communityName = jCommu.getString("commName");
		latitude = jCommu.getInt("latitudee6");
		longitude = jCommu.getInt("longitudee6");
		id = jCommu.getLong("id");
		communityGeoPoint = new GeoPoint(latitude,longitude);
		System.out.println("|||||||||||"+id+"<<<"+communityGeoPoint+">>>"+communityName);
		commu.setCommunityName(communityName);
		commu.setCommunityCenterPoint(communityGeoPoint);		
		commu.setId(id);
		
		return commu;
	}
}
