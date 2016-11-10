package com.yf.accountmanager.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.common.FileManager.FileComparator;
import com.yf.accountmanager.common.FileManager.IFileFilter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.filesystem.FileOperationService;
import com.yf.filesystem.FileOperationService.ShareViewHolder;

public class FileListAdapter extends BaseAdapter {

	private Context context;
	private int resId = R.layout.item4gv_shareitem;

	public List<File>  fileList, copyList ,
			moveList ;

	public File parent;
	public File[] files;

	private File selectedFile;
	private View selectedView;

	private TextView parentLabel;

	public boolean fileSelectedMode = false;

	public FileComparator comparator;

	public IFileFilter filter;

	public FileListAdapter(Context context, TextView parentLabel) {
		super();
		this.context = context;
		this.parentLabel = parentLabel;
		parentLabel.setText("");
		comparator = new FileComparator();
		filter = new IFileFilter();
		copyList = new ArrayList<File>();
		moveList = new ArrayList<File>();
		fileList = new ArrayList<File>();
	}

	// changeMode
	public void setFileSelectedMode(boolean b, View confirm, AdapterView av,
			View up) {
		comparator.setType(0);
		filter.setType(0);
		fileSelectedMode = b;
		changeDir(new File(File.separator), confirm, av, up);
	}

	// copy
	public boolean addToCopyList(File file, View convertView) {
		return FileOperationService.addToCopyList(file, convertView, copyList, moveList);
	}

	// move
	public boolean  addToMoveList(File file, View convertView) {
		return FileOperationService.addToMoveList(file, convertView, parent, copyList, moveList);
	}

	// sortBYType
	public void sort(int type) {
		if (comparator.setType(type) && !fileList.isEmpty()) {
			Collections.sort(fileList, comparator);
			notifyDataSetChanged();
		}
	}

	// screenByType
	public void screen(int type, View confirm) {
		if (filter.setType(type)) {
			screenNsort(confirm);
		}
	}

	public File getSelectedFile() {
		if (fileSelectedMode)
			return selectedFile;
		return parent;
	}

	@Override
	public int getCount() {
		if (fileList == null)
			return 0;
		return fileList.size();
	}

	@Override
	public Object getItem(int position) {
		if (fileList == null)
			return null;
		return fileList.get(position);
	}

	@Override
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

		if (moveList.contains(file)) {
			convertView.setEnabled(false);
		} else {
			convertView.setEnabled(true);
		}

		if (copyList.contains(file))
			holder.label.setEnabled(false);
		else
			holder.label.setEnabled(true);

		if (file.equals(selectedFile)) {
			convertView.setActivated(true);
			selectedView = convertView;
		} else
			convertView.setActivated(false);
		FileUtils.setFileIconByType(file, holder.icon, holder.label);
		return convertView;
	}

	// refresh
	public void refresh(View confirm) {
		changeDirectory(parent, confirm, null, null, false);
	}

	// changeDir
	public boolean changeDir(File file, View confirm, AdapterView av, View up) {
		return changeDirectory(file, confirm, av, up, true);
	}

	// dirChanged
	public boolean changeDirectory(File file, View confirm, AdapterView av,
			View up, boolean resetSort) {
		if (!file.exists())
			return false;
		if (file.isDirectory()) {
			if (resetSort) {
				filter.setType(0);
				comparator.setType(0);
			}
			this.parent = file;
			parentLabel.setText(TextUtils.isEmpty(parent.getName()) ? "ROOT"
					: parent.getName());
			files = file.listFiles();
			if (av != null && getCount() > 0)
				av.setSelection(0);
			if (up != null)
				up.setEnabled(parent.getParent() != null);
			screenNsort(confirm);
			return true;
		}
		return false;
	}

	// screenNsort
	public void screenNsort(View confirm) {
		changeSelectedFile(null, null, confirm);
		fileList.clear();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (filter.accept(f))
					fileList.add(f);
			}
			Collections.sort(fileList, comparator);
		}
		notifyDataSetInvalidated();
	}

	// levelUp
	public boolean levelUp(View up, View confirm, AdapterView av) {
		boolean b = false;
		if (parent == null) {
			up.setEnabled(false);
		} else {
			File file = parent.getParentFile();
			if (file == null)
				up.setEnabled(false);
			else {
				b = changeDir(file, confirm, av, up);
			}
		}
		return b;
	}

	// levelDown
	public boolean levelDown(View up, View self, int position, View confirm,
			AdapterView av) {
		boolean b = false;
		File file = fileList.get(position);
		if (file.isDirectory()) {
			b = changeDir(file, confirm, av, up);
		} else if (!fileSelectedMode) {

		} else if (file.isFile()) {
			changeSelectedFile(file, self, confirm);
		} else {
			changeSelectedFile(null, null, confirm);
		}
		return b;
	}

	// changeSelectedFile
	private void changeSelectedFile(File file, View self, View confirm) {
		if (!fileSelectedMode) {
			selectedFile = null;
			if (selectedView != null)
				selectedView.setActivated(false);
			selectedView = null;
			if (confirm != null)
				confirm.setEnabled(true);
		} else if (file != null && file.equals(selectedFile)) {
			selectedFile = null;
			selectedView = null;
			if (self != null)
				self.setActivated(false);
			if (confirm != null)
				confirm.setEnabled(false);
		} else {
			selectedFile = file;
			if (selectedView != null)
				selectedView.setActivated(false);
			if (self != null)
				self.setActivated(file != null);
			if (confirm != null)
				confirm.setEnabled(file != null);
			selectedView = self;
		}
	}

	// mkdir
	public String mkDir(String dirname, View confirm, View up) {
		int result = FileUtils.mkDir(parent, dirname);
		if (result == -1)
			return IConstants.FILE_ALREADY_EXISTS;
		else if (result == 1) {
			File file = new File(parent.getPath() + File.separator + dirname);
			changeDir(file, confirm, null, up);
			return IConstants.MKDIR_SUCCESS;
		} else
			return IConstants.MKDIR_FAIL;
	}

	
	//deleteFileAsyn
	public void deleteFileAsynchronized(final File file,final View confirm,Context context){
		if(!parent.canWrite()){
			if(file.isDirectory())
				CommonUtils.toast("无法删除该文件夹");
			else
				CommonUtils.toast("无法删除该文件");
			return ;
		}
		final ProgressDialog pd= CustomizedDialog.createProgressDialogNshow(null, "删除文件。。。", false, context);
		IConstants.THREAD_POOL.submit(new Runnable(){
			public void run(){
				final boolean b =FileUtils.deleteFileRecursively(file, false);
				IConstants.MAIN_HANDLER.post(new Runnable(){
					public void run(){
						if(b){
							refresh(confirm);
							CommonUtils.toast(IConstants.DELETE_SUCCESS);
						}else 
							CommonUtils.toast(IConstants.DELETE_FAIL);
					}
				});
				pd.dismiss();
			}
		});
	}

	// renameFile
	public int renameFile(File file, String newname, View confirm) {
		int result= FileOperationService.renameFile(file, newname);
		if(result==1)
			refresh(confirm);
		return result;
	}

	// pasteFile
	public boolean  pasteFiles(ClipBoardAdapter copy, ClipBoardAdapter move,
			Context context) {
		return FileOperationService.pasteFiles(copy, move, context, parent, this);
	}
	public boolean zipFiles(ClipBoardAdapter copy, ClipBoardAdapter move,String name,
			Context context){
		return FileOperationService.deflateFiles(copy, move, context, parent,name, this);
	}

}
