package com.icanit.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.icanit.app.R;
import com.icanit.app.common.UriConstants;
import com.icanit.app.entity.AppCommunity;
import com.icanit.app.entity.AppGoods;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.entity.CartItem;
import com.icanit.app.entity.EntityMapFactory;
import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.util.AppUtil;

public final class NormalDataServiceImpl implements DataService{
	private Context context;
	public NormalDataServiceImpl(Context context){
		this.context=context;
	}
	
	
	/**
	 * get  goodsList  by merId
	 */
	@Override
	public List<AppGoods> getProductsInfoByStoreId(int merId) throws AppException {
		List<AppGoods> list =null;
		try {
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据    
			String jsonResp=AppUtil.getNetUtilInstance().
					sendMessageWithHttpGet(UriConstants.FIND_GOODS_BY_MERID,"merId="+merId);
			list=new Gson().fromJson(jsonResp, new TypeToken<List<AppGoods>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		} 
		return list;
	}
	
	
	/**
	 * get merchantList by commId
	 */
	@Override
	public List<AppMerchant> getStoresInfoByCommunityId(int commId)
			throws AppException {
		List<AppMerchant> stores = null;
		try {
			String jsonResp = AppUtil.getNetUtilInstance().sendMessageWithHttpGet
					(UriConstants.FIND_MERCHANT_BY_COMMID, "commId="+commId);
			stores=new Gson().fromJson(jsonResp, new TypeToken<List<AppMerchant>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return stores;
	}

	@Override
	public String getOrderInfoNsign(List<CartItem> items) throws AppException {
		String response="";
		try {
			List<NameValuePair> params = convertCartItemsToNameValuePairList(items);
			params.add(new BasicNameValuePair("phone","18960897429"));
			params.add(new BasicNameValuePair("address","那美克星西区15号"));
			response = AppUtil.getNetUtilInstance().sendMessageWithHttpPost(UriConstants.SUBMIT_ORDER,
					params);
				Log.d("errorTag",response+" @NormalDataServiceImpl");
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return response;
	}
	
	
	@Override
	public List<AppCommunity> findCommunities() throws AppException {
		List<AppCommunity> communities = null;
		try {
			String jsonResp= AppUtil.getNetUtilInstance().sendMessageWithHttpGet(UriConstants.COMMUNITY_PAGINATION, "");
			communities=new Gson().fromJson(jsonResp, new TypeToken<List<AppCommunity>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		}
		return communities;
	}
	
	//=================================================================================;
	
	
	/**
	 * former MainActivity cityInfo
	 */
	public String[] getCityInfo() throws AppException {
		String[] cityInfo = new String[]{"北京","上海","福州","泉州","广州","加州"};
//		AppSQLite sqlite=new AppSQLite(context);
//		Cursor cursor=sqlite.getDistrictInfoSet();
//		String[] cityInfo = new String[cursor.getCount()];
//		Log.e("sqlite",cursor.getCount()+" @NormalDataServiceImpl");
//		for(int i=0;i<cursor.getCount();i++){
//			cursor.move(i);
//			cityInfo[i]=cursor.getString(1);
//		}
//		cursor.close();sqlite.close();
		return cityInfo;
	}
	
	/**
	 * former HomeActivity's 1st fragment  3X3 page Items info
	 */
	public List<Map<String,Object>> getItemsInfo() throws AppException {
		int firstpic=R.drawable.item_02;
		int[] iary=new int[10];
		for(int i=0;i<iary.length;i++)
			iary[i]=firstpic++;
		String[] sary = context.getResources().getStringArray(R.array.home_items_name);
		List list = new ArrayList<Map<String,Object>>();
		for(int i=0;i<sary.length;i++){
			Map map = new HashMap<String,Object>();
			map.put("pic",iary[i]);
			map.put("title",sary[i]);
			list.add(map);
		}
		return list;
	}

	/**
	 * former searchPageActivity category
	 */
	@Override
	public String[] getSearchCategory() throws AppException {
		return new String[]{"自助餐","火锅","烧烤","电影票","KTV","酒店","旅游","摄影",
				"足疗","零食","护肤","服装"};
	}

	/**
	 * not shoppingCart version get orderInfoNsign 
	 */
	@Override
	public String getOrderInfoNsign(Map<String, Object> info)
			throws AppException {
		try {
			String response = AppUtil.getNetUtilInstance().sendMessageWithHttpGet("obtainOrderInfoNsign.action",
					"sum="+String.format("%.0f",(Double.parseDouble(info.get("unitPrice").
							toString())*100*(Integer)info.get("buyingQuantity"))));
			Log.d("errorTag",response+" @NormalDataServiceImpl");
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e);
		} 
	}

	/**
	 * former alipay page info
	 */
	@Override
	public Map<String, String> getPurchaseInfo() throws AppException {
		Map<String,String> map= new HashMap<String,String>();
		map.put("merchandise", "海享捞养生火锅");
		map.put("seller", "上海汉涛信息咨询有限公司");
		map.put("sum", String.valueOf(19.90f));
		return map;
	}

	



	@Override
	public AppCommunity findCommunityById(int id) throws AppException {
		AppCommunity com= new AppCommunity();
		com.id=10086;
		com.commName="邮电公寓";com.location="福州鼓楼区蒙古营";
		com.cityName="福州";
		return com;
	}


	private List<NameValuePair> convertCartItemsToNameValuePairList(List<CartItem> items){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(items!=null&&!items.isEmpty()){
			for(int i=0;i<items.size();i++){
				CartItem item= items.get(i);
				params.add(new BasicNameValuePair("gidNquanPairs",item.prod_id+"_"+item.quantity));
			}
		}
		return params;
	}
		
}
