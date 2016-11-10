package com.icanit.app_v2.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.icanit.app_v2.entity.AppArea;
import com.icanit.app_v2.entity.AppCategory;
import com.icanit.app_v2.entity.AppCommunity;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantOrderItems;
import com.icanit.app_v2.entity.PayResultInfo;
import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;

public interface DataService {
//	public static final String[] PRODS_INFO_KEYS=new String[]{"pic","description","merchandise",
//		"customerCount","location","distance","seller","unitPrice","formerPrice","buyingQuantity","prodId"};
//	public static final String[] STORE_INFO_KEYS	=new String[]{"pic","store","percapita","totalCount",
//		"location","distance","rating"};
	String[] getCityInfo() throws AppException;
	List<AppCommunity> findCommunities() throws AppException;
	List<MerchantOrderItems> getMerchantOrderItemsListByOrderId(int orderId) throws AppException;
	List<MerchantOrderItems> getMerchantOrderItemsListByOrderNumNTime(String orderNum,String orderTime)throws AppException;
	List<AppOrder> getOrdersByPhoneNStatus(String phone,int status)throws AppException;
	List<AppOrder> getOrdersByPhoneNStatusPagination(String phone,int status,int page)throws AppException;
	List<AppOrder> getOrdersByPhoneNStatusFromIndex(String phone, int status,int index,int size)throws AppException;
	JSONObject cancelOrder(AppOrder order,PayResultInfo payResultInfo) ;
	boolean batchDeleteOrder(Set<Integer> orderIds)throws AppException;
	List<AppCommunity> findCommunitiesByAreaId(String areaId) throws AppException;
	String[] getSearchCategory() throws AppException;
	List<AppGoods> getProductsInfoByStoreId(int id) throws AppException;
	Map<String,String> getPurchaseInfo() throws AppException;
	String getOrderInfoNsign(Map<String ,Object> info) throws AppException;
	AppOrder getAccountPayOrderInfo(List<CartItem> items,UserContact contact) ;
	AppOrder confirmAccountPay(String veriCode,int orderId) ;
	AppOrder getTargetOrder(int orderId,String orderNum,String orderTime);
	String resubmitOrder(AppOrder order) ;
	String getOrderInfoNsign(List<CartItem> items,UserContact contact) throws AppException;
	String getOrderInfoNsign(String sum);
	List<AppMerchant>	getStoresInfoByCommunityId(String id) throws AppException;
	AppMerchant getStoreInfoById(int id)throws AppException;
	List<AppArea> listAreasByParentId(String parentId)throws AppException;
	List<AppCategory> getGoodsCategoryList(int merId) throws AppException;
	int sendRecommend(int merId,String phoneNum)throws AppException;
}
