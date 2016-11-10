package com.icanit.app_v2.sqlite;

import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;

public interface UserBrowsingDao {
	 int COLLECTED=2,
				VIEWED=1;
	int getBrowsedMerchantCountByMerId(int merId,String phone) throws AppException;
	void addToBrowsedMerchant(AppMerchant merchant,String phone) throws AppException;
	void cancelBrowsedMerchant(int merId,String phone)throws AppException;
	void clearBrowsedMerchant(String phone) throws AppException;
}
