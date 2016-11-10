package com.icanit.app_v2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public final  class MyArrayAdapter extends ArrayAdapter<Object> {
	private int selected=-1;
	public MyArrayAdapter(Context context, 
			int textViewResourceId, Object[] objects) {
		super(context,  textViewResourceId, objects);
	}
	public MyArrayAdapter(Context context, int resource,
			int textViewResourceId, Object[] objects) {
		super(context, resource, textViewResourceId, objects);
	}


	public View getView(int position, View convertView, ViewGroup parent) {
			convertView= super.getView(position, convertView, parent);
		if(position==selected)
			convertView.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
		else
			convertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		return convertView;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	
}
