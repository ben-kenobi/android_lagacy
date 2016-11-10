package com.icanit.app_v2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.util.AppUtil;

public class MapApplication extends Application {
	private Map<String, Object> map = new HashMap<String, Object>();
	public AppUser loginUser;
	public Map<String, CartItem> shoppingCartMap;
	public Set<String> reservedProdIdSet;
	public List<CartItem> shoppingCartList;
	public List<MerchantCartItems> dividedItemList;
	public Set<Integer> recommendedMerchants = new HashSet<Integer>();
//	public Set<Integer> collectedMerIdSet = new HashSet<Integer>();

	public void put(String key, Object value) {
		map.put(key, value);
	}

	public Object get(String key) {
		return map.get(key);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public void onCreate() {
		super.onCreate();
		AppUtil.appContext = this;
		// loadShoppingCartInfo();
	
		// 地图
		mInstance = this;
		initEngineManager(this);
		// 版本
		initLocalVersion();
		// 包名
		getAppPackageName();

	}

//	public void loadCollectedMerchants(String phone) throws AppException {
//		collectedMerIdSet = AppUtil.getServiceFactory()
//				.getUserCollectionDaoInstance(this).listCollections(phone);
//	}

	public void loadShoppingCartInfo() {
		try {
			shoppingCartList = AppUtil.getServiceFactory()
					.getShoppingCartDaoInstance(this)
					.findAllItemsByPhone(AppUtil.getLoginPhoneNum());
			shoppingCartMap = listToMap(shoppingCartList);
			dividedItemList = divideCartItemsByMer(shoppingCartList);
			reservedProdIdSet = new HashSet<String>(shoppingCartMap.keySet());
			Log.w("appInfo", "shoppingCart=" + shoppingCartMap + "\nprodIdSet="
					+ reservedProdIdSet + " @MapApplication onCreate");
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	private Map<String, CartItem> listToMap(List<CartItem> items) {
		Map<String, CartItem> map = new HashMap<String, CartItem>();
		CartItem item;
		for (int i = 0; i < items.size(); i++) {
			item = items.get(i);
			map.put(item.prod_id, item);
		}
		return map;
	}

	private List<MerchantCartItems> divideCartItemsByMer(List<CartItem> itemList) {
		if (itemList == null || itemList.isEmpty())
			return new ArrayList<MerchantCartItems>();
		List<MerchantCartItems> dividedItemList = new ArrayList<MerchantCartItems>();
		outer: for (int i = 0; i < itemList.size(); i++) {
			CartItem item = itemList.get(i);
			for (int j = 0; j < dividedItemList.size(); j++) {
				MerchantCartItems mc = dividedItemList.get(j);
				if (mc.merchant.id == item.mer_id) {
					mc.items.add(item);
					continue outer;
				}
			}
			MerchantCartItems mc = new MerchantCartItems();
			mc.merchant.id = item.mer_id;
			mc.merchant.merName = item.mer_name;
			mc.merchant.location = item.mer_location;
			mc.postscript = item.postscript;
			mc.items.add(item);
			dividedItemList.add(mc);
		}
		return dividedItemList;
	}

	// DBMAP

	private static MapApplication mInstance = null;
	public boolean m_bKeyRight = true;
	public BMapManager mBMapManager = null;

	public static final String strKey = "B50390F03032E76014B30A719ED95C6E7641FCD6";

	// 包名
	public static String mPckName;

	// 版本
	public static String mAppName;

	public static String mDownloadPath;
	public static int mVersionCode;
	public static String mVersionName;
	public static boolean mShowUpdate = true;

	@Override
	// 建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (!mBMapManager.init(strKey, new MyGeneralListener())) {
			Toast.makeText(
					MapApplication.getInstance().getApplicationContext(),
					"BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
		}
	}

	public static MapApplication getInstance() {
		return mInstance;
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
		}

		@Override
		public void onGetPermissionState(int iError) {

		}
	}

	// 版本
	public void initLocalVersion() {
		PackageInfo pinfo;
		try {
			pinfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			mVersionCode = pinfo.versionCode;
			mVersionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 包名

	public void getAppPackageName() {
		mPckName = this.getPackageName();
	}

}
