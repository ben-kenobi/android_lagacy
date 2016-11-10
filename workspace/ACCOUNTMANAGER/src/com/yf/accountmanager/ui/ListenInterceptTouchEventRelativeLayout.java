package com.yf.accountmanager.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class ListenInterceptTouchEventRelativeLayout extends RelativeLayout{
	public  OnTouchListener interceptTouchEventListener; 

	public ListenInterceptTouchEventRelativeLayout(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ListenInterceptTouchEventRelativeLayout(Context context,
			AttributeSet attrs) {
		super(context, attrs);
	}

	public ListenInterceptTouchEventRelativeLayout(Context context) {
		super(context);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(interceptTouchEventListener!=null)
		interceptTouchEventListener.onTouch(this, ev);
		return super.onInterceptTouchEvent(ev);
	}
	public void setInterceptTouchEventListener(OnTouchListener listener){
		this.interceptTouchEventListener=listener;
	}
	
}
