package com.what.weibo;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeiboAlertDialog {
	Context context;
	Dialog ad;
	TextView titleView;
	LinearLayout buttonLayout;
	Button negative_btn;
	Button positive_btn;
	public WeiboAlertDialog(Context context) {
		this.context=context;
		ad=new Dialog(context,R.style.dialogStyle);
		//使用window.setContentView,替换整个对话框窗口的布局
		View v=LayoutInflater.from(context).inflate(R.layout.weibo_dialog, null,false);
		ad.setContentView(v,new LayoutParams(-1,-1));
		titleView = (TextView)v.findViewById(R.id.tv_dialog_title);
		negative_btn = (Button) v.findViewById(R.id.btn_dialog_negative);
		positive_btn = (Button) v.findViewById(R.id.btn_dialog_positive);

	}
	public void show(){
		ad.show();
	}
	public WeiboAlertDialog setTitle(int resId){
		titleView.setText(resId);
		return this;
	}
	
	public WeiboAlertDialog setTitle(String title) {
		titleView.setText(title);
		return this;
	}

	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public WeiboAlertDialog setPositiveButton(String text,final View.OnClickListener listener){
		positive_btn.setOnClickListener(listener);
		return this;
	}
 
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public WeiboAlertDialog setNegativeButton(String text,final View.OnClickListener listener){
		negative_btn.setOnClickListener(listener);
		return this;
 
	}
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
