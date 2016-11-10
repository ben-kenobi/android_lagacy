package com.icanit.app_v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.CartItem;
import com.icanit.app_v2.entity.MerchantCartItems;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.sqlite.ShoppingCartService;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.ImageUtil;

public class CartItemListAdapterSub_sub extends MyBaseAdapter {
	private LayoutInflater inflater;
	private Context context;
	private MerchantCartItems mc;
	private int resId=R.layout.item4lv_cartiteminfo_x;
	private ShoppingCartService shoppingCartService;
	private CartItemListAdapterSub parentAdapter;
	public CartItemListAdapterSub_sub(Context context,MerchantCartItems mc,CartItemListAdapterSub parentAdapter) throws AppException {
		this.context=context;
		inflater=LayoutInflater.from(this.context);
		shoppingCartService=AppUtil.getServiceFactory().getShoppingCartServiceInstance(context);
		this.parentAdapter=parentAdapter;
		this.mc=mc;
	}
	
	
	public int getCount() {
		if(mc==null) return 0;
		return mc.items.size();
	}

	public Object getItem(int arg0) {
		return mc.items.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}
	
	
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final CartItem item = mc.items.get(position);
		if(convertView==null){
			holder = new ViewHolder();convertView=inflater.inflate(resId,parent,false);
			convertView.setTag(holder);
			holder.snap=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.goodsName=(TextView)convertView.findViewById(R.id.textView1);
			holder.count=(TextView)convertView.findViewById(R.id.textView2);
			holder.cost=(TextView)convertView.findViewById(R.id.textView3);
			holder.add=(Button)convertView.findViewById(R.id.button2);
			holder.minus=(Button)convertView.findViewById(R.id.button1);
//			holder.postScript=(EditText)convertView.findViewById(R.id.editText1);
		}else holder=(ViewHolder)convertView.getTag();
		ImageUtil.asyncDownloadImageAndShow(holder.snap, item.snap, context, busy);
		holder.goodsName.setText(item.prod_name);
		holder.cost.setText("гд"+item.present_price);
		holder.count.setText(item.quantity+"");
//		holder.postScript.setText(item.postscript==null||"null".equals(item.postscript)?"":item.postscript);
//		holder.postScript.setOnFocusChangeListener(new  OnFocusChangeListener() {
//			public void onFocusChange(View v, boolean focused) {
//				if(!focused){
//					item.postscript=holder.postScript.getText().toString();
//				}
//			}
//		});
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
					shoppingCartService.removeItem(item,mc);
					parentAdapter.notifyDataSetChanged();
				}
			}
		});
		return convertView;
	}
	public void updateInfo(ViewHolder holder,CartItem item){
		holder.count.setText(item.quantity+"");
		parentAdapter.updateTotalCost();
	}
	
	class ViewHolder	{
		public ImageView snap;
		public TextView goodsName,count,cost;
		public Button add,minus;
//		public EditText postScript;
	}
	@Override
	public void notifyDataSetChanged() {
		parentAdapter.updateSubmitButton();parentAdapter.updateTotalCost();
		super.notifyDataSetChanged();
	}
}
