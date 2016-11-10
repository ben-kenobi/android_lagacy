package com.icanit.app.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.icanit.app.entity.AppCommunity;
import com.icanit.app.entity.AppGoods;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.entity.CartItem;
import com.icanit.app.exception.AppException;

public interface DataService {
//	public static final String[] PRODS_INFO_KEYS=new String[]{"pic","description","merchandise",
//		"customerCount","location","distance","seller","unitPrice","formerPrice","buyingQuantity","prodId"};
//	public static final String[] STORE_INFO_KEYS	=new String[]{"pic","store","percapita","totalCount",
//		"location","distance","rating"};
	public static final ExecutorService THREAD_POOL=Executors.newFixedThreadPool(5);
	String[] getCityInfo() throws AppException;
	List<AppCommunity> findCommunities() throws AppException;
	List<Map<String,Object>> getItemsInfo() throws AppException;
	String[] getSearchCategory() throws AppException;
	List<AppGoods> getProductsInfoByStoreId(int id) throws AppException;
	Map<String,String> getPurchaseInfo() throws AppException;
	String getOrderInfoNsign(Map<String ,Object> info) throws AppException;
	String getOrderInfoNsign(List<CartItem> items) throws AppException;
	List<AppMerchant>	getStoresInfoByCommunityId(int id) throws AppException;
	AppCommunity findCommunityById(int id) throws AppException;
}
