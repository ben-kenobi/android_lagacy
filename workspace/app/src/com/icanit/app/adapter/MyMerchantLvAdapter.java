package com.icanit.app.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app.R;
import com.icanit.app.entity.AppMerchant;
import com.icanit.app.util.ImageUtil;

public class MyMerchantLvAdapter extends MyBaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private List<AppMerchant> merchantList=new ArrayList<AppMerchant>();
	private int resId=R.layout.item4lv_fragment4home_01_merchant;
	public MyMerchantLvAdapter(Context context){
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if(merchantList==null||merchantList.isEmpty()) return 0;
		return merchantList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return merchantList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final AppMerchant merchant = merchantList.get(position);
		ViewHolder holder=null;
		if(convertView==null){
			convertView=inflater.inflate(resId, parent,false);
			holder=new ViewHolder();
			holder.snap=(ImageView)convertView.findViewById(R.id.imageView1);
			holder.detail=(Button)convertView.findViewById(R.id.button1);
			holder.merName=(TextView)convertView.findViewById(R.id.textView1);
			holder.minCost=(TextView)convertView.findViewById(R.id.textView2);
			holder.phone=(TextView)convertView.findViewById(R.id.textView3);
			holder.address=(TextView)convertView.findViewById(R.id.textView4);
			holder.rating=(RatingBar)convertView.findViewById(R.id.ratingBar1);
			convertView.setTag(holder);
			Log.d("errorTag",position+" @MerchantLVAdapter inner");
		}else holder=(ViewHolder)convertView.getTag();
		Log.d("errorTag",position+" @MerchantLVAdapter outer"+","+parent.getClass());
		ImageUtil.asyncDownloadImageAndShow(holder.snap,merchant.pic,context,busy);
//		Animator anim = AnimatorInflater.loadAnimator(context, R.animator.object_animator);
		holder.merName.setText(merchant.merName);
		holder.minCost.setText("ÆðËÍ:£¤"+merchant.minCost);
		holder.phone.setText(merchant.phone);
		holder.address.setText(merchant.location);
		holder.rating.setRating(3.8f);
		holder.detail.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Toast.makeText(context, merchant.merName+" ÏêÇé",Toast.LENGTH_SHORT).show();
			}
		});
		return convertView;
	}
	
	static class ViewHolder{
		ImageView snap;
		TextView merName,minCost,phone,address;
		 Button detail;
		 RatingBar rating;
	}

	public List<AppMerchant> getMerchantList() {
		return merchantList;
	}

	public void setMerchantList(List<AppMerchant> merchantList) {
		this.merchantList = merchantList;
	}



	
}
