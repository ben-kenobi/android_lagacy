package com.icanit.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.icanit.app.R;

public class CustomizedDialog extends Dialog {
	public static int resId=R.layout.dialog_customized;
	public CustomizedDialog(Context context) {
		super(context);
	}

	public CustomizedDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CustomizedDialog(Context context, int theme) {
		super(context, theme);
	}
	public static class Builder{
		Context context;
		String title,message,positiveButton,negativeButton;
		View contentView;
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
		public Builder setContentView(View view){
			this.contentView=view;
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
			contentView=LayoutInflater.from(context).inflate(resId, null,false);
			final CustomizedDialog dialog= new CustomizedDialog(context,R.style.dialogStyle);
			dialog.addContentView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT));
			((TextView)contentView.findViewById(R.id.title)).setText(title);
			((TextView)contentView.findViewById(R.id.message)).setText(message);
			Button button1 = ((Button)contentView.findViewById(R.id.positiveButton));
			Button button2=(Button)contentView.findViewById(R.id.negativeButton);
			if(positiveButton!=null){
				button1.setText(positiveButton);
				button1.setOnClickListener(new  View.OnClickListener() {
					public void onClick(View v) {
						if(positiveButtonListener!=null)
						positiveButtonListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						dialog.dismiss();
					}
				});
			}else{button1.setVisibility(View.GONE);}
			if(negativeButton!=null){
				button2.setText(negativeButton);
				button2.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						if(negativeButtonListener!=null)
						negativeButtonListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						dialog.dismiss();
					}
				});
			}else{button2.setVisibility(View.GONE);}
			
			return dialog;
		}
	}
}
