package com.icanit.app_v2.adapter;

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
import android.widget.ImageView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppGoods;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.util.ImageUtil;

public class MerchandizeAdPagerAdapter extends MyBasePagerAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<AppGoods> goodsList;
	private int resId=R.layout.item4vp_advertisement;
	private Map<Integer,View> viewMap=new HashMap<Integer,View>();
	private LinkedList<View> convertViews=new LinkedList<View>();
	
	public MerchandizeAdPagerAdapter(Context context) throws AppException{
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}
	
	public int getCount() {
		Log.d("pagerAdapter","getCount");
		if(goodsList==null)
		return 0;  return goodsList.size();
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
		Log.d("pagerAdapter","arg0="+arg0+",arg1="+arg1+"   isViewFromObject");
		return arg0==arg1;
	}
	
	
	@Override
	public void finishUpdate(ViewGroup container) {
		Log.d("pagerAdapter","container="+container+"   finishUpdate");
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
		Log.d("pagerAdapter","object="+object+"   getItemPosition");
//		return super.getItemPosition(object);
		return POSITION_NONE;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		Log.d("pagerAdapter","container="+container+",position="+position+",object="+object+"   setPrimaryItem");
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
		Log.d("pagerAdapter","container="+container+"   startUpdate");
		super.startUpdate(container);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return null;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		viewMap.remove(position);
		container.removeView((View)object);
		convertViews.add((View)object);
//		System.out.println(position+",object="+object+",convertViews="+convertViews+"  destoryItem  @MerchantAdPagerAdapter");
	}

	@Override
	public Object instantiateItem(ViewGroup container,final  int position) {
		final ViewHolder holder;final AppGoods goods= goodsList.get(position);
		View convertView=null;
		if(convertView==null&&!convertViews.isEmpty()) convertView=convertViews.removeFirst();
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, container,false);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
			Log.d("errorTag",position+" @MerchandizeAdPagerAdapter convertView==null");
		}else holder=(ViewHolder)convertView.getTag();
		Log.d("errorTag",position+" @MerchantdizeAdPagerAdapter convertView!=null");
		ImageUtil.asyncDownloadImageAndShow(holder.imageView, "",context,busy);
		viewMap.put(position,convertView);
		holder.imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					context.getClass().getMethod("showMerchandizeDetail", AppGoods.class)
					.invoke(context, goods);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		container.addView(convertView);
//		System.out.println(position+",convertViews="+convertViews+",convertView="+convertView+" instantiateItem @MerchantAdPagerAdapter");
		return convertView;
	}
	public void updateView(ViewPager pager,int position){
		if(position!=-1&&!viewMap.containsKey(position))return ;
		System.out.println(position+",viewMap="+viewMap+",contains="+viewMap.containsKey(position)+"  @MerchandizeAdpagerAdapter");
		Iterator<Entry<Integer,View>> it=viewMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer,View> en = it.next();
			AppGoods goods =goodsList.get(en.getKey());
			ViewHolder holder = (ViewHolder)en.getValue().getTag();
		}
	}
	public List<AppGoods> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<AppGoods> goodsList) {
		this.goodsList = goodsList;
		notifyDataSetChanged();
	}
	public void refreshViewPager(){
		
	}
	
	static class ViewHolder{
		ImageView imageView;
	}
	
}
