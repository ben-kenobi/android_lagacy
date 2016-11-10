package com.yf.accountmanager.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;

public class AccountListAdapter extends BaseUnstableCursorAdapter{
	private int resId=R.layout.item4lv_account;
	
	
	public AccountListAdapter(Context context, View deleteButton) {
		super(context,deleteButton);
	}
	
	

	@Override
	public void bindView(View convertView, Context context, Cursor c) {
		convertView.setActivated(selectedIds.contains(c.getInt(c.getColumnIndex(AccountColumns.ID))));
		ViewHolder holder=(ViewHolder)convertView.getTag();
		holder.site.setText("SITE : ");
		String group=c.getString(c.getColumnIndex(AccountColumns.GROUP));
		holder.sitename.setText(c.getString(c.getColumnIndex(AccountColumns.SITENAME))
				+"   (  "+(TextUtils.isEmpty(group)?"未分组":group)+"  )");
		String username =c.getString(c.getColumnIndex(AccountColumns.USERNAME));
		String password=c.getString(c.getColumnIndex(AccountColumns.PASSWORD));
		String passport =c.getString(c.getColumnIndex(AccountColumns.PASSPORT));
		String mailbox =c.getString(c.getColumnIndex(AccountColumns.MAILBOX));
		String phoneNum =c.getString(c.getColumnIndex(AccountColumns.PHONENUM));
		StringBuffer info = new StringBuffer();
		if(!TextUtils.isEmpty(username))
			info.append("用户名:"+username+"\n");
		if(!TextUtils.isEmpty(password))
			info.append("密码  :"+password+"\n");
		if(!TextUtils.isEmpty(passport))
			info.append("通行证:"+passport+"\n");
		if(!TextUtils.isEmpty(mailbox))
			info.append("邮箱  :"+mailbox+"\n");
		if(!TextUtils.isEmpty(phoneNum))
			info.append("手机号:"+phoneNum+"\n");
		if(info.length()>2)
			info.deleteCharAt(info.length()-1);
		holder.info.setText(info.toString());
	}

	@Override
	public View newView(Context context, Cursor c, ViewGroup parent) {
		View v=LayoutInflater.from(context).inflate(resId,parent,false);
		new ViewHolder(v);
		return v;
	}
	class ViewHolder{
		public ViewHolder(View v){
			site=(TextView)v.findViewById(R.id.textView1);
			sitename=(TextView)v.findViewById(R.id.textView2);
			info=(TextView)v.findViewById(R.id.textView3);
			v.setTag(this);
		}
		public TextView site,sitename,info;
	}
	
}
