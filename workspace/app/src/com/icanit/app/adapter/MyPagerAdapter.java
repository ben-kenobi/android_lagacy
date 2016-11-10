package com.icanit.app.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.support.v4.view.ViewPager;
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

public class MyPagerAdapter extends MyBasePagerAdapter{
	private LayoutInflater inflater;
	private Context context;
	private List<AppGoods> goodsList;
	private ShoppingCartService shoppingCartService;
	private int resId=R.layout.item4pager_merchandize;
	private MyPagerAdapter self = this;
	private Map<Integer,View> viewMap=new HashMap<Integer,View>();
	private LinkedList<View> convertViews=new LinkedList<View>();
	public MyPagerAdapter(Context context) throws AppException{
		super();
		this.context=context;inflater=LayoutInflater.from(context);
		shoppingCartService=AppUtil.getServiceFactory().getShoppingCartServiceInstance(context);
	}

	public int getCount() {
		if(goodsList==null)
		return 0;  return goodsList.size();
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0==arg1;
	}
	
	
	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return "商品"+position;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		viewMap.remove(position);
		container.removeView((View)object);
		convertViews.add((View)object);
		System.out.println(position+",object="+object+",convertViews="+convertViews+"  destoryItem  @MyPagerAdapter");
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final ViewHolder holder;final AppGoods goods= goodsList.get(position);
		View convertView=null;
		if(convertView==null&&!convertViews.isEmpty()) convertView=convertViews.removeFirst();
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, container,false);
			holder.add=(Button)convertView.findViewById(R.id.button1);
			holder.minus=(Button)convertView.findViewById(R.id.button2);
			holder.checkDetail=(Button)convertView.findViewById(R.id.button3);
			holder.detail=(TextView)convertView.findViewById(R.id.textView1);
			holder.merName=(TextView)convertView.findViewById(R.id.textView2);
			holder.curPrice=(TextView)convertView.findViewById(R.id.textView3);
			holder.duePrice=(TextView)convertView.findViewById(R.id.textView4);
			holder.hot=(TextView)convertView.findViewById(R.id.textView5);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
			Log.d("errorTag",position+" @PagerAdapter convertView==null");
		}else holder=(ViewHolder)convertView.getTag();
		Log.d("errorTag",position+" @PagerAdapter convertView!=null");
		
		
		shoppingCartService.updateAddNminusButton(holder.add, holder.minus,goods.id);
		ImageUtil.asyncDownloadImageAndShow(holder.imageView, goods.pic,context,busy);
		holder.detail.setText(goods.detail);holder.merName.setText(goods.goodName);
		holder.curPrice.setText("现价:￥"+goods.curPrice);holder.duePrice.setText("原价:￥"+goods.duePrice);
		holder.hot.setText(goods.hot+"");
		
		holder.add.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shoppingCartService.addCartItem(holder.add, holder.minus,  goods);
			}
		});
		holder.minus.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				shoppingCartService.minusCartItem(holder.add, holder.minus,goods.id);
			}
		});
		holder.checkDetail.setOnClickListener(new  OnClickListener() {
			public void onClick(View v) {
				//TODO Detail
				
				
//				Intent intent=new Intent(context,ProductDetailsActivity.class);
//				Toast.makeText(context, "详情加载...", 1000).show();
//				context.startActivity(intent);
			}
		});
		viewMap.put(position,convertView);
		container.addView(convertView);
		System.out.println(position+",convertViews="+convertViews+",convertView="+convertView+" instantiateItem @MyPagerAdapter");
		return convertView;
	}
	public void updateView(ViewPager pager,int position){
		if(position!=-1&&!viewMap.containsKey(position))return ;
		System.out.println(position+",viewMap="+viewMap+",contains="+viewMap.containsKey(position)+"  @pagerAdapter");
		Iterator<Entry<Integer,View>> it=viewMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer,View> en = it.next();
			AppGoods goods= goodsList.get(en.getKey());
			ViewHolder holder = (ViewHolder)en.getValue().getTag();
			shoppingCartService.updateAddNminusButton(holder.add, holder.minus, goods.id);
		}
	}
	public List<AppGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<AppGoods> goodsList) {
		this.goodsList = goodsList;
	}
	
	static class ViewHolder{
		TextView detail,merName,curPrice,duePrice,hot;
		Button add,minus,checkDetail;
		ImageView imageView;
	}

}
