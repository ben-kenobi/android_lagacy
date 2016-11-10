package com.icanit.app_v2.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class InnerScrollView extends ScrollView {
	private ScrollView parentScrollView;
	private int currentY;  int mTop = 10; private int lastScrollDelta = 0;
	public InnerScrollView(Context context) {
		super(context);
	}
	
	 public InnerScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InnerScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
	       boolean b =super.onInterceptTouchEvent(ev);
	       Log.i("touchEvent",b+"    onInterceptTouchEvent  @InnerScrollView");
	       return b;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean b =  super.onTouchEvent(ev);
		System.out.println("ScrollView.scrollY="+getScrollY()+"   @InnerScrollView");
		 Log.i("touchEvent",b+"   onTouchEvent  @InnerScrollView");
		return b;
	}

	public ScrollView getParentScrollView() {
		return parentScrollView;
	}

	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}
	 
}
