package com.what.yunbao.person;

import com.what.yunbao.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PCGridViewAdapter extends BaseAdapter{
	private Context mContext;
	private String [] mTextArr;
	private Integer [] mImgArr;
	public PCGridViewAdapter(Context context,String [] textArr,Integer [] imgArr){
		this.mContext = context;
		this.mTextArr = textArr;
		this.mImgArr = imgArr;
	}
	@Override
	public int getCount() {
		
		return mTextArr.length;
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
		View view = LayoutInflater.from(mContext).inflate(R.layout.personal_center_item, null);
		ImageView gvItem_iv = (ImageView) view.findViewById(R.id.iv_person_gvitem_img);
		TextView gvItem_tv = (TextView) view.findViewById(R.id.tv_person_gvitem_tv);
		gvItem_tv.setText(mTextArr[position]);
		gvItem_iv.setBackgroundResource(mImgArr[position]);
		return view;
	}
	
}
