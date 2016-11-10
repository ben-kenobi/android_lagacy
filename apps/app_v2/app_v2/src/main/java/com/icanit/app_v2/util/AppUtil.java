package com.icanit.app_v2.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bshare.BShare;
import com.bshare.core.BSShareItem;
import com.bshare.core.BShareBaseHandler;
import com.bshare.core.Config;
import com.bshare.core.PlatformType;
import com.google.gson.Gson;
import com.icanit.app_v2.R;
import com.icanit.app_v2.activity.MapApplication;
import com.icanit.app_v2.activity.MerchandizeListActivity;
import com.icanit.app_v2.adapter.MerchandizeAdPagerAdapter;
import com.icanit.app_v2.adapter.MerchantAdPagerAdapter;
import com.icanit.app_v2.common.ByteArrayCache;
import com.icanit.app_v2.common.FileCache;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.common.MemoryCache;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.component.MyAnimator;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.service.factory.ServiceFactory;
import com.icanit.app_v2.sqlite.ShoppingCartDao;
import com.icanit.app_v2.ui.AutoChangeCheckRadioGroup;
import com.what.weibo.Constants;
import com.what.weibo.WeiboAlertDialog;

public final class AppUtil {
	public static MapApplication appContext;
	public static Toast globalToast;

	public static ServiceFactory getServiceFactory() throws AppException {
		try {
			return (ServiceFactory) Class
					.forName(
							PropertiesConfig
									.getProperty(IConstants.SERVICE_FACTORY))
					.getMethod(IConstants.SERVICE_FACTORY_GETINSTANCE)
					.invoke(null);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("Fail to create ServiceFactory  @AppUtil;",
					e);
		}
	}

	public static void toast(String message) {
		if (globalToast == null) {
			globalToast = Toast.makeText(appContext, null, Toast.LENGTH_SHORT);
		}
		globalToast.setText(message);
		globalToast.show();
	}

	public static JSONUtil getJSONUtilInstance() {
		return JSONUtil.getInstance();
	}

	public static NetUtil getNetUtilInstance() {
		return NetUtil.getInstance();
	}

	public static byte[] turnStreamToByteArray(InputStream is)
			throws IOException {
		byte[] dest = new byte[0];
		byte[] buf = new byte[1024];
		int count, length;
		while ((count = is.read(buf)) != -1) {
			length = dest.length;
			dest = Arrays.copyOf(dest, length + count);
			System.arraycopy(buf, 0, dest, length, count);
		}
		return dest;
	}

	public static ListenerUtil getListenerUtilInstance() {
		return ListenerUtil.getInstance();
	}

	public static SharedPreferencesUtil getSharedPreferencesUtilInstance() {
		return SharedPreferencesUtil.getInstance(appContext);
	}

