package com.icanit.app_v2.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public final  class MySimpleAdapter  extends SimpleAdapter{
	private int selected=-1;
	public MySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView= super.getView(position, convertView, parent);
		if(position==selected)
			convertView.setBackgroundColor(Color.rgb(0xee, 0xee, 0xee));
		else
			convertView.setBackgroundColor(Color.argb(0,0,0,0));
		return convertView;
	}
	
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
}
