package com.what.yunbao.update;

import com.what.yunbao.R;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAlertDialog {
	Context context;
	android.app.AlertDialog ad;
	TextView titleView;
	TextView messageView;
	LinearLayout buttonLayout;
	Button negative_btn;
	Button positive_btn;
	public MyAlertDialog(Context context) {
		this.context=context;
		ad=new android.app.AlertDialog.Builder(context).create();
		ad.show();
		//使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.dialog);
		titleView = (TextView)window.findViewById(R.id.tv_dialog_title);
		messageView = (TextView) window.findViewById(R.id.tv_dialog_message);
		negative_btn = (Button) window.findViewById(R.id.btn_dialog_negative);
		positive_btn = (Button) window.findViewById(R.id.btn_dialog_positive);

	}
	public void setTitle(int resId){
		titleView.setText(resId);
	}
	
	public void setTitle(String title) {
		titleView.setText(title);
	}
	public void setMessage(int resId) {
		messageView.setText(resId);
	}
 
	public void setMessage(String message){
		messageView.setText(message);
	}
	
	public void hideMessage(){
		messageView.setVisibility(View.GONE);
	}
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setPositiveButton(String text,final View.OnClickListener listener){
		positive_btn.setOnClickListener(listener);
	}
 
	/**
	 * 设置按钮
	 * @param text
	 * @param listener
	 */
	public void setNegativeButton(String text,final View.OnClickListener listener){
		negative_btn.setOnClickListener(listener);
 
	}
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
}
