package com.what.yunbao.order;

import java.util.List;

import com.what.yunbao.R;
import com.what.yunbao.util.AppOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderItemAdapter extends BaseAdapter{
	private Context mContext;
	private List<AppOrder> mList;
	public OrderItemAdapter(Context context,List<AppOrder> list){
		this.mContext = context;
		this.mList = list;
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
		}
		
		//商铺名称暂时用地址代替
		TextView order_address_tv = (TextView) convertView.findViewById(R.id.tv_order_item_name);
		//商户id用隐藏tv保存
		TextView order_id_tv = (TextView) convertView.findViewById(R.id.tv_order_id);
		TextView order_time_tv = (TextView) convertView.findViewById(R.id.tv_order_item_time);
		TextView order_count_tv = (TextView) convertView.findViewById(R.id.tv_order_item_count);
		TextView order_price_tv = (TextView) convertView.findViewById(R.id.tv_order_item_price);
		
		order_address_tv.setText(mList.get(position).getAddress());
		order_id_tv.setText(String.valueOf(mList.get(position).getId()));
		order_id_tv.setTag(position);
		order_time_tv.setText(mList.get(position).getOrderTime());
		order_count_tv.setText(mList.get(position).getCount());
		order_price_tv.setText(String.valueOf(mList.get(position).getSum()));
		return convertView;
	}
	
}
