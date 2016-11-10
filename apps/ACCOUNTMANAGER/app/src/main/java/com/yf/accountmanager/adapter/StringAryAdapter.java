package com.yf.accountmanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.accountmanager.R;

public class StringAryAdapter extends BaseAdapter{

	private int resId=R.layout.item4lv_simpletext;
	
	private String[] sary;
	
	private Context context;
	
	public StringAryAdapter(Context context ){
		super();
		this.context=context;
	}
	
	
	public void setSary(String[] sary){
		this.sary=sary;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(sary==null) return 0;
		return sary.length;
	}

	@Override
	public Object getItem(int position) {
		if(sary==null) return null;
		return sary[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView=LayoutInflater.from(context).inflate(resId, parent,false);
		((TextView)convertView).setText(sary[position]);
		return convertView;
	}
	
}
