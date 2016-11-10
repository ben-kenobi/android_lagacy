package com.yf.accountmanager.action;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public abstract class BasePopWindow  implements BaseAction{
	protected PopupWindow pop;
	protected TextView trigger;
	protected View contentView;
	public BasePopWindow(int resId,LayoutInflater inflater,int width,int height){
		pop=new PopupWindow(contentView=inflater.inflate(resId, null,false),
				width,height,true);
		pop.setBackgroundDrawable(new ColorDrawable(Color.argb(0x00, 0x00, 0x00, 0x00)));
		pop.setOutsideTouchable(true);
		
		pop.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				if(trigger!=null)
				trigger.setSelected(false);
			}
		});
		
	}
	
	public void setTrigger(TextView trig){
		this.trigger=trig;
	}
	public void showAsDropDown(){
		if(trigger!=null)
		trigger.setSelected(true);
		pop.showAsDropDown(trigger);
	}
	public void showAtLocation(View parent){
		if(trigger!=null)
		trigger.setSelected(true);
		pop.showAtLocation(parent, Gravity.CENTER	,0,0);
	}
	
	public void dismiss(){
		pop.dismiss();
	}
	public abstract void resetTrigger();
}
