package com.icanit.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.unionpay.upomp.lthj.util.PluginHelper;
public  class UpompPay{
	public static final String CMD_PAY_PLUGIN = "cmd_pay_plugin";	
	//商户包名
	public static final String MERCHANT_PACKAGE = "com.lthj.gq.merchant";
	public  void start_upomp_pay(Activity thisActivity,String LanchPay){
		byte[] to_upomp = LanchPay.getBytes();
		Bundle mbundle = new Bundle();
		// to_upomp为商户提交的XML
		mbundle.putByteArray("xml", to_upomp);
		mbundle.putString("action_cmd", CMD_PAY_PLUGIN);
		//更换参数调起测试与生产插件,value为true是测试插件 ，为false是生产插件
		mbundle.putBoolean("test", false);	
		Log.d("errorTag","@upomPay");
		PluginHelper.LaunchPlugin(thisActivity, mbundle);
		}	
}