package com.icanit.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.unionpay.upomp.lthj.util.PluginHelper;
public  class UpompPay{
	public static final String CMD_PAY_PLUGIN = "cmd_pay_plugin";	
	//�̻�����
	public static final String MERCHANT_PACKAGE = "com.lthj.gq.merchant";
	public  void start_upomp_pay(Activity thisActivity,String LanchPay){
		byte[] to_upomp = LanchPay.getBytes();
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