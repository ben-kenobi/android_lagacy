package com.yf.accountmanager;

import android.app.Application;

import com.yf.accountmanager.sqlite.AccountService;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.util.CommonUtils;

public class MyApplication extends Application{
	
	
	public String accessKey;
	
	public void onCreate() {
		super.onCreate();
		AccountService.context=this;
		CommonService.context=this;
		CommonUtils.context=this;
	}
	
	public void onTerminate() {
		AccountService.context=null;
		CommonService.context=null;
		CommonUtils.context=null;
		super.onTerminate();
	}
}
