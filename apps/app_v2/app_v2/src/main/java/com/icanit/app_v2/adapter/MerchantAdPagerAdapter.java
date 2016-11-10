package com.icanit.app_v2.adapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.activity.MerchandizeListActivity;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.util.ImageUtil;

public class MerchantAdPagerAdapter extends MyBasePagerAdapter{

	private LayoutInflater inflater;
	private Context context;
	private List<AppMerchant> merchantList;
	private int resId=R.layout.item4vp_advertisement;
	private Map<Integer,View> viewMap=new HashMap<Integer,View>();
	private LinkedList<View> convertViews=new LinkedList<View>();
	
	public MerchantAdPagerAdapter(Context context) throws AppException{
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}
	
	public int getCount() {
//		Log.d("pagerAdapter","getCount");
		if(merchantList==null)
		return 0;  return merchantList.size();
	}

	public boolean isViewFromObject(View arg0, Object arg1) {
//		Log.d("pagerAdapter","arg0="+arg0+",arg1="+arg1+"   isViewFromObject");
		return arg0==arg1;
	}
	
	
	@Override
	public void finishUpdate(ViewGroup container) {
//		Log.d("pagerAdapter","container="+container+"   finishUpdate");
		super.finishUpdate(container);
	}

	@Override
	public int getItemPosition(Object object) {
//		Log.d("pagerAdapter","object="+object+"   getItemPosition");
//		return super.getItemPosition(object);
		return POSITION_NONE;
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
//		Log.d("pagerAdapter","container="+container+",position="+position+",object="+object+"   setPrimaryItem");
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public void startUpdate(ViewGroup container) {
//		Log.d("pagerAdapter","container="+container+"   startUpdate");
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
		final ViewHolder holder;final AppMerchant merchant= merchantList.get(position);
		View convertView=null;
		if(convertView==null&&!convertViews.isEmpty()) convertView=convertViews.removeFirst();
		if(convertView==null){
			holder=new ViewHolder();
			convertView=inflater.inflate(resId, container,false);
			holder.imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			convertView.setTag(holder);
		}else holder=(ViewHolder)convertView.getTag();
		ImageUtil.asyncDownloadImageAndShow(holder.imageView, "ad_"+position+".jpg",context,busy);
		viewMap.put(position,convertView);
		holder.imageView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent(context,MerchandizeListActivity.class).putExtra(IConstants.MERCHANT_KEY, 
						merchantList.get(position));
				context.startActivity(intent);
			}
		});
		container.addView(convertView);
//		System.out.println(position+",convertViews="+convertViews+",convertView="+convertView+" instantiateItem @MerchantAdPagerAdapter");
		return convertView;
	}
	public void updateView(ViewPager pager,int position){
		if(position!=-1&&!viewMap.containsKey(position))return ;
		System.out.println(position+",viewMap="+viewMap+",contains="+viewMap.containsKey(position)+"  @MerchantAdpagerAdapter");
		Iterator<Entry<Integer,View>> it=viewMap.entrySet().iterator();
		while(it.hasNext()){
			Entry<Integer,View> en = it.next();
			AppMerchant merchant= merchantList.get(en.getKey());
			
			ViewHolder holder = (ViewHolder)en.getValue().getTag();
		}
	}
	public List<AppMerchant> getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(List<AppMerchant> merchantList) {
		this.merchantList = merchantList;
		notifyDataSetChanged();
	}
	public void refreshViewPager(){
		
	}
	
	static class ViewHolder{
		ImageView imageView;
	}
	
}
