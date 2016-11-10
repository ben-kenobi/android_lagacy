package com.yf.accountmanager.adapter;

import java.io.File;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.FileManager;
import com.yf.accountmanager.common.FileManager.FileComparator;
import com.yf.accountmanager.common.FileManager.IFileFilter;

public class OptionListAdapter extends BaseAdapter{
	
	
	private int resId=R.layout.item4lv_simpletext;
	
	private Context context;
	
	public String[] optionList;
	
	public int type;
	
	public File file;
	
	public View targetView;
	
	public int curSortType,curFilterType;
	
	public OptionListAdapter(Context context){
		super();
		this.context=context;
	}
	
	public String getDeleteNotice(){
		if(file==null) return "";
		if(file.isDirectory())
			return context.getString(R.string.deleteDirNotice, file.getName());
		else 
			return context.getString(R.string.deleteFileNotice, file.getName());
	}
	public void setType(File file,TextView title,int type,View targetView,FileComparator comparator,IFileFilter filter){
		this.type=type;
		if(filter!=null)
			this.curFilterType=filter.type;
		if(comparator!=null)
			this.curSortType=comparator.type;
		if(type==FileManager.TYPE_SORT){
			this.file=null;
			title.setText("排序方式");
			optionList=FileManager.SORTOPTIONS;
		}else if(type==FileManager.TYPE_SCREEN){
			this.file=null;
			title.setText("只查看");
			optionList=FileManager.SCREENOPTIONS;
		}else{
			this.file=file;
			title.setText(file.getName());
			optionList=FileManager.FILEOPTIONS;
		}
		this.targetView=targetView;
		notifyDataSetChanged();
	}
	
	
	
	@Override
	public int getCount() {
		if(optionList==null) return 0;
		return optionList.length;
	}

	@Override
	public Object getItem(int position) {
		if(optionList==null) return null;
		return optionList[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(resId, parent,false);
		}
		
		if(type==FileManager.TYPE_SCREEN&&curFilterType==position||
				type==FileManager.TYPE_SORT&&curSortType==position){
			((TextView)convertView).setTextColor(Color.rgb(0x55, 0x55, 0xdd));
		}else 
			((TextView)convertView).setTextColor(Color.rgb(0,0,0));
		((TextView)convertView).setText(optionList[position]);
		return convertView;
	}

}