	public static void sendKeyEvent(final int keyCode) {
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				Instrumentation in = new Instrumentation();
				in.sendCharacterSync(keyCode);
			}
		});
	}

	public static void bindBackListener(View view) {
		if (view == null)
			return;
		view.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				sendKeyEvent(KeyEvent.KEYCODE_BACK);
			}
		});
	}

	public static double calculateTotalCost(List<CartItem> items) {
		if (items == null)
			return 0;
		double cost = 0;
		for (CartItem item : items) {
			cost += item.present_price * item.quantity;
		}
		return cost;
	}

	public static void bindEditTextNtextDisposer(final EditText editText,
			final View textDisposer) {
		new ListenerUtil.TextChangeListener(editText, textDisposer);
	}

	public static void bindSearchTextNtextDisposerNsearchConfirm(
			final EditText editText, final View textDisposer,
			final View searchConfirmButton) {
		new ListenerUtil.TextChangeListener(editText, textDisposer,
				searchConfirmButton);
	}

	public static void putIntoApplication(String key, Object value) {
		appContext.put(key, value);
	}

	public static Object getFromApplication(String key) {
		return appContext.get(key);
	}

	public static void removeFromApplication(String key) {
		appContext.remove(key);
	}

	public static UserContact getUserContactFromApplication() {
		Object obj = getFromApplication(IConstants.USER_CONTACT);
		if (obj == null)
			return null;
		return (UserContact) obj;
	}

	public static String getLoginPhoneNum() {
		if (appContext.loginUser != null)
			return appContext.loginUser.phone;
		return "";

	}

	public static AppUser getLoginUser() {
		return appContext.loginUser;
	}

	public static boolean isPhoneNum(String phone) {
		if (phone == null)
			return false;
		return phone.matches("^[1]([3][0-9]{1}|59|56|58|88|89)[0-9]{8}$");
	}

	public static boolean isDecimal(String decimal, int scale) {
		if (TextUtils.isEmpty(decimal))
			return false;
		return decimal.matches("^[0-9]*\\.?[0-9]{0," + scale + "}$")
				&& Double.parseDouble(decimal) > 0;
	}

	public static boolean isPassword(String password) {
		if (password == null)
			return false;
		return password.matches("^\\w{6,18}$");
	}

	public static boolean isNumber(String number) {
		if (number == null)
			return false;
		return number.matches("^\\d+$");
	}

	public static boolean isVeriCode(String veriCode) {
		if (veriCode == null)
			return false;
		return veriCode.length() == 3;
	}

	public static int getVersionCode() {
		try {
			return appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void deleteFileFromSDCard(String filename) {
		File file = new File(
				android.os.Environment.getExternalStorageDirectory(), filename);
		if (file.exists()
				&& file.length() == AppUtil.getSharedPreferencesUtilInstance()
						.getUncompletedFileBytes(filename)) {
			boolean b = file.delete();
			Log.d("sdInfo", "isDeleted=" + b + "  @AppUtil");
		} else {
			Log.d("sdInfo", "file not exist  @AppUtil");
		}
	}

	public static void clearAllCaches() {
		FileCache.clear();
		ByteArrayCache.getInstance().clear();
		MemoryCache.getInstance().clear();
	}

	public static String getVersionName() {
		try {
			return appContext.getPackageManager().getPackageInfo(
					appContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean loginOnServer(String phone, String password)
			throws Exception {
		boolean b = false;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("phone", phone);
		map.put("password", password);
		String response = getNetUtilInstance().sendMessageWithHttpPost(
				UriConstants.USER_LOGIN, map);
		if (response != null && !"".equals(response)) {
			b = loginOnClient(response);
		}
		return b;
	}

	public static boolean loginOnClient(String serverResponse)
			throws JSONException, AppException {
		boolean b = false;
		AppUser user = new Gson().fromJson(serverResponse, AppUser.class);
		if (user != null) {
			appContext.loginUser = user;
			// appContext.loadCollectedMerchants(user.phone);
			b = true;
		}
		return b;
	}

	public static void logout() {
		appContext.loginUser = null;
		getSharedPreferencesUtilInstance().setLoginUserInfo(
				IConstants.AUTO_LOGIN, "false");
		// appContext.collectedMerIdSet.clear();
	}

	// public static MapUtil getMapUtilInstance(){
	// return MapUtil.getInstance(appContext);
	// }

	public static void loadShoppingCartInfo() {
		appContext.loadShoppingCartInfo();
	}

	public static void updateShoppingCartDB() {
		setCartItemsDeliveryField();
		try {
			Log.w("appInfo", "shoppingCart=" + appContext.shoppingCartMap
					+ "\nprodIdSet=" + appContext.reservedProdIdSet
					+ "  @appUtil updateShoppingCartDb");
			ShoppingCartDao cartDao = getServiceFactory()
					.getShoppingCartDaoInstance(appContext);
			List<CartItem> cartItems = appContext.shoppingCartList;
			Log.w("appInfo", "cartItems=" + cartItems
					+ "  @appUtil updateShoppingCartDb");
			for (int i = 0; i < cartItems.size(); i++) {
				CartItem item = cartItems.get(i);
				if (appContext.reservedProdIdSet.contains(item.prod_id)) {
					Log.w("appInfo", "Update  @appUtil updateShoppingCartDb");
					cartDao.updateItem(item);
					appContext.reservedProdIdSet.remove(item.prod_id);
				} else {
					Log.w("appInfo", "addResult=" + cartDao.addItem(item)
							+ "  @appUtil updateShoppingCartDb");
				}
			}
			Log.w("appInfo", "after update shoppingCart="
					+ appContext.shoppingCartMap + "\nprodIdSet="
					+ appContext.reservedProdIdSet
					+ "  @appUtil updateShoppingCartDb");
			cartDao.deleteItemByProdIdSet(appContext.reservedProdIdSet);
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public static void turnShoppingCartItemsToHistory() {
		if (appContext.shoppingCartList == null
				|| appContext.shoppingCartList.isEmpty())
			return;
		updateShoppingCartDB();
		try {
			getServiceFactory().getShoppingCartDaoInstance(appContext)
					.changeItemStatusToHistoryByPhone(
							AppUtil.getLoginPhoneNum());
			appContext.reservedProdIdSet.clear();
			appContext.shoppingCartMap.clear();
			appContext.shoppingCartList.clear();
			appContext.dividedItemList.clear();
		} catch (AppException e) {
			e.printStackTrace();
		}
	}

	public static void setCartItemsDeliveryField() {
		List<MerchantCartItems> mcList = appContext.dividedItemList;
		for (int i = 0; i < mcList.size(); i++) {
			MerchantCartItems mc = mcList.get(i);
			for (int j = 0; j < mc.items.size(); j++) {
				CartItem item = mc.items.get(j);
				item.setDelivery(mc.delivery);
				item.postscript = mc.postscript;
			}
		}
	}

	public static void adapterNotifyInHandler(Handler handler,
			final BaseAdapter adapter) {
		handler.post(new Runnable() {
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}

	public static void setOnClickListenerForCheckedTextView(
			final CheckedTextView ctv) {
		ctv.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (ctv.isChecked())
					ctv.setChecked(false);
				else
					ctv.setChecked(true);
			}
		});
	}

	public static void setOnClickListenerForSelectionView(final View v) {
		v.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (v.isSelected())
					v.setSelected(false);
				else
					v.setSelected(true);
			}
		});
	}

	public static boolean hadRecommended(int merId) {
		return appContext.recommendedMerchants.contains(merId);
	}

	public static boolean hadCollected(int merId) {
		if (getLoginUser() == null)
			return false;
		try {
			return getServiceFactory().getUserCollectionDaoInstance(appContext)
					.getCollectionCountByMerId(merId, getLoginPhoneNum()) > 0;
		} catch (AppException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void recommend(final AppMerchant mer, final TextView tv,
			final Context context) {
		if(getLoginUser()==null){
			toast("未登陆用户无法执行该操作");
		}else if (appContext.recommendedMerchants.contains(mer.id)) {
			Toast.makeText(context, "您已经推荐过了。", Toast.LENGTH_SHORT).show();
		} else {
			IConstants.THREAD_POOL.submit(new Runnable() {
				public void run() {
					try {
						int recommend = getServiceFactory()
								.getDataServiceInstance(context).sendRecommend(
										mer.id,getLoginPhoneNum());
						if (recommend > 0) {
							//TODO
							mer.recommend = recommend;
							appContext.recommendedMerchants.add(mer.id);
							((Activity) context).runOnUiThread(new Runnable() {
								public void run() {
									tv.setSelected(true);
									tv.setText(mer.recommend + "");
									Toast.makeText(context, "推荐成功。",
											Toast.LENGTH_SHORT).show();
								}
							});
						}
					} catch (AppException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void handlerCollectionEvent(AppMerchant merchant,
			TextView tv, Context context) throws AppException {
		if (getLoginUser() == null) {
			Toast.makeText(context, "未登录用户无法进行这类操作", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tv.isSelected()) {
			getServiceFactory().getUserCollectionDaoInstance(context)
					.cancelCollection(merchant.id, getLoginPhoneNum());
			tv.setSelected(false);
		} else {
			getServiceFactory().getUserCollectionDaoInstance(context)
					.collectMerchant(merchant, getLoginPhoneNum());
			tv.setSelected(true);
		}
	}

	public static void setNoAdImage(FrameLayout adContainer, Context context) {
		adContainer.removeAllViews();
		ImageView imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.ad3);
		imageView.setAdjustViewBounds(true);
		imageView.setScaleType(ScaleType.FIT_XY);
		adContainer.addView(imageView, -1, -1);
	}

	public static ViewPager setMerchantAdPagers(FrameLayout adContainer,
			ViewPager adContent, List<AppMerchant> adMerchantList,
			Context context) throws AppException {
		adContainer.removeAllViews();
		MerchantAdPagerAdapter adAdapter = null;
		AutoChangeCheckRadioGroup adIndicator = null;
		if (adContent == null) {
			adContent = new ViewPager(context);
			adContent
					.setAdapter(adAdapter = new MerchantAdPagerAdapter(context));
			adContent.setTag(adIndicator = FeaturedViewCreator
					.createAdIndicatingRG(context));
			getListenerUtilInstance().bindAdContentNAdIndicator(adContent,
					adIndicator, adAdapter);
		} else {
			adAdapter = (MerchantAdPagerAdapter) adContent.getAdapter();
			adIndicator = (AutoChangeCheckRadioGroup) adContent.getTag();
		}
		adAdapter.setMerchantList(adMerchantList);
		adIndicator.initRadioGroup(adAdapter.getCount(), context);
		adContainer.addView(adContent, 0);
		adContainer.addView(adIndicator, 1);

		adIndicator.startAd();
		return adContent;
	}

	public static ViewPager setMerchandizeAdPagers(FrameLayout adContainer,
			ViewPager adContent, List<AppGoods> goodsList, Context context)
			throws AppException {
		adContainer.removeAllViews();
		MerchandizeAdPagerAdapter adAdapter = null;
		AutoChangeCheckRadioGroup adIndicator = null;
		if (adContent == null) {
			adContent = new ViewPager(context);
			adContent.setAdapter(adAdapter = new MerchandizeAdPagerAdapter(
					context));
			adContent.setTag(adIndicator = FeaturedViewCreator
					.createAdIndicatingRG(context));
			getListenerUtilInstance().bindAdContentNAdIndicator(adContent,
					adIndicator, adAdapter);
		} else {
			adAdapter = (MerchandizeAdPagerAdapter) adContent.getAdapter();
			adIndicator = (AutoChangeCheckRadioGroup) adContent.getTag();
		}
		adAdapter.setGoodsList(goodsList);
		adIndicator.initRadioGroup(adAdapter.getCount(), context);
		adContainer.addView(adContent, 0);
		adContainer.addView(adIndicator, 1);

		adIndicator.startAd();
		return adContent;
	}

	public static int calculateListViewHeight(ListView lv, int itemHeight,
			int itemCount) {
		int verticalPaddingHeight = lv.getPaddingBottom() + lv.getPaddingTop();
		if (itemCount == 0)
			return verticalPaddingHeight;
		return itemHeight * itemCount + lv.getDividerHeight() * (itemCount - 1)
				+ verticalPaddingHeight;
	}

	public static int getMeasuredHeight(View view) {
		view.measure(0, 0);
		return view.getMeasuredHeight();
	}

	public static void checkAutoLoginIf() {
		SharedPreferencesUtil share = AppUtil
				.getSharedPreferencesUtilInstance();
		String str = share.getReservedUserInfo(IConstants.AUTO_LOGIN);
		if (str != null && "true".equalsIgnoreCase(str)) {
			final String phone = share.getReservedUserInfo(IConstants.PHONE);
			final String password = share
					.getReservedUserInfo(IConstants.PASSWORD);
			if (phone == null || password == null)
				return;
			IConstants.THREAD_POOL.submit(new Runnable() {
				public void run() {
					try {
						AppUtil.loginOnServer(phone, password);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public static void addShortcutToDesktop(Context context) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		shortcut.putExtra("duplicate", false);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				context.getString(R.string.app_name));
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(context,
						R.drawable.app_logo));
		shortcut.putExtra(
				Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(context, context.getClass())
						.setAction(Intent.ACTION_MAIN)
						.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
						.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY));
		context.sendBroadcast(shortcut);
	}

	public static String getOrderStatusDescByOrderStatus(int status) {
		return status == 0 ? "未支付" : status == 1 ? "已支付" : status == 2 ? "发货中"
				: status == 3 ? "确认完成" : status == 4 ? "撤销中"
						: status == 5 ? "撤销完成" : status == 6 ? "退货中"
								: status == 7 ? "退货完成" : status == 8 ? "货品未收到"
										: status == 9 ? "问题订单" : "";
	}

	public static Intent newIntentForMerchandizeListActivity(int merId)
			throws AppException {
		AppMerchant merchant = getServiceFactory().getDataServiceInstance(
				appContext).getStoreInfoById(merId);
		if (merchant == null)
			return null;
		return new Intent().setClass(appContext, MerchandizeListActivity.class)
				.putExtra(IConstants.MERCHANT_KEY, merchant);
	}

	public static String formatPhoneNum(String phone) {
		if (phone.length() != 11)
			return phone;
		char[] chary = phone.toCharArray();
		for (int i = 0; i < 4; i++) {
			chary[i + 3] = '*';
		}

		return new String(chary);
	}

	public static void updateUser() {
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				boolean b = false;
				try {
					b = loginOnServer(getLoginUser().phone,
							getLoginUser().password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!b)
					toast("更新用户信息失败");
			}
		});

	}

	public static void bindSearchEditTextTrigger(View trigger1, View trigger2,
			View v) {
		try {
			final MyAnimator anim = new MyAnimator(v, 0,
					DeviceInfoUtil.getScreenWidth() * 2 / 3, 300);
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (anim.toValue == anim.endValue) {
						anim.startReverse();
					} else {
						anim.start();
					}
				}
			};
			if (trigger1 != null)
				trigger1.setOnClickListener(listener);
			if (trigger2 != null)
				trigger2.setOnClickListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static byte[] getAssetResAsBytes(String name, Context context)
			throws Exception {
		InputStream is = context.getAssets().open(name);
		byte[] bytes = new byte[0];
		byte[] buf = new byte[1024];
		int count = 0, olength = 0;
		while ((count = is.read(buf)) != -1) {
			olength = bytes.length;
			bytes = Arrays.copyOf(bytes, olength + count);
			System.arraycopy(buf, 0, bytes, olength, count);
		}
		is.close();
		return bytes;
	}

	public static void shareToMB(PlatformType platform, Context context) {
		BSShareItem item = new BSShareItem(platform, "应用分享",
				"分享一个不错的应用\nhttp://183.250.110.72:8443/appserver/",
				"http://183.250.110.72:8443/appserver/");
		item.setImageUrl("http://183.250.110.72:8443/appserver/images/app_v2_download.png");
		try {
			item.setImg(AppUtil.getAssetResAsBytes("app_v2_download.png",
					context));
		} catch (Exception e) {
			e.printStackTrace();
		}
		BShare.instance(context).share(context, item);
	}

	public static void shareToOthers(Context context, ResolveInfo ri) {
		if (ri != null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT,
					context.getString(R.string.setting_share_app_subject));
			intent.putExtra(Intent.EXTRA_TEXT,
					context.getString(R.string.setting_share_app_body)
							+ "http://183.250.110.72:8443/appserver/");
			intent.setComponent(new ComponentName(
					ri.activityInfo.applicationInfo.packageName,
					ri.activityInfo.name));
			context.startActivity(intent);
		}
	}

	public static void removeMBCredential(String message,
			final Context context, final TextView tv,
			final PlatformType platform) {
		final WeiboAlertDialog dialog = new WeiboAlertDialog(context);
		dialog.setTitle(message).setNegativeButton("取消", new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		}).setPositiveButton("确定", new OnClickListener() {
			public void onClick(View v) {
				BShare.instance(context).removeCredential(context, platform);
				tv.setText("未绑定");
				dialog.dismiss();
			}
		}).show();
	}

	public static void initBShareConfig(Context context) {
		Config.instance().setAutoCloseShareList(true);
		Config.instance().setShouldTrackBackClick(true);
		Config.instance().setPublisherUUID(
				"d6797b63-d359-4a5f-a7e3-dbeeffbd7847");
		Config.instance().setSinaAppkeyAndAppSecret(Constants.SINA_APP_KEY,
				Constants.SINA_APP_KEY_SEC);
		Config.instance().setSinaCallbackUrl(Constants.SINA_REDIRECT_URL);
		Config.instance().setQQMBAppkeyAndAppSecret(Constants.TECENT_APP_KEY,
				Constants.TECENT_APP_KEY_SEC);
		BShare.instance(context).registerBShareHandler(
				new BShareBaseHandler(context));

	}

	public static void afterModifyUserInfoSuccess(String phone, String password) {
		if (phone != null)
			getLoginUser().phone = phone;
		if (password != null)
			getLoginUser().password = password;
		AppUtil.getSharedPreferencesUtilInstance().saveLoginUserInfo(
				getLoginUser().phone, getLoginUser().password, false);
	}

	public static void deleteFileRecursively(File file, boolean retainDir) {
		if (!file.exists())
			return;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFileRecursively(files[i], false);
			}
		}
		if (file.isFile() || !retainDir)
			file.delete();
	}

	public static long getDirBytes(File file) {
		if (!file.exists())
			return 0;
		long size = 0;
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				size += getDirBytes(files[i]);
			}
		} else {
			size = file.length();
		}
		return size;
	}

	public static void intentToCall(String phoneNum, Context context) {
		Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum)); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	context.startActivity(intent);
	}

	public static void intentToDial(String phoneNum, Context context) {
		Intent intent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNum)); 
    	intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	context.startActivity(intent);
	}
	
	public static String formatMoney(Double money){
		return String.format("%.2f", money);
	}

}
