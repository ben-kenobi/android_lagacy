package com.icanit.app_v2.adapter;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icanit.app_v2.R;

public class SuggestionAdapter extends BaseAdapter {
	private List list;
	private Context context;
	private Field field;
	private int resId=R.layout.simple_list_item3;
	public SuggestionAdapter(Context context,Field field){
		this.context=context;this.field=field;
	}
	public List getList(){
		return list;
	}
	public void setList(List list){
		this.list=list;
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if(list==null) return 0;
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		if(list==null||position>list.size()-1) return null;
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId,container,false);
		}
		try {
			((TextView)convertView).setText(field.get(list.get(position)).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return convertView;
	}
	
}
