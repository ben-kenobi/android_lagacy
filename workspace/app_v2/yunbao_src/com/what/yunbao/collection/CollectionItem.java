package com.what.yunbao.collection;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.what.yunbao.R;
import com.what.yunbao.util.Notes.CollectColumns;

public class CollectionItem extends LinearLayout{
	private TextView name_tv;
	private TextView addr_tv;
	private TextView distance_tv;
	public CheckBox checkBox;
	private ImageView arrow;
	private CollectionItemData mItemData;

	
	public CollectionItem(Context context,OnCheckedChangeListener listener) {
		super(context);
		inflate(context,R.layout.business_item,this);
		name_tv = (TextView) findViewById(R.id.tv_business_item_name);
		addr_tv = (TextView) findViewById(R.id.tv_business_item_addr);
		distance_tv = (TextView) findViewById(R.id.tv_business_item_distance);
		checkBox = (CheckBox) findViewById(android.R.id.checkbox);
		checkBox.setOnCheckedChangeListener(listener);
		arrow = (ImageView) findViewById(R.id.iv_collection_arrow);
	} 

	public void onBind(boolean choiceMode,boolean checked ,Cursor cursor){
		checkBox.setTag(cursor.getLong(cursor.getColumnIndex(CollectColumns.ID)));
		if (choiceMode) {		
			arrow.setVisibility(View.GONE);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(checked);
            
        } else {
        	arrow.setVisibility(View.VISIBLE);
        	checkBox.setVisibility(View.GONE);
        }
		name_tv.setText(cursor.getString(cursor.getColumnIndex(CollectColumns.MERNAME)));
		addr_tv.setText(cursor.getString(cursor.getColumnIndex(CollectColumns.LOCATION)));
		double mincost=cursor.getDouble(cursor.getColumnIndex(CollectColumns.MINCOST));
		distance_tv.setText(mincost>0?"起送价：￥"+mincost:"不支持送货");
	}
	public CollectionItemData getItemData(){
		return mItemData;
	}
}
