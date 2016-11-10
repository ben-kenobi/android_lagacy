package com.ican.ipay.common;

public interface UriConstants {
	String FIND_CATEGORY_HIERARCHY="category!findCategoryHierarchy";
	String FIND_CATELIST_BY_MERID="category!findCateListByMerId";
	String COMMUNITY_PAGINATION="community!listCommunitiesByPagination";
	String COMMUNITY_BYAREAID="community!listCommunitiesByAreaId.action";
	String AREA_BYPARENTID="area!listAreaByParentId.action";
//	String MERCHANT_TYPE_HIERARCHY="merchantType!findMerchantTypeHierarchy";
//	String FIND_MERCHANT_TYPE_BY_PARENT_ID="merchantType!findMerchantTypeByParentId";
	String FIND_GOODS_BY_MERID="goods!findGoodsByMerIdByPagination";
	String FIND_MERCHANT_BY_COMMID="merchant!findMerchantByCommIdByPagination";
	String FIND_MERCHANT_BY_ID="merchant!findMerchantById";
	String RECOMMEND_MERCHANT_BY_ID="merchant!recommend.action";
//	String DISCOMMEND_MERCHANT_BY_ID="merchant!discommend.action";
	String USER_LOGIN="user!login";
	String MODIFY_PASSWORD="user!modifyPassword";
	String USER_REGISTER="user!register";
	String RETRIEVE_AND_MODIFY_PASSWORD="user!retrieveAndModifyPassword";
	String RETRIEVE_PASSWORD="user!retrievePassword";
	String SEND_VERICODE="user!sendVeriCode";
	String RESEND_VERICODE="user!resendVeriCode";
	String SEND_VERICODE_TEMP_TONEWPHONE="user!sendTempVeriCodeToNewPhone.action";
	String CHANGE_ACCOUNT_PHONENUM="user!changeAccountPhoneNum.action";
	String USER_ACCOUNT_VERIFY="user!verifyUserAccount";
	String SUBMIT_ORDER="order!submitOrder_v2";
	String SUBMIT_RECHARGE_ORDER="order!submitRechargeOrder";
	String SUBMIT_ACCOUNT_PAY="order!submitAccountPay.action";
	String RESUBMIT_ORDER="order!resubmitOrder.action";
	String CANCEL_ORDER="order!cancelOrder.action";
	String NOTICE_AFTER_PAY="order!afterPayNotice";
	String NEW_VERSION_APP_DOWNLOAD="file/app_v2.apk";
	String GET_LATEST_APP_VERSION="latestAppVersion.txt";
	String GET_MERORDERITEMSLIST_BY_ORDERID="order!findOrderItemsByOrderId.action";
	String GET_MERORDERITEMSLIST_BY_ORDERNUMNTIME="order!findOrderItemsByOrderNumNTime.action";
	String GET_ORDERS_BY_PHONENSTATUS="order!findOrderByPhoneByStatus.action";
	String GET_ORDERS_BY_PHONENSTATUS_FROM_INDEX="order!findOrderByPhoneByStatusFromIndex.action";
	String BATCH_DELETE_ORDER="order!batchDeleteOrder.action";
	String GET_ORDER_VERICODE="order!sendTempVeriCodeMessage.action";
	String CONFIRM_ACCOUNT_PAY="order!confirmAccountPay.action";
	String GET_TARGET_ORDER="order!findTargetOrder.action";
	
//	String GET_ORDERINFO_N_SIGN="order!getOrderInfoNsign.action";
	String GET_ORDERINFO_N_SIGN="http://192.168.120.123:8080/GremPay_web/upomppay/upomppay.htm";
	
}
