package com.yf.filesystem;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.yf.accountmanager.R;
import com.yf.accountmanager.sqlite.IPathService;
import com.yf.accountmanager.sqlite.ISQLite.IPathColumns;
import com.yf.accountmanager.util.FileUtils;
import com.yf.filesystem.FileOperationService.ShareViewHolder;

public class IPathAdapter extends CursorAdapter{
	
	private int resId=R.layout.item4gv_shareitem;
	
	public boolean multiselectorMode;
	
	public Set<Integer> selectedIds;
	
	public ImageButton remove;
	
	public IPathAdapter(Context context,ImageButton remove) {
		super(context, null,true);
		selectedIds=new HashSet<Integer>();
		this.remove=remove;
	}

	public void refresh(){
		changeCursor(IPathService.query());
	}
	
	public void reset(){
		changeCursor(null);
	}
	
	public boolean  switchMode(){
		if(multiselectorMode)
			normalMode();
		else
			multiselectorMode();
		notifyDataSetChanged();
		return multiselectorMode;
	}
	public void multiselectorMode(){
		selectedIds.clear();
		remove.setVisibility(View.VISIBLE);
		multiselectorMode=true;
	}
	public void normalMode(){
		selectedIds.clear();
		remove.setVisibility(View.GONE);
		multiselectorMode=false;
	}
	
	
	public void selectedAll(){
		if(getCount()==0) return ;
		multiselectorMode();
		Cursor cursor = getCursor();
		if(cursor.moveToFirst()){
			do{
				selectedIds.add(getId());
			}while(cursor.moveToNext());
		}
		notifyDataSetChanged();
		
	}
	
	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ShareViewHolder holder = (ShareViewHolder)convertView.getTag();
		String name=cursor.getString(cursor.getColumnIndex(IPathColumns.NAME));
		String path = cursor.getString(cursor.getColumnIndex(IPathColumns.PATH));
		if(multiselectorMode&&selectedIds.contains(getId())){
			convertView.setActivated(true);
		}else{
			convertView.setActivated(false);
		}
		
		holder.label.setText(TextUtils.isEmpty(name)?"ROOT":name);
		FileUtils.setFileIconByType(path, holder.icon, null);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View convertView =LayoutInflater.from(context).inflate(resId, parent,false);
		new ShareViewHolder(convertView);
		return convertView;
	}
	
	
	public void handleOption(int position,String name,View self){
		switch(position){
		case 0:
			if(IPathService.deleteById(getId()))
					refresh();
			break;
		case 1:
			if(IPathService.update(name, null, getId()))
				refresh();
			break;
		case 2:
			selectedAll();
			break;
		case 3:
			int id=getId();
			if(switchMode()){
				selectedIds.add(id);
				self.setActivated(true);
			}
			break;
		default: break;
		}
	}
	
	public int getId(){
		return getCursor().getInt(getCursor().getColumnIndex(IPathColumns.ID));
	}
	
	public File onItemClick(View self){
		File file = null;
		if (multiselectorMode) {
			int id = getId();
			if(selectedIds.contains(id)){
				selectedIds.remove(id);
				self.setActivated(false);
			}else {
				selectedIds.add(id);
				self.setActivated(true);
			}
		} else {
			String path =getCursor().getString(getCursor().getColumnIndex(IPathColumns.PATH));
			file = new File(path);
		}
		return file;
	}
	
}
