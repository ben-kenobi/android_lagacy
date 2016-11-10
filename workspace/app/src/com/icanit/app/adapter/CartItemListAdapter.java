package com.icanit.app.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app.R;
import com.icanit.app.entity.CartItem;
import com.icanit.app.exception.AppException;
import com.icanit.app.sqlite.ShoppingCartService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.ImageUtil;

public final class CartItemListAdapter extends MyBaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CartItem> itemList;
	private int resId=R.layout.item4lv_fragment4home_04;
	private TextView totalCost;
	private Button submitButton;
	private Button clearCartButton;
	private ShoppingCartService shoppingCartService;
	public CartItemListAdapter(Context context,TextView tv,Button submitButton,Button clearCartButton) throws AppException {
		this.context=context;
		itemList=AppUtil.appContext.shoppingCartList;
		inflater=LayoutInflater.from(this.context);
		this.submitButton=submitButton;
		this.clearCartButton=clearCartButton;
		totalCost=tv;shoppingCartService=AppUtil.getServiceFactory().getShoppingCartServiceInstance(context);
	}
	public int getCount() {
		return itemList.size();
	}

	public Object getItem(int arg0) {
		return itemList.get(arg0);
	}
	private  void updateTotalCost(){
		totalCost.setText("总价:￥"+String.format("%.2f", AppUtil.calculateTotalCost(itemList)));
	}
	private void updateSubmitButton(){
		Log.w("cartInfo","items="+itemList+"\n,itemList="+itemList+"  @CartItemListAdapter  updateSubmitButton");
		if(itemList==null||itemList.isEmpty()){
			submitButton.setEnabled(false);
			clearCartButton.setEnabled(false);
		}else {
			submitButton.setEnabled(true);
			clearCartButton.setEnabled(true);
		}
	}
	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup container) {
		final ViewHolder holder;
		if(convertView==null){
			Log.w("adapterInfo",position+"  @CartItemListAdapter  getView ConvertView==null");
			holder=new ViewHolder();
			convertView = inflater.inflate(resId, container,false);
			holder.imgView=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.name=(TextView)convertView.findViewById(R.id.textView1);
			holder.unitPriceNquantity=(TextView)convertView.findViewById(R.id.textView2);
			holder.unitPriceNquantity=(TextView)convertView.findViewById(R.id.textView2);
			holder.totalPrice=(TextView)convertView.findViewById(R.id.textView3);
			holder.plus=(ImageButton)convertView.findViewById(R.id.imageButton1);
			holder.minus=(ImageButton)convertView.findViewById(R.id.imageButton2);
			holder.delete=(Button)convertView.findViewById(R.id.button1);
			convertView.setTag(holder);
		}else holder=(ViewHolder)convertView.getTag();
		Log.w("adapterInfo",position+"  @CartItemListAdapter  getView ConvertView!=null");
		final CartItem item = itemList.get(position);
		ImageUtil.asyncDownloadImageAndShow(holder.imgView, item.snap,context,busy);
		holder.name.setText(item.prod_name);
		refreshInfo(holder, item);
		holder.plus.setOnClickListener(new OnClickListener() {
			public void onClick(View self) {
				item.quantity++;
				updateInfo(holder, item);
			}
		});
		holder.minus.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(item.quantity>1)
				item.quantity--;
				updateInfo(holder, item);
			}
		});
		holder.delete.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				shoppingCartService.removeItem(item);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	private static class ViewHolder{
			public TextView name,unitPriceNquantity,totalPrice;
			public ImageView imgView;
			public ImageButton plus,minus;
			public Button delete;
	}
	private void refreshInfo(ViewHolder holder,CartItem item){
		holder.unitPriceNquantity.setText("￥"+item.present_price+" x "+item.quantity);
		holder.totalPrice.setText("总计:￥"+String.format("%.2f",(item.present_price*item.quantity)));
		if(item.quantity<=1) holder.minus.setEnabled(false);
		else holder.minus.setEnabled(true);
	}
	private void updateInfo(ViewHolder holder,CartItem item){
		holder.unitPriceNquantity.setText("￥"+item.present_price+" x "+item.quantity);
		holder.totalPrice.setText("总计:￥"+String.format("%.2f",(item.present_price*item.quantity)));
		if(item.quantity<=1) holder.minus.setEnabled(false);
		else holder.minus.setEnabled(true);
		updateTotalCost();
	}
	
	public void notifyDataSetChanged() {
		updateTotalCost();updateSubmitButton();
		super.notifyDataSetChanged();
	}
	
}
