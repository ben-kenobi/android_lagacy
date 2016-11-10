package com.yf.accountmanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class FocusedEditText extends EditText{
	
	public boolean focused=false;
	
	public FocusedEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FocusedEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FocusedEditText(Context context) {
		super(context);
	}

	@Override
	public boolean isFocused() {
		if(focused)
		return true;
		return super.isFocused();
	}
}
