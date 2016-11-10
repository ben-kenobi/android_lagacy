package com.what.weibo;



public interface Constants {
	//新浪微博=====================
	
//	public static final String SINA_APP_KEY="3026810927";
//
//	public static final String SINA_APP_KEY_SEC = "4cb7f66d843b7fc956595ce0d35ca134";
//	
//	//替换为开发者REDIRECT_URL
//	public static final String SINA_REDIRECT_URL = "http://www.google.hk";
	public static final String SINA_APP_KEY="3225948249";

	public static final String SINA_APP_KEY_SEC = "3f7acf7a42a8747161e14fbe11f9f192";
	
	//替换为开发者REDIRECT_URL
	public static final String SINA_REDIRECT_URL = "http://183.250.110.72:8443/appserver/";
	    
	//新支持scope 支持传入多个scope权限，用逗号分隔
	public static final String SINA_SCOPE = "email,direct_messages_read,direct_messages_write," +
			"friendships_groups_read,friendships_groups_write,statuses_to_me_read," +
				"follow_app_official_microblog";
	
	public static final String CLIENT_ID = "client_id";
	
	public static final String RESPONSE_TYPE = "response_type";
	
	public static final String USER_REDIRECT_URL = "redirect_uri";
	
	public static final String DISPLAY = "display";
	
	public static final String USER_SCOPE = "scope";
	
	public static final String PACKAGE_NAME = "packagename";
	
	public static final String KEY_HASH = "key_hash";
	
	//腾讯微博=====================

//	public static final String TECENT_APP_KEY = "801394488";
//	
//	public static final String TECENT_APP_KEY_SEC = "eb4c7fcb5694ce966aa81107f1463309";
//
//	public static final String TECENT_REDIRECT_URL = "http://www.sina.com.cn";  
	
	

	public static final String TECENT_APP_KEY = "801420202";
	
	public static final String TECENT_APP_KEY_SEC = "cf8cff383391d1966acd6bd87ae5e82c";

	public static final String TECENT_REDIRECT_URL = "http://183.250.110.72:8443/appserver/";  
	
	
	
}
