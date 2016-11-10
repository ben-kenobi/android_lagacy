package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.entity.MerchantOrderItems;
import com.icanit.app_v2.ui.InnerListView2;
import com.icanit.app_v2.ui.OuterListView;
import com.icanit.app_v2.util.AppUtil;

public class OrderDetailLvAdapter extends MyBaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resId=R.layout.item4lv_order_detail;
	private TextView totalCost;
	private List<MerchantOrderItems> merOrderItemsList;
	private int subLvItemHeight,subLvMaxHeight,screenHeight;
	
	public OrderDetailLvAdapter(Context context,TextView totalCost){
		this.context=context;inflater=LayoutInflater.from(context);
		this.totalCost=totalCost;
		subLvItemHeight=AppUtil.getMeasuredHeight(inflater.inflate(R.layout.item4lv_order_detail_items, null,false));
	}
	
	public List<MerchantOrderItems> getMerOrderItemsList() {
		return merOrderItemsList;
	}
	public void setMerOrderItemsList(List<MerchantOrderItems> merOrderItemsList) {
		this.merOrderItemsList=merOrderItemsList;
		notifyDataSetChanged();
	}

	
	
	public int getCount() {
//		return 10;
		if(merOrderItemsList==null) return 0;
		return merOrderItemsList.size();
	}

	public Object getItem(int arg0) {
		if(merOrderItemsList==null) return null;
		return merOrderItemsList.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup container) {
		final ViewHolder holder;
		final MerchantOrderItems mo =merOrderItemsList.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, container,false);
			convertView.setTag(holder);
			initHolder(holder,convertView,container);
		}else{holder=(ViewHolder)convertView.getTag();}
		holder.storeName.setText(mo.merchant.merName);
		holder.address.setText("µêÆÌµØÖ·£º"+mo.merchant.location);
		int height=AppUtil.calculateListViewHeight(holder.goodsList, subLvItemHeight, mo.items.size());
		LayoutParams params=holder.goodsList.getLayoutParams();
		params.height=mo.items.size()>3&&position!=merOrderItemsList.size()-1?
					AppUtil.calculateListViewHeight(holder.goodsList, subLvItemHeight, 3):height;
		holder.goodsList.setAdapter(new OrderDetailItemsAdapter(context,mo.items,this));
		return convertView;
	}
	
	private void initHolder(ViewHolder holder,View convertView,ViewGroup container){
		holder.snap=(ImageButton)convertView.findViewById(R.id.imageButton1);
		holder.storeName=(TextView)convertView.findViewById(R.id.textView1);
		holder.address=(TextView)convertView.findViewById(R.id.textView2);
		holder.goodsList=(InnerListView2)convertView.findViewById(R.id.listView1);
		holder.goodsList.setOuterLv((OuterListView)container);
	}
	
	class ViewHolder{
		public ImageButton snap;
		public TextView storeName;
		public TextView address;
		public InnerListView2 goodsList;
	}
	
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	public void removeItems(MerchantCartItems mc){
		merOrderItemsList.remove(mc);
		notifyDataSetChanged();
	}
	
}
