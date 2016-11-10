package com.icanit.app_v2.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.ListAdapter;

public class MyListView extends AbsListView {

	public MyListView(Context context) {
		super(context);
	}

	public ListAdapter getAdapter() {
		return null;
	}

	public void setSelection(int position) {
		
	}
	
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	
	@SuppressLint("Override")
	public void setAdapter(ListAdapter adapter) {
		
	}

	

}
