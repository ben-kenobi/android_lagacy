package com.what.yunbao.history;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.what.yunbao.R;
import com.what.yunbao.util.Notes.CollectColumns;

public class BusinessAdapter extends CursorAdapter{
	private int resId=R.layout.history_business_item;
	private ViewPagerFragment fragment;
	public BusinessAdapter(Context context,ViewPagerFragment vf){
		super(context,null);
		this.fragment=vf;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		ViewHolder holder=(ViewHolder)view.getTag();
		holder.onbind(cursor);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup container) {
		View view = LayoutInflater.from(context).inflate(resId, container,false);
		new ViewHolder(view);
		return view;
	}
	class ViewHolder{
		public TextView name,address,minCost;
		public ViewHolder(View view){
			name= (TextView) view.findViewById(R.id.tv_business_item_name);
			address = (TextView) view.findViewById(R.id.tv_business_item_addr);
			minCost = (TextView) view.findViewById(R.id.tv_business_item_distance);
			view.setTag(this);
		}
		public void onbind(Cursor cursor){
			name.setText(cursor.getString(cursor.getColumnIndex(CollectColumns.MERNAME)));
			address.setText(cursor.getString(cursor.getColumnIndex(CollectColumns.LOCATION)));
			double minc=cursor.getDouble(cursor.getColumnIndex(CollectColumns.MINCOST));
			minCost.setText(minc==0?"支持送货":"起送价：￥"+minCost);
		}
	}
	@Override
	protected void onContentChanged() {
		super.onContentChanged();
		if(getCount()==0){
			fragment.showEmptyTip();
		}
	}
}
