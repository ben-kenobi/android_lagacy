package com.icanit.app_v2.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.icanit.app_v2.R;
import com.icanit.app_v2.entity.AppArea;
import com.icanit.app_v2.entity.AppCommunity;

public class CommunityDistrictLvAdapter extends BaseAdapter {

	public static final int TYPE_AREA = 1, TYPE_COMM = 2;

	private Context context;
	private LayoutInflater inflater;
	private List<AppArea> areaList;
	private List<AppCommunity> commList;
	private int resId = R.layout.item4lv_community_pop1;
	public int selected = -1;
	public int type;
	private int selectedColor = Color.rgb(0xee, 0xee, 0xee);

	public CommunityDistrictLvAdapter(Context context, int type) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.type = type;
	}

	@Override
	public int getCount() {
		if (type == TYPE_AREA) {
			if (areaList == null)
				return 0;
			return areaList.size();
		} else if (type == TYPE_COMM) {
			if (commList == null)
				return 0;
			return commList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (type == TYPE_AREA) {
			if (areaList == null)
				return null;
			return areaList.get(position);
		} else if (type == TYPE_COMM) {
			if (commList == null)
				return 0;
			return commList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView != null)
			holder = (ViewHolder) convertView.getTag();
		if (convertView == null || holder == null) {
			convertView = inflater.inflate(resId, parent, false);
			holder = new ViewHolder();
			holder.district = (TextView) convertView
					.findViewById(R.id.textView1);
			holder.count = (TextView) convertView.findViewById(R.id.textView2);
		}
		if(type==TYPE_AREA){
			AppArea area = areaList.get(position);
			holder.district.setText(area.areaName);
		}else if(type==TYPE_COMM){
			AppCommunity comm=commList.get(position);
			holder.district.setText(comm.commName);
		}
		if (position == selected)
			convertView.setBackgroundColor(selectedColor);
		else
			convertView.setBackgroundColor(Color.argb(0, 0, 0, 0));
		holder.count.setText("");
		if (true) {
			holder.count.setCompoundDrawablesWithIntrinsicBounds(0, 0,
					R.drawable.ic_arrow_small, 0);
		} else {
			holder.count.setCompoundDrawablesWithIntrinsicBounds(null, null,
					null, null);
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView district, count;
	}

	public int getSelected() {
		return selected;
	}

	public void setSelected(int selected) {
		this.selected = selected;
		notifyDataSetInvalidated();
	}

	public List<AppArea> getAreaList() {
		return areaList;
	}
	public List<AppCommunity> getCommList(){
		return commList;
	}

	public void setAreaList(List<AppArea> areaList) {
		this.areaList = areaList;
		notifyDataSetChanged();
	}
	public void setCommList(List<AppCommunity> commList) {
		this.commList = commList;
		notifyDataSetChanged();
	}

	public void setSelectedColor(int color) {
		this.selectedColor = color;
	}

	public AppArea getSelectedAppArea() {
		if (selected < 0||areaList==null)
			return null;
		return areaList.get(selected);
	}

	public AppCommunity getSelectedAppComm() {
		if (selected < 0||commList==null)
			return null;
		return commList.get(selected);
	}

	public String getSelectedAreaName() {
		if (selected < 0)
			return "";
		if (type == TYPE_AREA)
			return areaList.get(selected).areaName;
		else if (type == TYPE_COMM)
			return commList.get(selected).commName;
		return "";
	}

}
