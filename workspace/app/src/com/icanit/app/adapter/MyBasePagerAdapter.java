package com.icanit.app.adapter;

import android.support.v4.view.PagerAdapter;

public abstract class MyBasePagerAdapter extends PagerAdapter{
	protected boolean busy;

	public boolean isBusy() {
		return busy;
	}

	public void setBusy(boolean busy) {
		this.busy = busy;
	}
	
}
