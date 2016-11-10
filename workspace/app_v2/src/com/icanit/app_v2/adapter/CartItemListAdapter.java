package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.sqlite.ShoppingCartService;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.ImageUtil;

public class CartItemListAdapter extends MyBaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private List<CartItem> itemList;
	private int resId=R.layout.item4lv_cartiteminfo;
	private TextView totalCost;
	private Button confirmation;
	private ShoppingCartService shoppingCartService;
	public CartItemListAdapter(Context context,TextView tv,Button confirmation) throws AppException {
		this.context=context;
		itemList=AppUtil.appContext.shoppingCartList;
		inflater=LayoutInflater.from(this.context);
		totalCost=tv;shoppingCartService=AppUtil.getServiceFactory().getShoppingCartServiceInstance(context);
		this.confirmation=confirmation;updateTotalCost();
		updateSubmitButton();
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
	private  void updateTotalCost(){
		totalCost.setText("×Ü½ð¶î:£¤"+String.format("%.2f", AppUtil.calculateTotalCost(itemList)));
	}
	private void updateSubmitButton(){
		Log.w("cartInfo","itemList="+itemList+"  @CartItemListAdapter  updateSubmitButton");
		if(itemList==null||itemList.isEmpty()){
			confirmation.setEnabled(false);
		}else {
			confirmation.setEnabled(true);
		}
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final CartItem item = itemList.get(position);
//		if(convertView!=null) holder=(ViewHolder)convertView.getTag();
		if(convertView==null){
			holder = new ViewHolder();convertView=inflater.inflate(resId,parent,false);
			convertView.setTag(holder);
			holder.snap=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.goodsName=(TextView)convertView.findViewById(R.id.textView1);
			holder.count=(TextView)convertView.findViewById(R.id.textView2);
			holder.cost=(TextView)convertView.findViewById(R.id.textView3);
			holder.add=(Button)convertView.findViewById(R.id.button2);
			holder.minus=(Button)convertView.findViewById(R.id.button1);
			holder.postScript=(EditText)convertView.findViewById(R.id.editText1);
		}else holder=(ViewHolder)convertView.getTag();
		ImageUtil.asyncDownloadImageAndShow(holder.snap, item.snap, context, busy);
		holder.goodsName.setText(item.prod_name);
		holder.cost.setText("£¤"+item.present_price);
		holder.count.setText(item.quantity+"");
		holder.postScript.setText(item.postscript==null||"null".equals(item.postscript)?"":item.postscript);
		holder.postScript.setOnFocusChangeListener(new  OnFocusChangeListener() {
			public void onFocusChange(View v, boolean focused) {
				if(!focused){
					item.postscript=holder.postScript.getText().toString();
				}
			}
		});
		holder.add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				item.quantity++;
				updateInfo(holder, item);
			}
		});
		holder.minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(item.quantity>1) {
					item.quantity--;
					updateInfo(holder, item);
				}else{
					shoppingCartService.removeItem(item,null);
					notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}
	public void updateInfo(ViewHolder holder,CartItem item){
		holder.count.setText(item.quantity+"");
		updateTotalCost();
	}
	
	class ViewHolder	{
		public ImageView snap;
		public TextView goodsName,count,cost;
		public Button add,minus;
		public EditText postScript;
	}

	public void notifyDataSetChanged() {
		updateSubmitButton();updateTotalCost();
		super.notifyDataSetChanged();
	}
	
	public void onConfirmationButonClicked() {
		
		
	}
}
