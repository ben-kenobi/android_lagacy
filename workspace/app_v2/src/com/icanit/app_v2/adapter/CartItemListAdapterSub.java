package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.ui.InnerListView2;
import com.icanit.app_v2.ui.OuterListView;
import com.icanit.app_v2.util.AppUtil;

public class CartItemListAdapterSub extends MyBaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private int resId=R.layout.item4lv_cartiteminfo_sub;
//	private List<CartItem> itemList;
//	private List<MerchantCartItems> dividedItemList;
	private TextView totalCost;
	private View confirmation,trashButton;
	private int subLvItemHeight,subLvMaxHeight;
	
	public CartItemListAdapterSub(Context context,TextView totalCost,View confirmation,View trashButton){
		this.context=context;inflater=LayoutInflater.from(context);
		this.totalCost=totalCost;this.confirmation=confirmation;this.trashButton=trashButton;
//		itemList=AppUtil.appContext.shoppingCartList;
		updateTotalCost();updateSubmitButton();
//		dividedItemList=AppUtil.appContext.dividedItemList;
		LinearLayout ll=((LinearLayout)inflater.inflate(R.layout.item4lv_cartiteminfo_x, null,false));
		subLvItemHeight=AppUtil.getMeasuredHeight(ll);
	}
	
	public List<MerchantCartItems> getDividedItemList() {
		return AppUtil.appContext.dividedItemList;
	}

	
	public  void updateTotalCost(){
		totalCost.setText("×Ü½ð¶î:£¤"+String.format("%.2f", AppUtil.calculateTotalCost(AppUtil.appContext.shoppingCartList)));
	}
	public void updateSubmitButton(){
		if(AppUtil.appContext.shoppingCartList==null||AppUtil.appContext.shoppingCartList.isEmpty()){
			confirmation.setEnabled(false);
			trashButton.setEnabled(false);
		}else {
			confirmation.setEnabled(true);
			trashButton.setEnabled(true);
		}
	}
	
	public int getCount() {
//		return 10;
		if(AppUtil.appContext.dividedItemList==null) return 0;
		return AppUtil.appContext.dividedItemList.size();
	}

	public Object getItem(int arg0) {
		if(AppUtil.appContext.dividedItemList==null) return null;
		return AppUtil.appContext.dividedItemList.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup container) {
		final ViewHolder holder;
		final MerchantCartItems mc =
				AppUtil.appContext.dividedItemList.get(position);
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, container,false);
			convertView.setTag(holder);
			initHolder(holder,convertView,container);
		}else{holder=(ViewHolder)convertView.getTag();}
		holder.storeName.setText(mc.merchant.merName);
		holder.delivery.setChecked(mc.delivery);
		holder.pickup.setChecked(!mc.delivery);
		int height=AppUtil.calculateListViewHeight(holder.goodsList, subLvItemHeight, mc.items.size());
		LayoutParams params=holder.goodsList.getLayoutParams();
		params.height=mc.items.size()>3&&position!=AppUtil.appContext.dividedItemList.size()-1?
				AppUtil.calculateListViewHeight(holder.goodsList, subLvItemHeight,3):height;
//		holder.goodsList.setLayoutParams(params);
		holder.postScript.setText(mc.postscript==null||"null".equals(mc.postscript)?"":mc.postscript);
		holder.postScript.setOnFocusChangeListener(new  OnFocusChangeListener() {
			public void onFocusChange(View v, boolean focused) {
				if(!focused){
					mc.postscript=holder.postScript.getText().toString();
				}
			}
		});
		try {
			holder.goodsList.setAdapter(new CartItemListAdapterSub_sub(context,mc, this));
		} catch (AppException e) {
			e.printStackTrace();
		}
		holder.delivery.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton self, boolean checked) {
				mc.delivery=checked;
			}
		});
		return convertView;
	}
	
	private void initHolder(ViewHolder holder,View convertView,ViewGroup container){
		holder.snap=(ImageButton)convertView.findViewById(R.id.imageButton1);
		holder.storeName=(TextView)convertView.findViewById(R.id.textView1);
		holder.delivery=(RadioButton)convertView.findViewById(R.id.radio0);
		holder.pickup=(RadioButton)convertView.findViewById(R.id.radio1);
		holder.goodsList=(InnerListView2)convertView.findViewById(R.id.listView1);
		holder.goodsList.setOuterLv((OuterListView)container);
		holder.postScript=(EditText)convertView.findViewById(R.id.editText1);
	}
	
	class ViewHolder{
		public ImageButton snap;
		public TextView storeName;
		public RadioButton delivery,pickup;
		public InnerListView2 goodsList;
		public EditText postScript;
	}
	
	@Override
	public void notifyDataSetChanged() {
		updateSubmitButton();updateTotalCost();
		super.notifyDataSetChanged();
	}
	public void clearAllCartItems() throws AppException{
		AppUtil.getServiceFactory().getShoppingCartServiceInstance(context).clearAllItems();
		notifyDataSetChanged();
	}
	
}
