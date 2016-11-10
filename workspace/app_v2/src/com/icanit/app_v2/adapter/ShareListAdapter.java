package com.icanit.app_v2.adapter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.icanit.app_v2.R;

public class ShareListAdapter extends BaseAdapter{
	Context context;
	public List<ResolveInfo> activityList;
	public Intent intent;
	int resId=R.layout.item4gv_shareitem;
	public ShareListAdapter(Context context){
		super();
		this.context=context;
		intent=new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		activityList=context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		ResolveInfo.DisplayNameComparator comparator=new ResolveInfo.DisplayNameComparator(context.getPackageManager());
		Collections.sort(activityList,comparator);
		activityList.add(0, null);activityList.add(0,null);
	}
	@Override
	public int getCount() {
		if(activityList==null)return 0;
		return activityList.size();
	}

	@Override
	public Object getItem(int position) {
		if(activityList==null)return null;
		return activityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId, container,false);
		}
		if(position==0){
			ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			TextView tv=(TextView)convertView.findViewById(R.id.textView1);
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.sinamb_logo));
			tv.setText("分享到新浪微博");
		}else if(position==1){
			ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			TextView tv=(TextView)convertView.findViewById(R.id.textView1);
			imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.tencentmb_logo));
			tv.setText("分享到腾讯微博");
		}else{
			ResolveInfo ri=activityList.get(position);
			ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			TextView tv=(TextView)convertView.findViewById(R.id.textView1);
			imageView.setImageDrawable(ri.loadIcon(context.getPackageManager()));
			tv.setText(ri.loadLabel(context.getPackageManager()));
		}
		return convertView;
	}

}
