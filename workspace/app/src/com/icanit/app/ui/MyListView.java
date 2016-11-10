package com.icanit.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListAdapter;

public class MyListView extends AbsListView {

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public ListAdapter getAdapter() {
		return null;
	}

	@Override
	public void setSelection(int arg0) {
	}

}
