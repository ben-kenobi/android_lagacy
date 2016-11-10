package com.icanit.app_v2.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public interface IConstants {
	public static final ExecutorService THREAD_POOL=Executors.newFixedThreadPool(5);
	public static final Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());
	String APP_PACKAGE="com.icanit.app";
	String APP_NEWVERSION_FILE = "_appnewversion.apk";
	String RESPONSE_SUCCESS="success";
	String RESPONSE_CODE="responseCode";
	String RESPONSE_DESC="responseDesc";
	String LEAD_TO_MERCHANT_LIST="leadToMerchantList";
	String URL_PREFIX="http://183.250.110.72:8443/as/";
//	String URL_PREFIX="http://192.168.120.228:8090/as/";
//	String URL_PREFIX="http://192.168.120.108:8080/as/";
//	String IMAGE_PREFIX="http://192.168.120.108:8080/news_manage/image/goods/entity_img/150x150/";
	String IMAGE_PREFIX="http://183.250.110.72:8443/as/images/";
	String SERVICE_FACTORY="serviceFactory";
	String SERVICE_FACTORY_GETINSTANCE="getInsta" +
			"nce";
//	String CHOOSED_CITY_NAME="choosedCityName";
	String MERCHANDISE_INFO="merchandiseInfo";
	String STORE_INFO="storeInfo";
	String CART_ITEM_LIST="cartItemList";
	String TRANSMISSION_CHARSET="UTF-8";
	String COMMUNITY_INFO="communityInfo";
	String AFTER_COMMUNITY_CHANGE="afterCommunityChange";
	String AFTER_PAY="afterPay";
//	String LOGIN_USER="loginUser";
	String AFTER_LOGIN="afterLogin";
	String AUTO_LOGIN="autoLogin";
	String PHONE="phone";
	String PASSWORD="password";
	String USERNAME="username";
	String VERIFY_AFTER_LOGIN="verifyAfterLogin";
	String FORWARD_TO_PAY="forwardToPay";
	String MERCHANT_KEY="merchantKey";
	String USER_CONTACT="userContact";
	String GOODS_KEY="goodsKey";
	String DESTINATION_AFTER_REGISTER="targetClassAfterRegister";
	String DESTINATION_AFTER_RETRIEVEPASSWORD="targetClassAfterRetrievePassword";
	String ORDER_INFO="orderInfo";
	String BACK_CLASS="backClass";
	String CUSTOM_SERVICE="18960897429";
	String VERI_CODE="veriCode";
	 String ROOTAREAID="013001";
	//下载包保存路径
	String FILE_PATH ="file/";
			//下载路径
	String APP_DOWNLOAD_PATH=IConstants.URL_PREFIX
					+ UriConstants.NEW_VERSION_APP_DOWNLOAD;
}
