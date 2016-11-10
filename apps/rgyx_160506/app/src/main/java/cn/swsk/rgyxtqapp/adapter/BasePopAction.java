package cn.swsk.rgyxtqapp.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public abstract class BasePopAction  implements BaseAction{
	protected PopupWindow pop;
	protected View trigger;
	protected View contentView;
	public OnClickListener trigClickListener;
	public BasePopAction(int resId,LayoutInflater inflater,View trig){
		pop=new PopupWindow(contentView=inflater.inflate(resId, null,false),
				-1,-1,true);
		pop.setBackgroundDrawable(new ColorDrawable(Color.argb(0x99, 0x00, 0x00, 0x00)));
		pop.setOutsideTouchable(true);
		this.trigger=trig;
		pop.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				trigger.setSelected(false);
			}
		});
		trigger.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showAsDropDown();
			}
		});
	}
	protected void onShow(){}
	public void showAsDropDown(){
		trigger.setSelected(true);
		pop.showAsDropDown(trigger);
		onShow();
		if(trigClickListener!=null)
			trigClickListener.onClick(trigger);
	}
	public void showAtLocation(View parent){
		trigger.setSelected(true);
		pop.showAtLocation(parent, Gravity.CENTER	,0,0);
	}
	public abstract void resetTrigger();
}
