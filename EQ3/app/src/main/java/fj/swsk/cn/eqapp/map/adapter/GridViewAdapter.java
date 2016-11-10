package fj.swsk.cn.eqapp.map.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import fj.swsk.cn.eqapp.R;

public class GridViewAdapter extends BaseAdapter implements ListAdapter {
	
	private String[] mList;
	private Context mContex;
	
	public GridViewAdapter(Context context, String[] list) {
		this.mContex = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.length;
	}

	@Override
	public Object getItem(int position) {
		return mList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContex, R.layout.item_gridview, null);
			viewHolder.item = (TextView) convertView.findViewById(R.id.item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String label = mList[position];
		viewHolder.item.setText(label);
		return convertView;
	}
	
	class ViewHolder{
		TextView item;
	}
}
