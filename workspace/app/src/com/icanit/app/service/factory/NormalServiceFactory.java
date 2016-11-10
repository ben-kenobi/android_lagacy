package com.icanit.app.service.factory;

import android.content.Context;
import android.util.Log;

import com.icanit.app.exception.AppException;
import com.icanit.app.service.DataService;
import com.icanit.app.service.UserService;
import com.icanit.app.sqlite.ShoppingCartDao;
import com.icanit.app.sqlite.ShoppingCartService;
import com.icanit.app.util.PropertiesConfig;
public final class NormalServiceFactory implements ServiceFactory{
	private static ServiceFactory sf=new NormalServiceFactory();
	private DataService dataService;
	private UserService userService;
	private ShoppingCartDao cartDao;
	private ShoppingCartService cartService;
	@Override
	public DataService getDataServiceInstance(Context context) throws AppException {
		try {
			if(dataService==null)
			dataService= (DataService)Class.forName(PropertiesConfig.getProperty("dataService")).
					getConstructor(Context.class).newInstance(context!=null?context.getApplicationContext():null);
			return dataService;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("create dataServiceException @NormalServiceFactory;",e);
		} 
	}
	private NormalServiceFactory(){}
	public static ServiceFactory getInstance(){
		return sf;
	}
	@Override
	public ShoppingCartDao getShoppingCartDaoInstance(Context context) throws AppException {
		try {
			if(cartDao==null){
				cartDao=(ShoppingCartDao)Class.forName(PropertiesConfig.getProperty("shoppingCartDao")).
					getConstructor(Context.class).newInstance(context!=null?context.getApplicationContext():null);
			}
			return cartDao;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("createShoppingCartDaoException @NormalServiceFactory;",e);
		}
	}
	@Override
	public UserService getUserServiceInstance(Context context)
			throws AppException {
		try {
			if(userService==null)
			userService= (UserService)Class.forName(PropertiesConfig.getProperty("userService")).
					getConstructor(Context.class).newInstance(context!=null?context.getApplicationContext():null);
			return userService;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("createUserServiceException @NormalServiceFactory;",e);
		} 
	}
	@Override
	public ShoppingCartService getShoppingCartServiceInstance(Context context)
			throws AppException {
		try {
			if(cartService==null)
			cartService= (ShoppingCartService)Class.forName(PropertiesConfig.getProperty("shoppingCartService")).
					newInstance();
			return cartService;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("createUserServiceException @NormalServiceFactory;",e);
		} 
	}
	
}
