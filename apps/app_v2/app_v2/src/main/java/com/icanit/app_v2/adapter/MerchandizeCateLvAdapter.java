package com.icanit.app_v2.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppCategory;

public class MerchandizeCateLvAdapter  extends MyBaseAdapter{
	public static final int[] colorCycle=new int[]{Color.rgb(0xff, 0xaa, 0xaa),Color.rgb(0xee,0xee,0),
		Color.rgb(0xc1,0xff,0xc1),Color.rgb(0x90, 0xee, 0x90),Color.rgb(0xa4, 0xd3, 0xee)};
	private int resId=R.layout.item4lv_mer_cates;
	private Context context;
	private List<AppCategory> merCateList ;
	private LayoutInflater inflater;
	public MerchandizeCateLvAdapter(Context context){
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}
	public int getCount() {
		if(merCateList==null) return 0;
		return merCateList.size();
	}

	public Object getItem(int arg0) {
		return merCateList.get(arg0);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		AppCategory cate =merCateList.get(position);
		if(convertView!=null)
		holder=(ViewHolder)convertView.getTag();
		if(convertView==null||holder==null){
			convertView = inflater.inflate(resId, parent,false);
			holder=new ViewHolder();
			holder.cateName=(TextView)convertView.findViewById(R.id.textView1);
			holder.count=(TextView)convertView.findViewById(R.id.textView2);
			holder.color=convertView.findViewById(R.id.view1);
		}
		
		holder.cateName.setText(cate.cateName);
		holder.count.setText(cate.count+"");
		holder.color.setBackgroundColor(colorCycle[position%colorCycle.length]);
		return convertView;
	}
	static class ViewHolder{
		public TextView cateName,count;
		public View color;
	}
	public List<AppCategory> getMerCateList() {
		return merCateList;
	}
	public void setMerCateList(List<AppCategory> cateList) {
		this.merCateList = cateList;
		this.merCateList.add(new AppCategory(1,"È«²¿",0,0,calculateTotalCount(merCateList)));
		Collections.sort(this.merCateList, new Comparator<AppCategory>() {
			public int compare(AppCategory lhs, AppCategory rhs) {
				return lhs.id-rhs.id;
			}
		});
		notifyDataSetChanged();
	}
	private int calculateTotalCount(List<AppCategory> cateList){
		if(cateList==null) return 0;
		int count=0;
		for(int i=0;i<cateList.size();i++){
			count+=cateList.get(i).count;
		}
		return count;
	}
	
}
