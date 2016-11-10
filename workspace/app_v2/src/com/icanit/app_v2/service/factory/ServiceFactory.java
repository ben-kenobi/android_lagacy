package com.icanit.app_v2.service.factory;

import android.content.Context;

import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.service.DataService;
import com.icanit.app_v2.service.UserService;
import com.icanit.app_v2.sqlite.ShoppingCartDao;
import com.icanit.app_v2.sqlite.ShoppingCartService;
import com.icanit.app_v2.sqlite.UserBrowsingDao;
import com.icanit.app_v2.sqlite.UserCollectionDao;
import com.icanit.app_v2.sqlite.UserContactDao;

public interface ServiceFactory {
	DataService getDataServiceInstance(Context context) throws AppException;
	ShoppingCartDao getShoppingCartDaoInstance(Context context) throws AppException;
	UserService getUserServiceInstance(Context context)throws AppException;
	ShoppingCartService getShoppingCartServiceInstance(Context context)throws AppException;
	UserContactDao getUserContactDaoInstance(Context context) throws AppException;
	UserCollectionDao getUserCollectionDaoInstance(Context context)throws AppException;
	UserBrowsingDao getUserBrowsingDaoInstance(Context context) throws AppException;
}
