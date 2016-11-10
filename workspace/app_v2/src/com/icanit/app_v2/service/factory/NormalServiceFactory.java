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
import com.icanit.app_v2.util.PropertiesConfig;
public final class NormalServiceFactory implements ServiceFactory{
	private static ServiceFactory sf=new NormalServiceFactory();
	private DataService dataService;
	private UserService userService;
	private ShoppingCartDao cartDao;
	private ShoppingCartService cartService;
	private UserContactDao contactDao;
	private UserCollectionDao collectionDao;
	private UserBrowsingDao browsingDao;
	@Override
	public DataService getDataServiceInstance(Context context) throws AppException {
		try {
			if(dataService==null)
			dataService= (DataService)Class.forName(PropertiesConfig.getProperty("dataService")).
					getConstructor(Context.class).newInstance(context!=null?context.getApplicationContext():null);
			System.out.println(dataService+"   @NormalServiceFactory");
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
	@Override
	public UserContactDao getUserContactDaoInstance(Context context)
			throws AppException {
			try {
				if(contactDao==null)
				contactDao=(UserContactDao)Class.forName(PropertiesConfig.getProperty("userContactDao"))
				.getConstructor(Context.class).newInstance(context!=null?context.getApplicationContext():null);
				return contactDao;
			} catch (Exception e) {
				e.printStackTrace();
				throw new AppException("createUserContactDaoException   @NormalServiceFactory");
			} 
	}
	@Override
	public UserCollectionDao getUserCollectionDaoInstance(Context context)
			throws AppException {
		try {
			if(collectionDao==null)
				collectionDao=(UserCollectionDao)Class.forName(PropertiesConfig.getProperty("userCollectionDao"))
				.getConstructor(Context.class).newInstance(context);
			return collectionDao;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("createUserCollectionDaoException   @NormalServiceFactory");
		} 
	}
	@Override
	public UserBrowsingDao getUserBrowsingDaoInstance(Context context)
			throws AppException {
		try {
			if(browsingDao==null)
				browsingDao=(UserBrowsingDao)Class.forName(PropertiesConfig.getProperty("userBrowsingDao"))
				.getConstructor(Context.class).newInstance(context);
			return browsingDao;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("createUserBrowsingDaoException   @NormalServiceFactory");
		} 
	}
	
}
