package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.UserContact;

public class UserInfoListAdapter extends MyBaseAdapter {
	public static final int[] colorCycle=new int[]{Color.rgb(0xff, 0xaa, 0xaa),Color.rgb(0xee,0xee,0),
		Color.rgb(0xc1,0xff,0xc1),Color.rgb(0x90, 0xee, 0x90),Color.rgb(0xa4, 0xd3, 0xee)};
	private Context context;
	private LayoutInflater inflater;
	private int resId=R.layout.item4lv_userinfo;
	private List<UserContact> contactList;
	public UserInfoListAdapter(Context context){
		super();this.context=context;inflater=LayoutInflater.from(context);
	}
	

	public int getCount() {
		if(contactList==null||contactList.isEmpty()) return 0;
		return contactList.size();
	}

	public Object getItem(int arg0) {
		return contactList.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		UserContact contact=contactList.get(contactList.size()-position-1);
		if(convertView!=null) holder=(ViewHolder)convertView.getTag();
		if(convertView==null||holder==null){
			holder=new ViewHolder();convertView=inflater.inflate(resId, parent,false);
			convertView.setTag(holder);holder.color=convertView.findViewById(R.id.view1);
			holder.info=(TextView)convertView.findViewById(R.id.textView1);
		}
		holder.info.setText("电话："+contact.phoneNum+"\n姓名："+
				contact.username+"\n地址："+contact.address);
		holder.color.setBackgroundColor(colorCycle[position%colorCycle.length]);
		return convertView;	
	}
	class ViewHolder {
		public TextView info;public View color;
	}
	public List<UserContact> getContactList() {
		return contactList;
	}


	public void setContactList(List<UserContact> contactList) {
		this.contactList = contactList;
		notifyDataSetChanged();
	}
	
	
}
