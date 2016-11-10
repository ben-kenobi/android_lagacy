package com.what.yunbao.test;

import java.util.HashMap;
import java.util.List;

import com.what.yunbao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TestBusinessAdapter extends BaseAdapter{
	private Context mContext;
	private List<HashMap<String,String>> mList;
	public TestBusinessAdapter(Context context,List<HashMap<String,String>> list){
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {		
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = LayoutInflater.from(mContext).inflate(R.layout.history_business_item, null);
		TextView name_tv = (TextView) view.findViewById(R.id.tv_business_item_name);
		TextView address_tv = (TextView) view.findViewById(R.id.tv_business_item_addr);
		name_tv.setText(mList.get(position).get("name"));
		address_tv.setText(mList.get(position).get("address"));
		name_tv.setTag(position);
		address_tv.setTag(position+100);
		return view;
	}

}
