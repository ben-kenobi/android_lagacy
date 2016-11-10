package com.icanit.app_v2.adapter;

import android.widget.BaseAdapter;


public abstract class MyBaseAdapter extends BaseAdapter{
	protected boolean busy=false;

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
}
