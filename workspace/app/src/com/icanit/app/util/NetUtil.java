
package com.icanit.app.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import android.os.Environment;
import android.util.Log;

import com.icanit.app.HomePageActivity.Downloader;
import com.icanit.app.WelcomePageActivity.Downloader_v2;
import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;

public final class NetUtil {
	public static String targetUri = "appdownload.action";
	public static HttpParams httpParams =new BasicHttpParams();
	private static NetUtil self = new NetUtil();
	static{
		HttpConnectionParams.setConnectionTimeout(httpParams, 10*1000);
		HttpConnectionParams.setSoTimeout(httpParams, 10*1000);
		Log.w("netInfo","#connectionTimeout="+HttpConnectionParams.getConnectionTimeout(httpParams)+"  @NetUtil");
		Log.w("netInfo","#soTimeout="+HttpConnectionParams.getSoTimeout(httpParams)+"  @NetUtil");
		
	}
	private NetUtil() {
	}
	
	public HttpURLConnection getUrlConnection(String uri) throws Exception{
		URL url = new URL(IConstants.URL_PREFIX+uri);
		Log.d("netInfo","host="+url.getHost()+",port="+url.getPort()+",path="+url.getPath()+
				",authority="+url.getAuthority()+",defaultPort="+url.getDefaultPort()+
				",file="+url.getFile()+",protocol="+url.getProtocol()+
				",query="+url.getQuery()+",ref="+url.getRef()+",userInfo="+url.getUserInfo()+
				"   @URLTestActivity");
		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setRequestMethod("GET");
		return connection;
	}
	public File appBreakPointDownload(Downloader downloader) throws Exception{
		InputStream is=null ;
		FileOutputStream fos=null;
		HttpURLConnection connection =null;
		try {
			downloader.active=true;
//			File file = AppUtil.getNetUtilInstance().downloadApp();
			File file = new File(Environment.getExternalStorageDirectory(),
					downloader.newVersionName+IConstants.APP_NEWVERSION_FILE);
			connection = AppUtil.getNetUtilInstance().getUrlConnection(UriConstants.NEW_VERSION_APP_DOWNLOAD);
			long contentLength;
			if(file.length()>0){
				connection.setRequestProperty("Range", "bytes="+file.length()+"-"+(contentLength=AppUtil.
						getSharedPreferencesUtilInstance().getUncompletedFileBytes(file.getName())));
			}else{
				AppUtil.getSharedPreferencesUtilInstance().putUncompletedFileBytes(file.getName(), 
						contentLength=connection.getContentLength());
			}
			 is = connection.getInputStream();
			fos = new FileOutputStream(file,true);
			byte[] buf = new byte[1024]; 
			downloader.pd.setMax((int)contentLength);
			int count;long completed=file.length();
			downloader.pd.setProgress((int)completed);
			while(downloader.active&&(count=is.read(buf))!=-1){
				fos.write(buf, 0, count);
				completed+=count;
				downloader.pd.setProgress((int)completed);
			}
			fos.flush();
			return file;
		}finally{
			if(is!=null)is.close();
			if(fos!=null)fos.close();
			if(connection!=null)connection.disconnect();
		}
	}
	
	public File appBreakPointDownload(Downloader_v2 downloader) throws Exception{
		InputStream is=null ;
		FileOutputStream fos=null;
		HttpURLConnection connection =null;
		try {
			downloader.active=true;
//			File file = AppUtil.getNetUtilInstance().downloadApp();
			File file = new File(Environment.getExternalStorageDirectory(),
					downloader.newVersionName+IConstants.APP_NEWVERSION_FILE);
			connection = AppUtil.getNetUtilInstance().getUrlConnection(UriConstants.NEW_VERSION_APP_DOWNLOAD);
			long contentLength;
			if(file.length()>0){
				connection.setRequestProperty("Range", "bytes="+file.length()+"-"+(contentLength=AppUtil.
						getSharedPreferencesUtilInstance().getUncompletedFileBytes(file.getName())));
			}else{
				AppUtil.getSharedPreferencesUtilInstance().putUncompletedFileBytes(file.getName(), 
						contentLength=connection.getContentLength());
			}
			 is = connection.getInputStream();
			fos = new FileOutputStream(file,true);
			byte[] buf = new byte[1024]; 
			downloader.pd.setMax((int)contentLength);
			int count;long completed=file.length();
			downloader.pd.setProgress((int)completed);
			while(downloader.active&&(count=is.read(buf))!=-1){
				fos.write(buf, 0, count);
				completed+=count;
				downloader.pd.setProgress((int)completed);
			}
			fos.flush();
			return file;
		}finally{
			if(is!=null)is.close();
			if(fos!=null)fos.close();
			if(connection!=null)connection.disconnect();
		}
	
	}
	

