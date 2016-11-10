package com.icanit.app.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Instrumentation;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.icanit.app.MapApplication;
import com.icanit.app.common.IConstants;
import com.icanit.app.common.UriConstants;
import com.icanit.app.entity.CartItem;
import com.icanit.app.entity.EntityMapFactory;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.service.factory.ServiceFactory;
import com.icanit.app.sqlite.ShoppingCartDao;

public final  class AppUtil {
	public static MapApplication appContext;
	public static ServiceFactory getServiceFactory() throws AppException{
		try {
			return (ServiceFactory)Class.forName(PropertiesConfig.getProperty(IConstants.SERVICE_FACTORY)).
					getMethod(IConstants.SERVICE_FACTORY_GETINSTANCE).invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("创建工厂失败  @AppUtil;",e);
		}
	}
	public static JSONUtil getJSONUtilInstance(){
		return JSONUtil.getInstance();
	}
	public static NetUtil getNetUtilInstance(){
		return NetUtil.getInstance();
	}
	public static ListenerUtil getListenerUtilInstance(){
		return ListenerUtil.getInstance();
	}
	
	public static SharedPreferencesUtil getSharedPreferencesUtilInstance(){
		return SharedPreferencesUtil.getInstance(appContext);
	}
	public static void sendKeyEvent(final int keyCode){
		DataService.THREAD_POOL.submit(new Runnable(){
			public void run(){
				Instrumentation in =new Instrumentation();
				in.sendCharacterSync(keyCode);
			}
		});
	}
	public static void bindBackListener(View view){
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendKeyEvent(KeyEvent.KEYCODE_BACK);
			}
		});
	}
	public static double calculateTotalCost(List<CartItem> items){
		double cost=0;
		for(CartItem item:items){
			cost+=item.present_price*item.quantity;
		}
		return cost;
	}
	
	public static void  bindEditTextNtextDisposer(final EditText editText,final ImageButton textDisposer){
		new ListenerUtil.TextChangeListener(editText, textDisposer);
	}
	public static void  bindSearchTextNtextDisposerNsearchConfirm(final EditText editText,final ImageButton textDisposer,final Button searchConfirmButton){
		new ListenerUtil.TextChangeListener(editText, textDisposer,searchConfirmButton);
	}
	
	public static void putIntoApplication(String key,Object value){
		appContext.put(key,value);
	}
	public static Object getFromApplication(String key){
		return appContext.get(key);
	}
	public static void removeFromApplication(String key){
		appContext.remove(key);
	}
	public static String getLoginPhoneNum(){
		Object obj= appContext.get(IConstants.LOGIN_USER);
		if(obj==null) return ""	;
		return ((Map<String,Object>)obj).get(IConstants.PHONE).toString();
	}
	public static Map<String,Object> getLoginUser(){
		Object obj =appContext.get(IConstants.LOGIN_USER);
		if(obj==null) return null; return (Map<String,Object>)obj;
	}
	
	public static boolean isPhoneNum(String phone){
		return phone.matches("^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$");
	}
	public static boolean isPassword(String password){
		return password.matches("^\\w{6,18}$");
	}
	public static boolean isNumber(String number){
		return number.matches("^\\d+$");
	}
	public static boolean isVeriCode(String veriCode){
		return veriCode.length()==6;
	}
	public static int getVersionCode(){
		try {
			return appContext.getPackageManager().getPackageInfo(appContext.getPackageName(),0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}
	public static void deleteFileFromSDCard(String filename){
		File file = new File(android.os.Environment.getExternalStorageDirectory(),filename);
		if(file.exists()&&file.length()==AppUtil.getSharedPreferencesUtilInstance().getUncompletedFileBytes(filename)) {
			boolean b=file.delete();
			Log.d("sdInfo","isDeleted="+b+"  @AppUtil");
		}else{
			Log.d("sdInfo","file not exist  @AppUtil");
		}
		
	}
	public static String getVersionName(){
		try {
			return appContext.getPackageManager().getPackageInfo(appContext.getPackageName(),0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String loginOnServer(String phone,String password) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("phone", phone);
		map.put("password", password);
		return getNetUtilInstance().sendMessageWithHttpPost(UriConstants.USER_LOGIN, map);
	}
	public static boolean loginOnClient(String phone,String password,String serverResponse) throws JSONException{
		boolean b =false;
		JSONObject jo = new JSONObject(serverResponse);
		if(b=jo.getBoolean(IConstants.RESPONSE_SUCCESS)){
			AppUtil.putIntoApplication(IConstants.LOGIN_USER, EntityMapFactory.generateUser(phone, password));
			if(!jo.getBoolean("isVerified")){
				AppUtil.putIntoApplication(IConstants.LOGIN_USER_VERIFICATION,false);
			}else{
				AppUtil.removeFromApplication(IConstants.LOGIN_USER_VERIFICATION);
			}
		}
		return b;
	}
	public static MapUtil getMapUtilInstance(){
		return MapUtil.getInstance(appContext);
	}
	public static void updateShoppingCartDB(){
		try {
			Log.w("appInfo","shoppingCart="+appContext.shoppingCartMap+"\nprodIdSet="
					+appContext.reservedProdIdSet+"  @appUtil updateShoppingCartDb");
			ShoppingCartDao cartDao = getServiceFactory().getShoppingCartDaoInstance(appContext);
			List<CartItem> cartItems=appContext.shoppingCartList;
			Log.w("appInfo","cartItems="+cartItems+"  @appUtil updateShoppingCartDb");
			for(int i =0;i<cartItems.size();i++){
				CartItem item = cartItems.get(i);
				if(appContext.reservedProdIdSet.contains(item.prod_id)){
					Log.w("appInfo","Update  @appUtil updateShoppingCartDb");
					cartDao.updateItem(item);appContext.reservedProdIdSet.remove(item.prod_id);
				}else{
					Log.w("appInfo","addResult="+cartDao.addItem(item)+"  @appUtil updateShoppingCartDb");
				}
			}
			cartDao.deleteItemByProdIdSet(appContext.reservedProdIdSet);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}
	public static void turnShoppingCartItemsToHistory(){
		updateShoppingCartDB();
		try {
			getServiceFactory().getShoppingCartDaoInstance(appContext).
			changeItemStatusToHistoryByPhone(AppUtil.getLoginPhoneNum());
			appContext.reservedProdIdSet.clear();
			appContext.shoppingCartMap.clear();
			appContext.shoppingCartList.clear();
		} catch (AppException e) {
			e.printStackTrace();
		}
		
	}
}
