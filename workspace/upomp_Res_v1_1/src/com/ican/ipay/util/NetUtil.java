
package com.ican.ipay.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.ican.ipay.common.IConstants;

public final class NetUtil {
	public static String targetUri = "appdownload.action";
	public static HttpParams httpParams =new BasicHttpParams();
	private static NetUtil self = new NetUtil();
	static{
		HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10*1000);
		
	}
	private NetUtil() {
	}
	

	
	public HttpURLConnection getHttpUrlConnection(String url) throws Exception{
		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection)u.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		return connection;
	}

	
	public String sendMessageWithHttpPost(String uri, String message)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		String url=getUrl(uri)+"?"+message;
		HttpGet request = new HttpGet(url);
		Log.i("httpPostInfo", uri + "?" + message + " @NetUtil  request");
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		String responseText=null;
		if(response.getStatusLine().getStatusCode()==200){
			responseText=EntityUtils.toString(response.getEntity());
		}
		Log.i("netInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		return responseText;
	}
	
	public String sendMessageWithHttpPost(String uri, Map<String,Object> params)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		HttpPost request = new HttpPost(getUrl(uri));
		if(params!=null&&!params.isEmpty()){
			List<NameValuePair> ls = new ArrayList<NameValuePair>();
			for(Entry<String,Object> en:params.entrySet()){
				ls.add(new BasicNameValuePair(en.getKey(),en.getValue().toString()));
			}
			request.setEntity(new UrlEncodedFormEntity(ls,HTTP.UTF_8));
		}
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		HttpEntity entity=response.getEntity();
		String responseText=null;responseText = EntityUtils.toString(entity);
		Log.i("netInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		if(response.getStatusLine().getStatusCode()==200){
			return responseText;
		}
		return null;
	}
	
	public String sendMessageWithHttpPost(String uri, List<NameValuePair> params)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		
		HttpPost request = new HttpPost(getUrl(uri));
		if(params!=null&&!params.isEmpty())
		request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		HttpEntity entity=response.getEntity();
		String responseText=null;responseText = EntityUtils.toString(entity);
		Log.i("netInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		if(response.getStatusLine().getStatusCode()==200){
			return responseText;
		}
		return null;
	}
	
	
	
//	public AppOrder  noticeServerAfterPay(String orderId,String orderTime) throws Exception{
//		String resp=sendMessageWithHttpPost(UriConstants.NOTICE_AFTER_PAY, 
//				"orderNum="+orderId+"&orderTime="+orderTime);
//		if(!TextUtils.isEmpty(resp)&&resp.startsWith("{")){
//			return new Gson().fromJson(resp, AppOrder.class);
//		}
//		return null;
//	}
	private String getUrl(String uri){
		return uri.startsWith("http://")?uri:IConstants.URL_PREFIX+uri;
	}
	
	public static NetUtil getInstance() {
		return self;
	}
}
