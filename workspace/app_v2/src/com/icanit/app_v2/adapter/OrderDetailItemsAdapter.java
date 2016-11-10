package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppOrderItems;
import com.icanit.app_v2.entity.CartItem;

public class OrderDetailItemsAdapter extends MyBaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resId=R.layout.item4lv_order_detail_items;
	private List<AppOrderItems> itemList;
	private OrderDetailLvAdapter outerLvAdapter;
	public OrderDetailItemsAdapter(Context context,List<AppOrderItems> itemList,OrderDetailLvAdapter outerLvAdapter){
		super();this.context=context;this.inflater=LayoutInflater.from(context);
		this.itemList=itemList;this.outerLvAdapter=outerLvAdapter;
	}

	public int getCount() {
		if(itemList==null||itemList.isEmpty()) return 0;
		return itemList.size();
	}
	public Object getItem(int arg0) {
		return itemList.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder =null;
		AppOrderItems item = itemList.get(position);
		if(convertView!=null) holder=(ViewHolder)convertView.getTag();
		if(convertView==null||holder==null){
			holder=new ViewHolder();convertView=inflater.inflate(resId, parent,false);
			holder.goodsName=(TextView)convertView.findViewById(R.id.textView1);
			holder.count=(TextView)convertView.findViewById(R.id.textView2);
			holder.unitPrice=(TextView)convertView.findViewById(R.id.textView3);
			convertView.setTag(holder);
		}
		holder.goodsName.setText(item.goodName);
		holder.count.setText(item.quantity+"");
		holder.unitPrice.setText("µ¥¼Û£º"+item.curPrice);
		return convertView;
	}
	
	 class ViewHolder{
		 public TextView goodsName,unitPrice,count;
	 }
		
}
