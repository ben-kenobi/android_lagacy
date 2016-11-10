package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppCommunity;
import com.icanit.app_v2.util.PinyinUtil;

public class CommunityLvAdapter  extends MyBaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private int resId = R.layout.item4lv_community_pop1;
	private List<AppCommunity> communities;
	public CommunityLvAdapter(Context context){
		super();
		this.context=context;inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		if(communities==null||communities.isEmpty())return 0;
		return communities.size();
	}

	@Override
	public Object getItem(int arg0) {
		return communities.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;AppCommunity community = communities.get(position);
		if(convertView!=null)  holder=(ViewHolder)convertView.getTag();
		if(convertView==null||holder==null){
			convertView = inflater.inflate(resId, parent,false);
			holder = new ViewHolder();
			holder.commName=(TextView)convertView.findViewById(R.id.textView1);
			holder.count=(TextView)convertView.findViewById(R.id.textView2);
		}
		
		holder.commName.setText(community.commName);
		holder.count.setText("1");
//		if(false){
//			holder.count.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow,0);
//		}else{
//			holder.count.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
//		}
		return convertView;
	}
	static class ViewHolder{
		public TextView commName,count;
	}
	public List<AppCommunity> getCommunities() {
		return communities;
	}

	public void setCommunities(List<AppCommunity> communities) {
		this.communities = communities;
		notifyDataSetChanged();
	}
	public int getPositionByAlpha(String alpha){
		if(communities==null) return 0;
		for(int i=0;i<communities.size();i++){
			AppCommunity comm=communities.get(i);
			if(PinyinUtil.toPinyin(comm.commName.charAt(0)).startsWith(alpha))
				return i;
		}
		return 0;
	}
	
}
