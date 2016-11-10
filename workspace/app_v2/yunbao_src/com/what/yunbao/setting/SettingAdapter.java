package com.what.yunbao.setting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

import com.what.yunbao.R;
import com.what.yunbao.push.PushSettingActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter{
	private Context mContext;
	private List<Map<String,String>> mListData;
	private Map<Integer,Boolean> mSelectedIndex;
	private String mCacheSize ;
	
	public SettingAdapter(Context context,List<Map<String,String>> listData){
		this.mContext = context;
		this.mListData = listData;
	} 

	public int getCount() {
		return mListData.size();
	}
	public Object getItem(int position) {
		return mListData.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.setting_item, null,false);
		TextView setting_tv = (TextView) view.findViewById(R.id.tv_setting_list_item_text);
		ImageView setting_iv = (ImageView) view.findViewById(R.id.iv_setting_list_item_arrow);
		CheckBox setting_cb = (CheckBox) view.findViewById(R.id.cb_setting_list_item_checkbox);
		RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rl_setting_list_layout);
		String text = mListData.get(position).get("text");
		setting_tv.setText(text);
		
		if(mListData.size()==4 &&  (position==1||position==2)){
			
			setting_iv.setVisibility(8);
			setting_cb.setVisibility(0);
			
			if(mSelectedIndex!=null){
				setting_cb.setChecked(mSelectedIndex.get(position));
				final int mPosition = position;
				
				setting_cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
						if(isChecked){						
							mSelectedIndex.put(mPosition,true);
							if(mPosition == 2){
								mContext.startActivity(new Intent(mContext,PushSettingActivity.class));
								JPushInterface.resumePush(mContext.getApplicationContext());
							}							
						}else{
							mSelectedIndex.put(mPosition, false);
							if(mPosition == 2){
								JPushInterface.stopPush(mContext.getApplicationContext());
							}							
						}
					}
				});	
			}
		}	
		
		
		if(mListData.size()==4 && text!="" && position==0){

			setting_iv.setVisibility(8);
			setting_cb.setVisibility(8);
			TextView cacheSize = new TextView(mContext);
			if(mCacheSize!=null){
				cacheSize.setText(mCacheSize);
			}
			cacheSize.setTextSize(16);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,-1);
			lp.setMargins(0, 0, 15, 0);
			lp.addRule(RelativeLayout.CENTER_VERTICAL);
			cacheSize.setLayoutParams(lp);
			layout.addView(cacheSize);
		}		
		return view;
	}
	
	public Map<Integer,Boolean> getSelectedIndex(){
		return mSelectedIndex ;
	}	
	
//	@Override
//	public void notifyDataSetChanged() {
//		
//		 Iterator<Integer> keys = mSelectedIndex.keySet().iterator();
//         while(keys.hasNext()){
//                 keys.next();
//                 keys.remove();
//         }
//         super.notifyDataSetChanged();
//	}
//	
	public void setCacheSize(String cacheSize){
		mCacheSize = cacheSize;
	}
	public void setSelectedIndex(Map<Integer,Boolean> selectedIndex){
		mSelectedIndex = selectedIndex;
	}
}
