package com.icanit.app.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app.R;
import com.icanit.app.entity.AppGoods;
import com.icanit.app.exception.AppException;
import com.icanit.app.sqlite.ShoppingCartService;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.ImageUtil;

public final  class MyMerchandiseLvAdapter  extends MyBaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<AppGoods> goodsList;
	private int resId=R.layout.item4lv_merchandize;
	private ShoppingCartService shoppingCartService;
	private MyMerchandiseLvAdapter self = this;
	public MyMerchandiseLvAdapter(Context context) throws AppException {
		super();
		this.context=context;inflater=LayoutInflater.from(context);
		shoppingCartService = AppUtil.getServiceFactory().getShoppingCartServiceInstance(context);
	}
	@Override
	public int getCount() {
		Log.d("errorTag","getCount @myMerchandiseLvAdapter");
		if(goodsList==null) return 0;
		return goodsList.size();
	}

	@Override
	public Object getItem(int position) {
		if(goodsList==null||goodsList.isEmpty()) return null;
		return goodsList.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;final AppGoods goods=goodsList.get(position);
		if(convertView ==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, parent,false);
			holder.snap=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.detail=(TextView)convertView.findViewById(R.id.textView1);
			holder.goodsName=(TextView)convertView.findViewById(R.id.textView2);
			holder.curPrice=(TextView)convertView.findViewById(R.id.textView3);
			holder.duePrice=(TextView)convertView.findViewById(R.id.textView4);
			holder.hot=(TextView)convertView.findViewById(R.id.textView5);
			holder.amount=(TextView)convertView.findViewById(R.id.textView6);
			holder.add=(Button)convertView.findViewById(R.id.button1);
			holder.minus=(Button)convertView.findViewById(R.id.button2);
			convertView.setTag(holder);
			Log.d("errorTag",position+" @MerchandiseLVAdapter inner"+","+holder.snap.getBackground());
		}else 
			holder=(ViewHolder)convertView.getTag();
		Log.d("errorTag",position+" @MerchandiseLVAdapter outer");
		ImageUtil.asyncDownloadImageAndShow(holder.snap, goods.pic,context,busy);
		holder.detail.setText(goods.detail);holder.goodsName.setText(goods.goodName);
		holder.curPrice.setText("￥"+goods.curPrice);holder.duePrice.setText("￥"+goods.duePrice);
		holder.amount.setText("数量:"+goods.amount);holder.hot.setText("热度:"+goods.hot);
		holder.add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shoppingCartService.addCartItem(holder.add, holder.minus, goods);
			}
		});
		holder.minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shoppingCartService.minusCartItem(holder.add, holder.minus,goods.id);
			}
		});
		shoppingCartService.updateAddNminusButton(holder.add, holder.minus, goods.id);
		return convertView;
	}
	
	

	public List<AppGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<AppGoods> goodsList) {
		this.goodsList = goodsList;
	}



	static class ViewHolder{
		ImageView snap;
		TextView detail,goodsName,curPrice,duePrice,hot,amount;
		 Button add,minus;
	}





}
