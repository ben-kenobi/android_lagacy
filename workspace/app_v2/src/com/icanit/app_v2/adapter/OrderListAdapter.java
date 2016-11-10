package com.icanit.app_v2.adapter;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.entity.AppOrder;
import com.icanit.app_v2.util.AppUtil;
import com.what.yunbao.R;

public class OrderListAdapter extends MyBaseAdapter implements OnCheckedChangeListener{
	private Context context;
	private List<AppOrder> orderList=new ArrayList<AppOrder>();
	private int resId=R.layout.order_item;
	public boolean isChoiceMode=false;
	public Set<Integer> selectedOrderId=new HashSet<Integer>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
	public OrderListAdapter(Context context){
		this.context = context;
	}
	public void setOrderList(List<AppOrder> orderList){
		this.orderList=orderList;
		notifyDataSetChanged();
	}
	public List<AppOrder> getOrderList(){
		return orderList;
	}
	public boolean  toggleChoiceMode(View view){
		selectedOrderId.clear();
		if(isChoiceMode=!isChoiceMode){
			if(view!=null)
			view.setVisibility(View.VISIBLE);
		}
		else {
			if(view!=null)
			view.setVisibility(View.GONE);
		}
		notifyDataSetChanged();
		return isChoiceMode;
	}
	
	public void appendOrderList(List<AppOrder> orderList){
		if(orderList==null) return;
		this.orderList.addAll(orderList);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		if(orderList==null) return 0;
		return orderList.size();
	}

	@Override
	public Object getItem(int position) {
		return orderList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	public void refreshAfterBatchDelete(boolean success,View view,Method method,Object obj) throws Exception{
		if(success){
			if(orderList.size()==selectedOrderId.size()){
				orderList.clear();
					method.invoke(obj,new Object[]{});
			}else{
				for(int i=0;i<orderList.size();){
					AppOrder order =orderList.get(i);
					if(selectedOrderId.contains(order.id))orderList.remove(order);else i++;
				}
			}
			toggleChoiceMode(view);
			notifyDataSetChanged();
			AppUtil.toast("É¾³ýÍê³É");
		}else{
			AppUtil.toast("É¾³ýÊ§°Ü£¬ÉÔºóÖØÊÔ¡£¡£");
		}
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder= null;AppOrder order = orderList.get(position);
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(resId, null,false);
			holder=new ViewHolder(convertView);
		}else 
			holder=(ViewHolder)convertView.getTag();
		holder.snap.setImageResource(context.getResources().getIdentifier( 
				context.getPackageName()+":drawable/al0"+(order.status+1),null,null));
		holder.orderNum.setText("¶©µ¥ºÅ£º   "+order.orderNumber);
		holder.time.setText(sdf.format(order.orderTime));
		holder.quantity.setText(order.count+"");
		holder.price.setText(String.format("%.2f",order.sum )+" Ôª");
		holder.checkBox.setTag(order.id);
		if(isChoiceMode){
			holder.arrow.setVisibility(View.GONE);
			holder.checkBox.setVisibility(View.VISIBLE);
			holder.checkBox.setChecked(selectedOrderId.contains(order.id));
		}else{
			holder.arrow.setVisibility(View.VISIBLE);
			holder.checkBox.setVisibility(View.GONE);
		}
		return convertView;
		
		
	}
	public class ViewHolder{
		public ImageView snap,arrow;
		public TextView orderNum,id,time,quantity,price;
		public CheckBox checkBox;
		public ViewHolder(View convertView){
			snap=(ImageView)convertView.findViewById(R.id.imageView1);
			arrow=(ImageView)convertView.findViewById(R.id.iv_order_arrow);
			orderNum = (TextView) convertView.findViewById(R.id.tv_order_item_name);
			orderNum.setLines(2);
			id = (TextView) convertView.findViewById(R.id.tv_order_id);
			time = (TextView) convertView.findViewById(R.id.tv_order_item_time);
			time.setTypeface(Typeface.SERIF,Typeface.ITALIC);
			quantity = (TextView) convertView.findViewById(R.id.tv_order_item_count);
			price = (TextView) convertView.findViewById(R.id.tv_order_item_price);
			price.setTextColor(Color.rgb(0xee, 0x76, 0x00));
			price.setTypeface(Typeface.SANS_SERIF,Typeface.BOLD_ITALIC);
			checkBox=(CheckBox)convertView.findViewById(R.id.checkBox1);
			checkBox.setOnCheckedChangeListener(OrderListAdapter.this);
			convertView.setTag(this);
		}
	}
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Integer orderId=(Integer)buttonView.getTag();
		if(isChecked){
			selectedOrderId.add(orderId);
		}else{
			selectedOrderId.remove(orderId);
		}
	}
}
