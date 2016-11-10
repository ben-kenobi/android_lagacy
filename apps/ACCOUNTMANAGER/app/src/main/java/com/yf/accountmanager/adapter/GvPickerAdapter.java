package com.yf.accountmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;
import com.yf.filesystem.FileOperationService.ShareViewHolder;

public class GvPickerAdapter extends BaseAdapter{
	private Context context;
	public Cursor cursor;
	private int resId=R.layout.item4gv_shareitem;
	
	private Drawable icon;
	
	private String columnName;
	
	public GvPickerAdapter(Context context){
		super();
		this.context=context;
	}
	
	public String getColumnName(){
		return columnName;
	}
	
	@Override
	public int getCount() {
		if(cursor==null) return 0;
		return cursor.getCount();
	}

	@Override
	public Object getItem(int position) {
		if(cursor.moveToPosition(position))
			return cursor;
		return null;
	}

	@Override
	public long getItemId(int position) {
//		cursor.moveToPosition(position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(!cursor.moveToPosition(position)) return null;
		ShareViewHolder holder = null;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId, parent,false);
			holder=new ShareViewHolder(convertView);
		}else 
			holder=(ShareViewHolder)convertView.getTag();
		if(icon!=null)
		holder.icon.setImageDrawable(icon);
		String str=cursor.getString(0);
		holder.label.setText(TextUtils.isEmpty(str)?"Î´Ö¸¶¨":str);
		return convertView;
	}
	
	
	
	private Drawable getItemIconByPlatform(String platform,String colName) {
		Drawable drawable = null;
		if (IConstants.ACCOUNT.equals(platform)) {
			if(AccountColumns.PHONENUM.equals(colName))
				drawable = context.getResources().getDrawable(R.drawable.phonenum);
			else if(AccountColumns.SITENAME.equals(colName))
				drawable = context.getResources().getDrawable(R.drawable.aticon);
			else if(AccountColumns.PASSPORT.equals(colName))
				drawable = context.getResources().getDrawable(R.drawable.passport);
			else if(AccountColumns.MAILBOX.equals(colName))
				drawable = context.getResources().getDrawable(R.drawable.mailbox);
			else 
				drawable = context.getResources().getDrawable(R.drawable.categories);
				
		} else if (IConstants.CONTACTS.equals(platform)) {
			drawable= context.getResources().getDrawable(R.drawable.group);
		}
		return drawable;
	}

	
	public void requery(String platform,String columnName){
		if(TextUtils.isEmpty(platform)){
			changeCursor(null);
		}else if(TextUtils.isEmpty(columnName)){
			icon=getItemIconByPlatform(platform,this.columnName);
				changeCursor(CommonService.queryDistinctColumnByPlatform(
						platform, this.columnName));
		}else{
			this.columnName = columnName;
			icon=getItemIconByPlatform(platform,this.columnName);
			Cursor cursor = CommonService.queryDistinctColumnByPlatform(
					platform, columnName);
			changeCursor(cursor);
		}
	}
	
	private void changeCursor(Cursor c){
		if(cursor!=null)cursor.close();
		cursor=c;
		notifyDataSetChanged();
	}
	
}
