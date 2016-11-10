package com.icanit.app_v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AlwaysFocusedTextView extends TextView {

	

	public AlwaysFocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean isFocused() {
		return true;
	}
	
}
