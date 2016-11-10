package com.icanit.app_v2.common;

import android.graphics.Color;
import android.widget.TextView;


public class VeriCodeTimer  implements Runnable{
	public boolean stop=false;
	public long dueMillis;
	public TextView tv;
	public VeriCodeTimer(TextView tv){
		this.tv=tv;
	}
	@Override
	public void run() {
		if(stop||System.currentTimeMillis()-dueMillis>=0){
			tv.setEnabled(true);
			tv.setTextColor(Color.rgb(0x33, 0x33, 0x33));
			tv.setText("获取验证码");
		}else{
			tv.setText((dueMillis-System.currentTimeMillis())/1000+"S");
			IConstants.MAIN_HANDLER.postDelayed(this,1000);
		}
	}
	public void start(int second){
		stop=false;
		dueMillis=System.currentTimeMillis()+second*1000;
		IConstants.MAIN_HANDLER.post(this);
	}
	public void stop(){
		stop=true;
	}
}