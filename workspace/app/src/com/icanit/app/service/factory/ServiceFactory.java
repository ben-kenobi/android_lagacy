package com.icanit.app.service.factory;

import android.content.Context;

import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.service.UserService;
import com.icanit.app.sqlite.ShoppingCartDao;
import com.icanit.app.sqlite.ShoppingCartService;

public interface ServiceFactory {
	DataService getDataServiceInstance(Context context) throws AppException;
	ShoppingCartDao getShoppingCartDaoInstance(Context context) throws AppException;
	UserService getUserServiceInstance(Context context)throws AppException;
	ShoppingCartService getShoppingCartServiceInstance(Context context)throws AppException;
}
