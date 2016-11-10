package com.what.yunbao.order;

import java.util.List;

import com.what.yunbao.R;
import com.what.yunbao.util.AppOrderItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderDetailItemAdapter extends BaseAdapter{

	private Context mContext;
	private List<AppOrderItems> mList;
	
	public OrderDetailItemAdapter(Context context,List<AppOrderItems> list){
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
		View view = LayoutInflater.from(mContext).inflate(R.layout.order_detail_item_listitem, null);
		TextView goodName_tv = (TextView) view.findViewById(R.id.tv_good_name);
		TextView totalPrice_tv = (TextView) view.findViewById(R.id.tv_totalPrice);
		TextView quantity_tv = (TextView) view.findViewById(R.id.tv_quantity);
		//隐藏tag
		TextView goodId_tv = (TextView) view.findViewById(R.id.tv_good_id);
		TextView merchantId_tv = (TextView) view.findViewById(R.id.tv_merchant_id);
		goodId_tv.setText(String.valueOf(mList.get(position).getGoodId()));
		goodId_tv.setTag("GOOD_ID"+position);
		merchantId_tv.setText(String.valueOf(mList.get(position).getMerId()));
		merchantId_tv.setTag("MERCHANT_ID"+position);
		
		goodName_tv.setText(mList.get(position).getGoodName());
		totalPrice_tv.setText("价格："+String.valueOf(mList.get(position).getSum()));
		quantity_tv.setText(String.valueOf(mList.get(position).getQuantity()));
		
		return view;
	}

}
