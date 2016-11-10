package com.icanit.app_v2.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.util.AppUtil;
import com.unionpay.upomp.lthj.util.PluginHelper;
public  class UpompPay{
	public static final String CMD_PAY_PLUGIN = "cmd_pay_plugin";	
	//�̻�����
	public static final String MERCHANT_PACKAGE = "com.lthj.gq.merchant";
	public  void start_upomp_pay(Activity thisActivity,String launchPay,boolean newOrder){
		if(launchPay==null||!launchPay.startsWith("<?xml")) {
			AppUtil.toast("�ύʧ�ܡ�����������");
			return ;
		}
		if(newOrder)
			AppUtil.turnShoppingCartItemsToHistory();
		
		byte[] to_upomp = launchPay.getBytes();
		Bundle mbundle = new Bundle();
		// to_upompΪ�̻��ύ��XML
		mbundle.putByteArray("xml", to_upomp);
		mbundle.putString("action_cmd", CMD_PAY_PLUGIN);
		//������������������������,valueΪtrue�ǲ��Բ�� ��Ϊfalse���������
		mbundle.putBoolean("test", false);	
		Log.d("errorTag","@upomPay");
		PluginHelper.LaunchPlugin(thisActivity, mbundle);
		}	
}