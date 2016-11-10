package com.yf.contacts;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.accountmanager.R;

public class NumsAdapter extends BaseAdapter{
	
	private int resId=R.layout.item4lv_simpletext;
	
	private List<String> numsDetail,nums;
	
	private Context context;
	
	public int type;

	public NumsAdapter(Context context){
		super();
		this.context=context;
	}
	
	public void setNums(List<String> numsDetail,List<String> nums,int type){
		this.numsDetail=numsDetail;
		this.nums=nums;
		this.type=type;
		notifyDataSetChanged();
	}
	
	public String getNum(int position){
		if(nums!=null&&position>=0&&position<nums.size())
			return nums.get(position);
		return "";
	}
	
	@Override
	public int getCount() {
		if(numsDetail==null) return 0;
		return numsDetail.size();
	}

	@Override
	public Object getItem(int position) {
		if(numsDetail==null) return null;
		return numsDetail.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
			convertView=LayoutInflater.from(context).inflate(resId, parent,false);
		((TextView)convertView).setText(numsDetail.get(position));
		return convertView;
	}
	
	
	
}
