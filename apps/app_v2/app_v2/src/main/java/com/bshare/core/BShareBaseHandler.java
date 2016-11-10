package com.bshare.core;

import android.content.Context;
import android.widget.Toast;

public class BShareBaseHandler extends DefaultHandler {
	private Context context;
	public BShareBaseHandler(Context context){
		this.context=context;
	}
	@Override
	public int describeContents() {
		Toast.makeText(context, " describeContents", Toast.LENGTH_LONG).show();
		return super.describeContents();
	}


	@Override
	public void onShareComplete(PlatformType p, ShareResult sr) {
		if(sr.isSuccess()){
			Toast.makeText(context,"分享成功", Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void onShareStart(PlatformType p, BSShareItem item) {
		Toast.makeText(context, "开始分享", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onVerifyError(PlatformType p) {
		Toast.makeText(context, p+"  验证失败", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onVerifySuccess(PlatformType p, TokenInfo ti) {
		Toast.makeText(context, ti.toString()+"  verifySuccess", Toast.LENGTH_LONG).show();
	}
	
}
