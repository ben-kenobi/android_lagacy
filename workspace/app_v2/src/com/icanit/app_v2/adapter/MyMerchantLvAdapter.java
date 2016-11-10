package com.icanit.app_v2.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.ImageUtil;
import com.icanit.app_v2.util.SortUtil;
import com.icanit.app_v2.util.SortUtil.ComparatorParamPair;

public class MyMerchantLvAdapter extends MyBaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<AppMerchant> merchantList;
	private List<AppMerchant> screenedMerchantList = new ArrayList<AppMerchant>();
	private int resId = R.layout.item4lv_merchant;
	private ComparatorParamPair cpp;
	private boolean deliveryOnly = false, reservationOnly = false;
	private SortUtil.MerchantComparator comparator = new SortUtil.MerchantComparator();

	public MyMerchantLvAdapter(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	public void sortBy(ComparatorParamPair cpp,boolean forceToRefresh) throws NoSuchFieldException {
		if (cpp == null || cpp.equals(this.cpp)&&!forceToRefresh)
			return;
		this.cpp = cpp;
		comparator.setComparatorParamPair(this.cpp);
		Collections.sort(screenedMerchantList, comparator);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (screenedMerchantList == null || screenedMerchantList.isEmpty())
			return 0;
		return screenedMerchantList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return screenedMerchantList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AppMerchant merchant = screenedMerchantList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(resId, parent, false);
			holder = new ViewHolder();
			holder.snap = (ImageView) convertView.findViewById(R.id.imageView1);
			holder.merName = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.address = (TextView) convertView
					.findViewById(R.id.textView2);
			holder.mincost = (TextView) convertView
					.findViewById(R.id.textView3);
			convertView.setTag(holder);
			Log.d("errorTag", position + " @MerchantLVAdapter inner");
		} else
			holder = (ViewHolder) convertView.getTag();
		if(TextUtils.isEmpty(merchant.logo)){
			holder.snap.setImageResource(R.drawable.mer_default);
		}else{
			ImageUtil.asyncDownloadImageAndShow(holder.snap, merchant.logo, context,
				busy);
		}
		holder.merName.setText(merchant.merName);
		holder.address.setText("位置:"+merchant.location);
		Log.d("errorTag",
				position + " @MerchantLVAdapter outer" + ","
						+ parent.getClass());
		holder.mincost.setText("起送价格：￥"+AppUtil.formatMoney(merchant.deliverPrice));
		return convertView;
	}

	static class ViewHolder {
		ImageView snap;
		TextView merName, address, mincost;
	}

	public List<AppMerchant> getMerchantList() {
		return merchantList;
	}
	
	public List<AppMerchant> getScreenedMerchantList() {
		return screenedMerchantList;
	}

	public void setMerchantList(List<AppMerchant> merchantList) {
		this.merchantList = merchantList;
		deliveryOnly=true;
		screenMerchantList(false, false);
	}

	public void screenMerchantList(boolean deliveryOnly, boolean reservationOnly) {
		if (this.deliveryOnly == deliveryOnly
				&& this.reservationOnly == reservationOnly||merchantList==null||screenedMerchantList==null)
			return;
		this.deliveryOnly = deliveryOnly;
		this.reservationOnly = reservationOnly;
		screenedMerchantList.clear();
		for (int i = 0; i < merchantList.size(); i++) {
			AppMerchant mer = merchantList.get(i);
			if (mer.deliverPrice < 0 && deliveryOnly )
				continue;
			screenedMerchantList.add(mer);
		}
		notifyDataSetChanged();
	}
	public void searchMerchantList(CharSequence searchKey){
		if(screenedMerchantList==null||merchantList==null) return;
		screenedMerchantList.clear();
		for(int i=0;i<merchantList.size();i++){
			AppMerchant mer = merchantList.get(i);
			if(mer.merName.contains(searchKey))
				screenedMerchantList.add(mer);
		}
		Collections.sort(screenedMerchantList,comparator);
		notifyDataSetChanged();
	}

}
