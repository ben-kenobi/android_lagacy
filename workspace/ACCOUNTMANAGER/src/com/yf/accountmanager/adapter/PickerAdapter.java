package com.yf.accountmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yf.accountmanager.R;

public class PickerAdapter extends CursorAdapter{
	private int resId=R.layout.item4lv_simpletext;

	public PickerAdapter(Context context) {
		super(context,null,true);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		((TextView)convertView).setText(cursor.getString(0));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return LayoutInflater.from(context).inflate(resId, parent,false);
	}
	
	

}
