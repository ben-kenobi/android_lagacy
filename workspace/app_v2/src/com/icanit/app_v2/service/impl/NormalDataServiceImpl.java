package com.icanit.app_v2.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icanit.app_v2.common.UriConstants;
import com.icanit.app_v2.entity.AppArea;
import com.icanit.app_v2.entity.AppCategory;
import com.icanit.app_v2.entity.AppCommunity;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.AppUser;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantOrderItems;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.service.DataService;
import com.icanit.app_v2.util.AppUtil;

public final class NormalDataServiceImpl implements DataService {
	private Context context;

	public NormalDataServiceImpl(Context context) {
		this.context = context;
	}

	/**
	 * get goodsList by merId
	 */
	public List<AppGoods> getProductsInfoByStoreId(int merId)
			throws AppException {
		List<AppGoods> list = null;
		try {
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(UriConstants.FIND_GOODS_BY_MERID,
							"merId=" + merId);
			list = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppGoods>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return list;
	}

	/**
	 * get merchantList by commId
	 */
	public List<AppMerchant> getStoresInfoByCommunityId(String commId)
			throws AppException {
		List<AppMerchant> stores = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(
							UriConstants.FIND_MERCHANT_BY_COMMID,
							"commId=" + commId);
			stores = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppMerchant>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return stores;
	}

	public String resubmitOrder(AppOrder order) {
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("orderId", order.id + ""));
			params.add(new BasicNameValuePair("userPhone", order.userPhone));
			params.add(new BasicNameValuePair("userPassword", AppUtil
					.getLoginUser().password));
			params.add(new BasicNameValuePair("payway", order.payway + ""));
			return AppUtil.getNetUtilInstance().sendMessageWithHttpPost(
					UriConstants.RESUBMIT_ORDER, params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getOrderInfoNsign(List<CartItem> items, UserContact contact)
			throws AppException {
		String response = "";
		try {
			List<NameValuePair> params = convertCartItemsToNameValuePairList(items);
			if (contact != null) {
				params.add(new BasicNameValuePair("contactPhone",
						contact.phoneNum));
				params.add(new BasicNameValuePair("address", contact.address));
				params.add(new BasicNameValuePair("name", contact.username));
				params.add(new BasicNameValuePair("userPhone", AppUtil
						.getLoginPhoneNum()));
			}
			response = AppUtil.getNetUtilInstance().sendMessageWithHttpPost(
					UriConstants.SUBMIT_ORDER, params);
			Log.d("errorTag", response + " @NormalDataServiceImpl");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return response;
	}

	public String getOrderInfoNsign(String sum) {
		String response = null;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", AppUtil
					.getLoginPhoneNum()));
			params.add(new BasicNameValuePair("userPassword", AppUtil
					.getLoginUser().password));
			params.add(new BasicNameValuePair("sum", sum));
			response = AppUtil.getNetUtilInstance().sendMessageWithHttpPost(
					UriConstants.SUBMIT_RECHARGE_ORDER, params);
			Log.d("errorTag", response + " @NormalDataServiceImpl");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	@Override
	public AppOrder getAccountPayOrderInfo(List<CartItem> items,
			UserContact contact) {
		try {
			List<NameValuePair> params = convertCartItemsToNameValuePairList(items);
			if (contact != null) {
				params.add(new BasicNameValuePair("contactPhone",
						contact.phoneNum));
				params.add(new BasicNameValuePair("address", contact.address));
				params.add(new BasicNameValuePair("name", contact.username));
				AppUser user = AppUtil.getLoginUser();
				params.add(new BasicNameValuePair("userPhone", user.phone));
				params.add(new BasicNameValuePair("userPassword", user.password));
			}
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(UriConstants.SUBMIT_ACCOUNT_PAY,
							params);
			if (TextUtils.isEmpty(jsonResp) || !jsonResp.startsWith("{"))
				return null;
			return new Gson().fromJson(jsonResp, AppOrder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<AppCommunity> findCommunities() throws AppException {
		List<AppCommunity> communities = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(UriConstants.COMMUNITY_PAGINATION,
							"");
			communities = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppCommunity>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return communities;
	}

	@Override
	public List<AppCommunity> findCommunitiesByAreaId(String areaId)
			throws AppException {
		List<AppCommunity> communities = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(UriConstants.COMMUNITY_BYAREAID,
							"areaId=" + areaId);
			communities = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppCommunity>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return communities;
	}

	@Override
	public List<AppArea> listAreasByParentId(String parentId) throws AppException {
		List<AppArea> areaList = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(UriConstants.AREA_BYPARENTID,
							"parentId=" + parentId);
			areaList = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppArea>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return areaList;
	}

	public AppMerchant getStoreInfoById(int merId) throws AppException {
		AppMerchant merchant = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(UriConstants.FIND_MERCHANT_BY_ID,
							"merId=" + merId);
			merchant = new Gson().fromJson(jsonResp, AppMerchant.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return merchant;
	}

	@Override
	public List<AppCategory> getGoodsCategoryList(int merId)
			throws AppException {
		List<AppCategory> cateList = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpGet(
							UriConstants.FIND_CATELIST_BY_MERID,
							"merId=" + merId);
			cateList = new Gson().fromJson(jsonResp,
					new TypeToken<List<AppCategory>>() {
					}.getType());
			return cateList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	public int sendRecommend(int merId,String phone) throws AppException {
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(
							UriConstants.RECOMMEND_MERCHANT_BY_ID,
							"merId=" + merId+"&phone="+phone);
			JSONObject jo = new JSONObject(jsonResp);
			return jo.getInt("recommend");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}

	}

	@Override
	public List<MerchantOrderItems> getMerchantOrderItemsListByOrderId(
			int orderId) throws AppException {
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(
							UriConstants.GET_MERORDERITEMSLIST_BY_ORDERID,
							"orderId=" + orderId);
			return new Gson().fromJson(jsonResp,
					new TypeToken<List<MerchantOrderItems>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}

	}

	@Override
	public List<MerchantOrderItems> getMerchantOrderItemsListByOrderNumNTime(
			String orderNum, String orderTime) throws AppException {
		try {
			String jsonResp = AppUtil
					.getNetUtilInstance()
					.sendMessageWithHttpGet(
							UriConstants.GET_MERORDERITEMSLIST_BY_ORDERNUMNTIME,
							"orderNum=" + orderNum + "&orderTime=" + orderTime);
			return new Gson().fromJson(jsonResp,
					new TypeToken<List<MerchantOrderItems>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}

	}

	@Override
	public List<AppOrder> getOrdersByPhoneNStatus(String phone, int status)
			throws AppException {
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(
							UriConstants.GET_ORDERS_BY_PHONENSTATUS,
							"userPhone=" + phone + "&status=" + status);
			return new Gson().fromJson(jsonResp,
					new TypeToken<List<AppOrder>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	@Override
	public List<AppOrder> getOrdersByPhoneNStatusPagination(String phone,
			int status, int page) throws AppException {
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(
							UriConstants.GET_ORDERS_BY_PHONENSTATUS,
							"userPhone=" + phone + "&status=" + status
									+ "&page=" + page);
			return new Gson().fromJson(jsonResp,
					new TypeToken<List<AppOrder>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	@Override
	public List<AppOrder> getOrdersByPhoneNStatusFromIndex(String phone,
			int status, int index, int size) throws AppException {
		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(
							UriConstants.GET_ORDERS_BY_PHONENSTATUS_FROM_INDEX,
							"userPhone=" + phone + "&status=" + status
									+ "&index=" + index + "&size=" + size);
			return new Gson().fromJson(jsonResp,
					new TypeToken<List<AppOrder>>() {
					}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	@Override
	public JSONObject cancelOrder(AppOrder order, PayResultInfo payResultInfo) {
		try {
			AppUser user = AppUtil.getLoginUser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", user.phone));
			params.add(new BasicNameValuePair("userPassword", user.password));
			if (order == null) {
				params.add(new BasicNameValuePair("orderNum",
						payResultInfo.merchantOrderId));
				params.add(new BasicNameValuePair("orderTime",
						payResultInfo.merchantOrderTime));
			} else
				params.add(new BasicNameValuePair("orderId", order.id + ""));
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(UriConstants.CANCEL_ORDER, params);
			if (jsonResp != null && jsonResp.startsWith("{"))
				return new JSONObject(jsonResp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean batchDeleteOrder(Set<Integer> orderIds) throws AppException {
		try {
			AppUser user = AppUtil.getLoginUser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", user.phone));
			params.add(new BasicNameValuePair("userPassword", user.password));
			for (Iterator<Integer> it = orderIds.iterator(); it.hasNext();) {
				params.add(new BasicNameValuePair("orderIds", it.next() + ""));
			}
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(UriConstants.BATCH_DELETE_ORDER,
							params);
			return new JSONObject(jsonResp).getBoolean("success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	@Override
	public AppOrder confirmAccountPay(String veriCode, int orderId) {
		try {
			AppUser user = AppUtil.getLoginUser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userPhone", user.phone));
			params.add(new BasicNameValuePair("userPassword", user.password));
			params.add(new BasicNameValuePair("tempVeriCode", veriCode));
			params.add(new BasicNameValuePair("orderId", orderId + ""));
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(UriConstants.CONFIRM_ACCOUNT_PAY,
							params);
			if (TextUtils.isEmpty(jsonResp) || !jsonResp.startsWith("{"))
				return null;
			return new Gson().fromJson(jsonResp, AppOrder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public AppOrder getTargetOrder(int orderId, String orderNum,
			String orderTime) {
		AppOrder order = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userPhone", AppUtil
				.getLoginPhoneNum()));
		if (orderId == 0) {
			params.add(new BasicNameValuePair("orderNum", orderNum));
			params.add(new BasicNameValuePair("orderTime", orderTime));
		} else
			params.add(new BasicNameValuePair("orderId", orderId + ""));

		try {
			String jsonResp = AppUtil.getNetUtilInstance()
					.sendMessageWithHttpPost(UriConstants.GET_TARGET_ORDER,
							params);
			order = new Gson().fromJson(jsonResp, AppOrder.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return order;
	}

	// =================================================================================;

	/**
	 * former MainActivity cityInfo
	 */
	public String[] getCityInfo() throws AppException {
		String[] cityInfo = new String[] { "北京", "上海", "福州", "泉州", "广州", "加州" };
		// AppSQLite sqlite=new AppSQLite(context);
		// Cursor cursor=sqlite.getDistrictInfoSet();
		// String[] cityInfo = new String[cursor.getCount()];
		// Log.e("sqlite",cursor.getCount()+" @NormalDataServiceImpl");
		// for(int i=0;i<cursor.getCount();i++){
		// cursor.move(i);
		// cityInfo[i]=cursor.getString(1);
		// }
		// cursor.close();sqlite.close();
		return cityInfo;
	}

	/**
	 * not shoppingCart version get orderInfoNsign
	 */
	public String getOrderInfoNsign(Map<String, Object> info)
			throws AppException {
		try {
			String response = AppUtil
					.getNetUtilInstance()
					.sendMessageWithHttpGet(
							"obtainOrderInfoNsign.action",
							"sum="
									+ String.format(
											"%.0f",
											(Double.parseDouble(info.get(
													"unitPrice").toString()) * 100 * (Integer) info
													.get("buyingQuantity"))));
			Log.d("errorTag", response + " @NormalDataServiceImpl");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
	}

	/**
	 * former alipay page info
	 */
	public Map<String, String> getPurchaseInfo() throws AppException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchandise", "海享捞养生火锅");
		map.put("seller", "上海汉涛信息咨询有限公司");
		map.put("sum", String.valueOf(19.90f));
		return map;
	}

	private List<NameValuePair> convertCartItemsToNameValuePairList(
			List<CartItem> items) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (items != null && !items.isEmpty()) {
			for (int i = 0; i < items.size(); i++) {
				CartItem item = items.get(i);
				params.add(new BasicNameValuePair("gidNquanPairs", item.prod_id
						+ "_" + item.quantity + "_" + item.postscript + "_"
						+ item.isDelivery()));
			}
		}
		return params;
	}

	@Override
	public String[] getSearchCategory() throws AppException {
		return new String[] { "自助餐", "火锅", "烧烤", "电影票", "KTV", "酒店", "旅游",
				"摄影", "足疗", "零食", "护肤", "服装" };
	}

}
