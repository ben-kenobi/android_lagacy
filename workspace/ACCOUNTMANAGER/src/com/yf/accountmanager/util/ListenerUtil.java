package com.yf.accountmanager.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ListenerUtil {
	private static ListenerUtil self;
	private ListenerUtil(){}
	public static ListenerUtil getInstance(){
		if(self==null) self= new ListenerUtil();
		return self;
	}
	
	
	

	
	
	public static class  TextChangeListener implements TextWatcher,OnClickListener{
		private EditText editText;
		private View textDisposer;
		private View searchConfirm;
		
		public TextChangeListener(EditText editText,View textDisposer,View searchConfirm){
			this.editText=editText;this.textDisposer=textDisposer;this.searchConfirm=searchConfirm;
			this.textDisposer.setOnClickListener(this);
			editText.addTextChangedListener(this);
		}
		public TextChangeListener(EditText editText,View textDisposer){
			this.editText=editText;this.textDisposer=textDisposer;
			this.textDisposer.setOnClickListener(this);
			editText.addTextChangedListener(this);
		}
		public void afterTextChanged(Editable s) {
			
		}
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		public void onTextChanged(CharSequence content, int start, int before,
				int count) {
			if(content.length()==0||!editText.isEnabled()){
				if(textDisposer!=null)
				textDisposer.setVisibility(View.GONE);
				if(searchConfirm!=null)
				searchConfirm.setVisibility(View.GONE);
			}else{
				if(textDisposer!=null)
				textDisposer.setVisibility(View.VISIBLE);
				if(searchConfirm!=null)
				searchConfirm.setVisibility(View.VISIBLE);
			}
		}
		public void onClick(View v) {
			if(v==textDisposer)
			editText.setText("");
		}
	}
}
