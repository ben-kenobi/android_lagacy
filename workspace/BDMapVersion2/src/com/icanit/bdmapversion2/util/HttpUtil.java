package com.icanit.bdmapversion2.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.baidu.platform.comapi.basestruct.GeoPoint;

public class HttpUtil {
	private static final int CONNECT_TIMEOUT = 10000;
	public static HttpClient client;
	 
	/**
	 * 根据社区id获取商户
	 * 缓存的存取
	 * @param id
	 * @param netWorkState
	 * @return
	 */
	public static String getMercByCommuId(long commuId,String netWorkState){
		String str = "";	
		String url = "http://192.168.137.1:8080/fc/getMercByCommuId";
		//从缓存中获取
		String cacheConfigString = ConfigCache.getUrlCache(url+commuId,netWorkState);
		if(cacheConfigString != null){
			return cacheConfigString;
		}
		if(client == null){
			client = getThreadSafeClient();
		}	
		
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();		
		params.add(new BasicNameValuePair("commuId",String.valueOf(commuId)));	

		try {
			post.setEntity((new UrlEncodedFormEntity(params,HTTP.UTF_8)));
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				str = EntityUtils.toString(response.getEntity());				
				ConfigCache.setUrlCache(url+commuId, str);
			}	
		} catch (IOException e) {
			Log.e("MyHttpUtil...", "10秒无响应.发生异常");
			e.printStackTrace();
		}	
		System.out.println("+++++++"+str);
		return str;
	}	
	/**
	 * 根据当前所在坐标点获取最近社区的商户
	 * @param geoPoint
	 * @return
	 */
	public static String getMercByLocation(GeoPoint geoPoint){
		String str = "";	
		String url = "http://192.168.137.1:8080/fc/getNearbyMercByLoc";
		if(client == null){
			client = getThreadSafeClient();
		}		

		HttpPost post = new HttpPost(url);
		List<NameValuePair> params=new ArrayList<NameValuePair>();		
		params.add(new BasicNameValuePair("latitudeE6",String.valueOf(geoPoint.getLatitudeE6())));
		params.add(new BasicNameValuePair("longitudeE6",String.valueOf(geoPoint.getLongitudeE6())));
		
		try {
			post.setEntity((new UrlEncodedFormEntity(params,HTTP.UTF_8)));
			HttpResponse response = client.execute(post);

			if(response.getStatusLine().getStatusCode() == 200){
				str = EntityUtils.toString(response.getEntity());	
			}	
		} catch (IOException e) {
			Log.e("MyHttpUtil...", "10秒无响应.发生异常");
			e.printStackTrace();
		}	
		System.out.println("+++++++"+str);

		return str;
	}
	/**
	 * 获取社区列表
	 * @return
	 */
	public static String getCommuList(){
		String str = "";	
		String url = "http://192.168.137.1:8080/fc/getCommuList";
		if(client == null){
			getThreadSafeClient();
		}	
		
		HttpPost post = new HttpPost(url);		
		try {			
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){					
				str = EntityUtils.toString(response.getEntity());					
			}	
		} catch (IOException e) {
			Log.e("MyHttpUtil...", "10秒无响应.发生异常");
			e.printStackTrace();
		}	
		System.out.println("+++++++"+str);	
		
		return str;
	}
	
	public static HttpClient getThreadSafeClient()  {

	    client = new DefaultHttpClient();
	    //设置超时
	  	client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
	    ClientConnectionManager mgr = client.getConnectionManager();
	    HttpParams params = client.getParams();
	    client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, 
	            mgr.getSchemeRegistry()), params);
	    return client;
	}
}
