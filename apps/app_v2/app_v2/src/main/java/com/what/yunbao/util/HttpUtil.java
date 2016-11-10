package com.what.yunbao.util;

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

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.UriConstants;

import android.util.Log;

public class HttpUtil {
	private static final int CONNECT_TIMEOUT = 10000;
	public static HttpClient client;
	
	/**
	 * 修改密码
	 * @return
	 */
	public static String changePwd(String phone,String oldPwd,String newPwd){
		String str = "";	
		String url =IConstants.URL_PREFIX+UriConstants.MODIFY_PASSWORD;
		if(client == null){
			client = getThreadSafeClient();
		}		

		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", phone));
		params.add(new BasicNameValuePair("oldpassword",oldPwd));
		params.add(new BasicNameValuePair("newpassword",newPwd));
		
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
