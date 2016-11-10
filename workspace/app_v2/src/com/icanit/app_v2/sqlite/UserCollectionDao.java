package com.icanit.app_v2.sqlite;

import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;

public interface UserCollectionDao {
	 int COLLECTED=2,
				VIEWED=1;
//	Set<Integer> listCollections(String phone) throws AppException;
	int getCollectionCountByMerId(int merId,String phone) throws AppException;
	void collectMerchant(AppMerchant merchant,String phone) throws AppException;
	void cancelCollection(int merId,String phone)throws AppException;
	void clearCollections(String phone) throws AppException;
}
