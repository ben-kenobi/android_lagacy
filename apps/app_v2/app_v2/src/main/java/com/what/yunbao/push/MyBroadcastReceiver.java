package com.what.yunbao.push;

import com.what.yunbao.test.TT;

import cn.jpush.android.api.JPushInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MyBroadcastReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e("WHAT", "用户点击打开了通知");
            Bundle bundle = intent.getExtras();
            String content = bundle.getString(JPushInterface.EXTRA_ALERT);
            content.contains("//businessId=");
            String [] arr = content.split("//businessId=");
            
        	//打开自定义的Activity
        	Intent i = new Intent(context, TT.class);
        	i.setAction(Intent.ACTION_NEW_OUTGOING_CALL);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	i.putExtra(Intent.EXTRA_UID, Long.valueOf(arr[1]));
        	context.startActivity(i);
		}
	}

}
