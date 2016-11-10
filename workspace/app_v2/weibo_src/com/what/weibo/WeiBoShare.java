package com.what.weibo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.weibo.sdk.android.api.util.Util;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class WeiBoShare {
	//Sina
	private static Oauth2AccessToken accessToken;
	
	//Tecent
	private static String tecent_accessToken;
	
	/**
	 * 分享到新浪微博
	 * @param context
	 * @param 内容
	 * @param 图片地址(需高级权限)
	 */
	public static void shareToSina(Context context,String content,String picUrl){
		accessToken = AccessTokenKeeper.readAccessToken(context);
		if (accessToken.isSessionValid()) {
			Bundle bundle = new Bundle();
			bundle.putString("title", "分享到新浪微博");
			bundle.putString("picUrl", picUrl);			
			bundle.putString("content", content);
			context.startActivity(new Intent(context,WeiBoRequestMessageActivity.class).putExtras(bundle));
		}else{
			Weibo mWeibo = Weibo.getInstance(Constants.SINA_APP_KEY, Constants.SINA_REDIRECT_URL,Constants.SINA_SCOPE);
			SsoHandler mSsoHandler = new SsoHandler((Activity) context, mWeibo);
            mSsoHandler.authorize(new AuthDialogListener(context),null);          
		}
	}
	/**
	 * 分享到腾讯微博
	 * @param context
	 * @param 内容
	 * @param 图片地址
	 * @param 其他内容(暂时不需要)
	 */
	public static void shareToTecent(Context context,String content,String picUrl,String others){
		tecent_accessToken = Util.getSharePersistent(context, "ACCESS_TOKEN");
		if("".equals(tecent_accessToken)){
			auth(context,Integer.valueOf(Constants.TECENT_APP_KEY), Constants.TECENT_APP_KEY_SEC);
			
		}else{
			Bundle bundle = new Bundle();
			bundle.putString("title", "分享到腾讯微博");
			bundle.putString("picUrl", picUrl);
			bundle.putString("content",content);
			context.startActivity(new Intent(context,WeiBoRequestMessageActivity.class).putExtras(bundle));
		}
	}
	/**
     * 新浪微博授权
     */
    private static class AuthDialogListener implements WeiboAuthListener {
    	Context mContext;
    	JSONObject o; 
    	OutputStreamWriter out = null;
    	InputStream l_urlStream = null;
    	public AuthDialogListener(Context context){
    		mContext = context;
    	}
        @Override
        public void onComplete(Bundle values) {        	            
        	String code = values.getString("code");  
              
            try {  
                URL url = new URL("https://api.weibo.com/oauth2/access_token");  
                URLConnection connection = url.openConnection();  
                connection.setDoOutput(true);  
                out = new OutputStreamWriter(connection    
                        .getOutputStream(), "utf-8");  
                out.write("client_id="+Constants.SINA_APP_KEY+"&client_secret="+Constants.SINA_APP_KEY_SEC+"&grant_type=authorization_code" +  
                        "&code="+code+"&redirect_uri="+Constants.SINA_REDIRECT_URL);  
                out.flush();    
                out.close();   
                String sCurrentLine;    
                String sTotalString;    
                sCurrentLine = "";    
                sTotalString = "";                       
                l_urlStream = connection.getInputStream();    
                BufferedReader l_reader = new BufferedReader(new InputStreamReader(    
                        l_urlStream));    
                while ((sCurrentLine = l_reader.readLine()) != null) {    
                    sTotalString += sCurrentLine;    
                }  
                l_urlStream.close(); 
                o = new JSONObject(sTotalString); 
                Log.e("values:", values.toString());
                  
            } catch (Exception e) {  
                e.printStackTrace();  
                
            } finally{
            	try {
                	if(out!=null){
    					out.flush();
    					out.close();
                	}
                	if(l_urlStream!=null){
                		l_urlStream.close();
                	}
				} catch (IOException e1) {
				}
            }            
            
          try {
        	  //更新新浪accessToken
        	  accessToken = new Oauth2AccessToken(o.getString("access_token"), o.getString("expires_in"));
		
	          if (accessToken.isSessionValid()) {
	              AccessTokenKeeper.keepAccessToken(mContext,accessToken);
	              Toast.makeText(mContext, "认证成功", Toast.LENGTH_SHORT).show();	
	              Bundle bundle = new Bundle();
	  			  bundle.putString("picUrl", "");
	  			  bundle.putString("title", "分享到新浪微博");
	  			  bundle.putString("content", "");
	  			  mContext.startActivity(new Intent(mContext,WeiBoRequestMessageActivity.class).putExtras(bundle));
	          }
            
          } catch (JSONException e) {
  			e.printStackTrace();
  		  }
     
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(mContext,
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(mContext, "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mContext,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
    
    /**
     * 腾讯微博授权
     * @param appid
     * @param app_secket
     */
    private static void auth(final Context context, long appid, String app_secket) {
		
		AuthHelper.register(context, appid, app_secket, new OnAuthListener() {

			@Override
			public void onWeiBoNotInstalled() {
//				Toast.makeText(MainActivity.this, "onWeiBoNotInstalled", 1000)
//						.show();
				Intent i = new Intent(context,Authorize.class);
				i.putExtra("from", "shareGUI");
				context.startActivity(i);
			}

			@Override
			public void onWeiboVersionMisMatch() {
//				Toast.makeText(MainActivity.this, "onWeiboVersionMisMatch",
//						1000).show();
				Intent i = new Intent(context,Authorize.class);
				i.putExtra("from", "shareGUI");
				context.startActivity(i);
			}

			@Override
			public void onAuthFail(int result, String err) {
				Toast.makeText(context, "result : " + result, 1000)
						.show();
			}

			@Override
			public void onAuthPassed(String name, WeiboToken token) {
				Toast.makeText(context, "passed", 1000).show();
			}

		});
		AuthHelper.auth(context, "");

	}
}
