package com.icanit.app_v2.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppCategory;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.sqlite.ShoppingCartService;
import com.icanit.app_v2.util.AnimationUtil;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.ImageUtil;

public final class MyMerchandiseLvAdapter extends MyBaseAdapter  {
	private Context context;
	private LayoutInflater inflater;
	private List<AppGoods> goodsList=new ArrayList<AppGoods>();
	private List<AppGoods> goodsListByCate = new ArrayList<AppGoods>();
	private int resId = R.layout.item4lv_merchandize;
	private AppCategory cate = new AppCategory(1,"全部",0,0,0);
	private ShoppingCartService shoppingCartService;
	private TextView totalQuantity;
	private ViewGroup container;
	private ImageView animateImage;
	private AppMerchant merchant;
	
	public MyMerchandiseLvAdapter(Context context,TextView totalQuantity,ViewGroup container,
			ImageView animateImage,AppMerchant merchant) throws AppException {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
		shoppingCartService = AppUtil.getServiceFactory()
				.getShoppingCartServiceInstance(context);
		this.merchant=merchant;
		this.totalQuantity=totalQuantity;this.container=container;this.animateImage=animateImage;
	}

	public void classifyGoodsListByCateId(AppCategory newcate) {
		if (newcate == null || this.cate != null && newcate.id == this.cate.id)
			return;
		this.cate = newcate;
		copyGoodsToListByCate(cate.id);
	}
	public void updateCartItemQuantity(){
		int count = shoppingCartService.getItemsCount();
		if(count==0){
			totalQuantity.setVisibility(View.GONE);
		}else{
			totalQuantity.setText(count+"");
			if(totalQuantity.getVisibility()!=View.VISIBLE)
				totalQuantity.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public int getCount() {
		if (goodsListByCate == null)
			return 0;
		return goodsListByCate.size();
	}

	@Override
	public Object getItem(int position) {
		if (goodsListByCate == null || goodsListByCate.isEmpty())
			return null;
		return goodsListByCate.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final AppGoods goods = goodsListByCate.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(resId, parent, false);
			holder.snap = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.goodsName = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.curPrice = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.info=(TextView)convertView.findViewById(R.id.textView3);
			holder.amount=(TextView)convertView.findViewById(R.id.textView4);
			holder.snapRim=(FrameLayout)convertView.findViewById(R.id.frameLayout1);
			holder.add=(Button)convertView.findViewById(R.id.button1);
			holder.minus=(Button)convertView.findViewById(R.id.button2);
			Paint paint = holder.curPrice.getPaint();
			paint.setTypeface(Typeface.create(Typeface.SANS_SERIF,
					Typeface.BOLD_ITALIC));
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		ImageUtil.asyncDownloadImageAndShow(holder.snap, goods.pic, context,
				busy);
		holder.goodsName.setText(goods.goodName);
		holder.curPrice.setText("￥" + goods.curPrice + "/个");
		holder.info.setText("基础信息：XXxx");
		holder.amount.setText("数量："+goods.amount);
		shoppingCartService.updateAddNminusButton(holder.add, holder.minus, goods.id);
		holder.snapRim.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					context.getClass().getMethod("showMerchandizeDetail", AppGoods.class)
					.invoke(context, goods);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		holder.add.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				try {
					AnimationUtil.startBuyingAnimation(animateImage, container, holder.snap,
							totalQuantity, context);
				} catch (Exception e) {
					e.printStackTrace();
				}
				shoppingCartService.addCartItem(holder.add, holder.minus, goods,merchant);
				updateCartItemQuantity();
			}
		});
		holder.minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shoppingCartService.decreaseCartItem(holder.add, holder.minus, goods.id);
				updateCartItemQuantity();
			}
		});
		return convertView;
	}

	public void setGoodsList(List<AppGoods> goodsList) {
		this.goodsList = goodsList;
		copyGoodsToListByCate(1);
	}

	public List<AppGoods> getGoodsList() {
		return goodsList;
	}

	public void copyGoodsToListByCate(int cateId) {
		if(goodsListByCate==null||goodsList==null)  return;
		goodsListByCate.clear();
		for (int i = 0; i < goodsList.size(); i++) {
			AppGoods goods = goodsList.get(i);
			if (cateId==1||goods.cateId == cateId)
				goodsListByCate.add(goods);
		}
		notifyDataSetChanged();
	}
	public void searchGoods(CharSequence keyword){
		if(goodsListByCate==null||goodsList==null)  return;
		goodsListByCate.clear();
		for (int i = 0; i < goodsList.size(); i++) {
			AppGoods goods = goodsList.get(i);
			if (goods.goodName.contains(keyword))
				goodsListByCate.add(goods);
		}
		notifyDataSetChanged();
	}

	public List<AppGoods> getGoodsListByCate() {
		return goodsListByCate;
	}

	static class ViewHolder {
		ImageView snap;
		TextView goodsName, curPrice,info,amount;
		FrameLayout snapRim;
		Button add,minus;
	}


}
