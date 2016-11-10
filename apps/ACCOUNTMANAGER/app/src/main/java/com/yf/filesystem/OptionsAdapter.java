package com.yf.filesystem;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.sqlite.ISQLite.IPathColumns;

public class OptionsAdapter extends BaseAdapter {
	
	public static final int FILEOPTION=1,
			
							IPATHOPTION=2;	
	
	public static final String[] OPTIONS1 = new String[] { "delete",
		"rename", "copy", "move", "detail" ,"selectAll","multiselectorMode" };
	
	public static final String[] OPTIONS2=new String[]{
		"取消标记","修改备注","全部选中","进入多选模式"
	};

	private int resId = R.layout.item4lv_simpletext;

	private Context context;

	public String[] options;


	public File file;

	public View targetView;
	
	public int type;


	public OptionsAdapter(Context context) {
		super();
		this.context = context;
	}

	public String getDeleteNotice() {
		if (file == null)
			return "";
		if (file.isDirectory())
			return context.getString(R.string.deleteDirNotice, file.getName());
		else
			return context.getString(R.string.deleteFileNotice, file.getName());
	}
	
	public void init(Cursor cursor,File file,TextView title,View targetView,boolean multiselectorMode){
		if(file!=null){
			setFile(file,title,targetView,multiselectorMode);
		}else{
			setIpath(cursor,title,targetView,multiselectorMode);
		}
	}
	
	public void setIpath(Cursor cursor,TextView title,View targetView,boolean multiselectorMode){
		if(cursor==null) return ;
		options=OPTIONS2;
		type=IPATHOPTION;
		title.setText(cursor.getString(cursor.getColumnIndex(IPathColumns.NAME)));
		this.targetView=targetView;
		if(multiselectorMode){
			options[options.length-1]="退出多选模式";
		}else{
			options[options.length-1]="进入多选模式";
		}
		notifyDataSetChanged();
	}

	public void setFile(File file, TextView title, View targetView,boolean multiselectorMode) {
		options = OPTIONS1;
		type=FILEOPTION;
		this.file = file;
		title.setText(file.getName());
		this.targetView = targetView;
		if(multiselectorMode){
			options[options.length-1]="退出多选模式";
		}else{
			options[options.length-1]="进入多选模式";
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (options == null)
			return 0;
		return options.length;
	}

	@Override
	public Object getItem(int position) {
		if (options == null)
			return null;
		return options[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resId, parent,
					false);
		}
		((TextView) convertView).setText(options[position]);
		return convertView;
	}

}
