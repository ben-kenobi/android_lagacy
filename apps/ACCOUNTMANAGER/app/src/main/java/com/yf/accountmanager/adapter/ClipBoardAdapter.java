package com.yf.accountmanager.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yf.accountmanager.R;
import com.yf.accountmanager.util.FileUtils;
import com.yf.filesystem.FileOperationService.ShareViewHolder;

public class ClipBoardAdapter extends BaseAdapter {
	public static final int TYPE_MOVE = 1, TYPE_COPY = 0;

	private int resId = R.layout.item4gv_shareitem;

	private List<File> fileList;

	public ArrayList<File> selectedFiles;

	private Context context;

	private int type;

	public ClipBoardAdapter(Context context) {
		this.context = context;
		selectedFiles = new ArrayList<File>();
	}

	public ClipBoardAdapter setFileList(List<File> files, int type) {
		this.fileList = files;
		selectedFiles.clear();
		this.type = type;
		return this;
	}

	
	public void toggleSelected(int position){
		if(position<0||position>getCount()-1) return;
		File file =fileList.get(position);
		if(selectedFiles.contains(file)){
			deselectedAll();
		}else{
			selectedAll();
		}
		
	}
	
	public void selectedAll(){
		selectedFiles.clear();
		for(int i=0;i<fileList.size();i++){
			selectedFiles.add(fileList.get(i));
		}
		notifyDataSetChanged();
	}
	public void deselectedAll(){
		selectedFiles.clear();
		notifyDataSetChanged();
	}
	public void reset() {
		for(int i=0;i<fileList.size();i++){
			File file =fileList.get(i);
			if(!file.exists()||!file.canRead()){
				fileList.remove(file);
				i--;
			}
		}
		selectedFiles.clear();
		notifyDataSetChanged();
	}

	public void onItemClick(int position, View self) {
		if (position < 0 || position >= getCount())
			return;
		File file = fileList.get(position);
		if (selectedFiles.contains(file)) {
			selectedFiles.remove(file);
			self.setActivated(false);
		} else {
			selectedFiles.add(file);
			self.setActivated(true);
		}
	}

	public void selectedRemove() {
		if(selectedFiles.isEmpty()) return ;
		for (int i = 0; i < selectedFiles.size(); i++) {
			fileList.remove(selectedFiles.get(i));
		}
		selectedFiles.clear();
		notifyDataSetChanged();
	}


	public int getCount() {
		if (fileList == null)
			return 0;
		return fileList.size();
	}

	public Object getItem(int position) {
		if (fileList == null)
			return null;
		return fileList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ShareViewHolder holder = null;
		if (convertView == null) {
			holder = new ShareViewHolder(convertView = LayoutInflater.from(context)
					.inflate(resId, parent, false));
		} else
			holder = (ShareViewHolder) convertView.getTag();
		File file = fileList.get(position);
		if (type == TYPE_COPY) {
			holder.label.setEnabled(false);
		} else if (type == TYPE_MOVE) {
			convertView.setEnabled(false);
		}

		if (selectedFiles.contains(file)) {
			convertView.setActivated(true);
		} else {
			convertView.setActivated(false);
		}

		FileUtils.setFileIconByType(file, holder.icon, holder.label);
		return convertView;
	}

}
