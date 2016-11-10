package com.icanit.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icanit.app.R;
import com.icanit.app.entity.AppCommunity;

public class CommunityLvAdapter  extends MyBaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private int resId = R.layout.simple_list_item_2;
	private List<AppCommunity> communities=new ArrayList<AppCommunity>();
	public CommunityLvAdapter(Context context){
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(communities==null||communities.isEmpty())return 0;
		return communities.size();
	}

	@Override
	public Object getItem(int arg0) {
		return communities.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView=inflater.inflate(resId, parent,false);
		((TextView)convertView).setText(communities.get(position).commName);
		return convertView;
	}

	public List<AppCommunity> getCommunities() {
		return communities;
	}

	public void setCommunities(List<AppCommunity> communities) {
		this.communities = communities;
	}
	
}
