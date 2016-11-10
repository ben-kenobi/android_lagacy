package com.icanit.app.common;

public interface UriConstants {
	String FIND_CATEGORY_HIERARCHY="category!findCategoryHierarchy";
	String COMMUNITY_PAGINATION="community!listCommunitiesByPagination";
	String MERCHANT_TYPE_HIERARCHY="merchantType!findMerchantTypeHierarchy";
	String FIND_MERCHANT_TYPE_BY_PARENT_ID="merchantType!findMerchantTypeByParentId";
	String FIND_GOODS_BY_MERID="goods!findGoodsByMerIdByPagination";
	String FIND_MERCHANT_BY_COMMID="merchant!findMerchantByCommIdByPagination";
	String USER_LOGIN="user!login";
	String MODIFY_PASSWORD="user!modifyPassword";
	String USER_REGISTER="user!register";
	String RETRIEVE_AND_MODIFY_PASSWORD="user!retrieveAndModifyPassword";
	String RETRIEVE_PASSWORD="user!retrievePassword";
	String RESEND_VERICODE="user!resendVeriCode";
	String RESEND_VERICODE_TEMP="user!resendTempVeriCode";
	String USER_ACCOUNT_VERIFY="user!verifyUserAccount";
	String SUBMIT_ORDER="order!submitOrder";
	String NOTICE_AFTER_PAY="order!afterPayNotice";
	String NEW_VERSION_APP_DOWNLOAD="file/app.apk";
	String GET_LATEST_APP_VERSION="latestAppVersion.txt";
}
