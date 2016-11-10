package com.icanit.app_v2.ui;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.GridView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.util.AppUtil;

public class CustomizedDialog extends Dialog {
	public TextView title,message;
	public Button positiveButton,negativeButton;
	public CheckedTextView checkedText;
	public GridView gv;
	public static int resId=R.layout.dialog_customized;
	public static int shareResId=R.layout.dialog_sharelist;
	public CustomizedDialog(Context context) {
		super(context);
	}

	CustomizedDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	public boolean getCheckedTextStatus() {
		return checkedText.isChecked();
	}
	public CustomizedDialog setPositiveButton(String name,final DialogInterface.OnClickListener listener){
		positiveButton.setVisibility(View.VISIBLE);
		positiveButton.setText(name);
		positiveButton.setOnClickListener(new  View.OnClickListener() {
			public void onClick(View v) {
				if(listener!=null)
				listener.onClick(CustomizedDialog.this, DialogInterface.BUTTON_POSITIVE);
				dismiss();
			}
		});
		return this;
	}
	public CustomizedDialog setNegativeButton(String name,final DialogInterface.OnClickListener listener){
		negativeButton.setVisibility(View.VISIBLE);
		negativeButton.setText(name);
		negativeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(listener!=null)
				listener.onClick(CustomizedDialog.this, DialogInterface.BUTTON_NEGATIVE);
				dismiss();
			}
		});
		return this;
	}
	public CustomizedDialog(Context context, int theme) {
		super(context, theme);
	}
	public static CustomizedDialog initDialog(String title,String message,String checkedText,float fontSize,Context context){
		CustomizedDialog dialog= new CustomizedDialog(context,R.style.dialogStyle);
		View contentView=LayoutInflater.from(context).inflate(resId, null,false);
		dialog.setContentView(contentView);
		dialog.title=((TextView)contentView.findViewById(R.id.title));
		dialog.message=((TextView)contentView.findViewById(R.id.message));
		dialog.positiveButton= ((Button)contentView.findViewById(R.id.positiveButton));
		dialog.negativeButton=(Button)contentView.findViewById(R.id.negativeButton);
		dialog.checkedText=(CheckedTextView)contentView.findViewById(R.id.checkedTextView1);
		dialog.title.setText(title);
		if(fontSize!=0)
			dialog.message.setTextSize(fontSize);
		dialog.message.setText(message);
		if(checkedText!=null){
			dialog.checkedText.setText(checkedText);
			AppUtil.setOnClickListenerForCheckedTextView(dialog.checkedText);
		}else{dialog.checkedText.setVisibility(View.GONE);}
		dialog.positiveButton.setVisibility(View.GONE);
		dialog.negativeButton.setVisibility(View.GONE);
		return dialog;
	}
	public static CustomizedDialog initShareDialog(String title,final Context context){
		final CustomizedDialog dialog= new CustomizedDialog(context,R.style.dialogStyle);
		View contentView=LayoutInflater.from(context).inflate(shareResId, null,false);
		dialog.setContentView(contentView);
		dialog.title=((TextView)contentView.findViewById(R.id.title));
		dialog.gv=(GridView)contentView.findViewById(R.id.gridView1);
		dialog.title.setText(title);
		return dialog;
	}
	
	public static class Builder{
		Context context;
		String title,message,positiveButton,negativeButton,checkedText;
		float fontSize;
		DialogInterface.OnClickListener positiveButtonListener,negativeButtonListener;
		public Builder(Context context){
			this.context=context;
		}
		public Builder setTitle(String title){
			this.title=title;return this;
		}
		public Builder setMessage(String message){
			this.message=message;
			return this;
		}
		public Builder setCheckedText(String text){
			this.checkedText=text;
			return this;
		}
		public Builder setContentFontSize(float size){
			this.fontSize=size;
			return this;
		}
		public Builder setPositiveButton(String name,DialogInterface.OnClickListener listener){
			this.positiveButton=name;
			this.positiveButtonListener=listener;return this;
		}
		public Builder setNegativeButton(String name,DialogInterface.OnClickListener listener){
			this.negativeButton=name;
			this.negativeButtonListener=listener;return this;
		}
		
		public CustomizedDialog create(){
			final CustomizedDialog dialog=initDialog(title,message,checkedText,fontSize,context);
			if(positiveButton!=null){
				dialog.positiveButton.setVisibility(View.VISIBLE);
				dialog.positiveButton.setText(positiveButton);
				dialog.positiveButton.setOnClickListener(new  View.OnClickListener() {
					public void onClick(View v) {
						if(positiveButtonListener!=null)
						positiveButtonListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						dialog.dismiss();
					}
				});
			}else{dialog.positiveButton.setVisibility(View.GONE);}
			if(negativeButton!=null){
				dialog.negativeButton.setVisibility(View.VISIBLE);
				dialog.negativeButton.setText(negativeButton);
				dialog.negativeButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(negativeButtonListener!=null)
						negativeButtonListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						dialog.dismiss();
					}
				});
			}else{dialog.negativeButton.setVisibility(View.GONE);}
			LayoutParams lp=dialog.getWindow().getAttributes();
			System.out.println("height="+lp.height+",width="+lp.width+"   @CustomizedDialog");
			return dialog;
		}
	}
}