	public String sendMessageWithHttpGet(String uri, String message)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		HttpGet request = new HttpGet(IConstants.URL_PREFIX + uri + "?"
				+ message);
		Log.i("httpGetInfo", uri + "?" + message + " @NetUtil  request");
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		String responseText = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
		Log.i("httpGetInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		return responseText;
	}
	public String sendMessageWithHttpPost(String uri, Map<String,Object> params)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		HttpPost request = new HttpPost(IConstants.URL_PREFIX + uri);
		if(params!=null&&!params.isEmpty()){
			List<NameValuePair> ls = new ArrayList<NameValuePair>();
			for(Entry<String,Object> en:params.entrySet()){
				ls.add(new BasicNameValuePair(en.getKey(),en.getValue().toString()));
			}
			request.setEntity(new UrlEncodedFormEntity(ls,HTTP.UTF_8));
		}
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		String responseText = EntityUtils.toString(response.getEntity());
		Log.i("netInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		return responseText;
	}
	
	public String sendMessageWithHttpPost(String uri, List<NameValuePair> params)
			throws Exception {
		if(!DeviceInfoUtil.isNetworkConnected()) return null;
		HttpPost request = new HttpPost(IConstants.URL_PREFIX + uri);
		if(params!=null&&!params.isEmpty())
		request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		String responseText = EntityUtils.toString(response.getEntity());
		Log.i("netInfo", "@json responseText:" + responseText
				+ " @NetUtil  responseText");
		return responseText;
	}
	
	public void noticeServerAfterPay(String orderId) throws Exception{
		sendMessageWithHttpGet(UriConstants.NOTICE_AFTER_PAY, 
				"orderNum="+orderId);
	}
	
	
	//download apk file  for update
	public File downloadApp() throws Exception {
		HttpPost request = new HttpPost(IConstants.URL_PREFIX + targetUri);
		List<NameValuePair> ls = new ArrayList<NameValuePair>();
		ls.add(new BasicNameValuePair("name", "Ñî·å"));
		ls.add(new BasicNameValuePair("phone", "¾­³£¸ü»»"));
		ls.add(new BasicNameValuePair("phone", "¿ÅÁ£¼Á"));
		request.setEntity(new UrlEncodedFormEntity(ls, HTTP.UTF_8));
		HttpResponse response = new DefaultHttpClient(httpParams).execute(request);
		Log.d("netInfo", "responseStatus="
				+ response.getStatusLine().getStatusCode() + "@NetUtil");
		if (200 == response.getStatusLine().getStatusCode()) {
			HttpEntity entity = response.getEntity();
			InputStream is = entity.getContent();
			Log.d("netInfo",
					"permission="
							+ android.os.Environment.getExternalStorageState()
							+ ",path="
							+ Environment.getExternalStorageDirectory()
									.getPath() + ",available=" + is.available()
							+ "  @NetUtil");
			if (is != null) {
				FileOutputStream fos = null;
				try {
					byte[] buf = new byte[1024 * 10];
					File file = new File(
							Environment.getExternalStorageDirectory(), IConstants.APP_NEWVERSION_FILE);
					fos = new FileOutputStream(file);
					int count;
					while ((count = is.read(buf)) != -1) {
						fos.write(buf, 0, count);
					}
					fos.flush();
					return file;
				} finally {
					if (fos != null)
						fos.close();
					is.close();
				}
			}
		}
		return null;
	}

	public static NetUtil getInstance() {
		return self;
	}
}
