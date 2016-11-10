package com.yf.filesystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.adapter.ClipBoardAdapter;
import com.yf.accountmanager.common.FileManager.FileComparator;
import com.yf.accountmanager.common.FileManager.IFileFilter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.IPathService;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.SystemUtil;
import com.yf.filesystem.FileOperationService.ShareViewHolder;

public class FileSystemAdapter extends BaseAdapter{

	private Context context;

	private int resId = R.layout.item4gv_shareitem;

	public List<File> fileList, copyList, moveList, selectedList;

	public File parent;

	public File[] files;

	private TextView parentLabel;

	public boolean multiselectorMode = false;

	public FileComparator comparator;

	public IFileFilter filter;

	private Drawable favorite, discard;

	private ImageButton toggleMode;
	
	private View up,dmc;
	
	private AdapterView av;

	public FileSystemAdapter(Context context, TextView label,
			ImageButton toggleMode,View up,View dmc,AdapterView av) {
		super();
		this.context = context;
		this.parentLabel = label;
		this.toggleMode = toggleMode;
		this.up = up;
		this.av=av;
		this.dmc=dmc;
		parentLabel.setText("");
		comparator = new FileComparator();
		filter = new IFileFilter();
		copyList = new ArrayList<File>();
		moveList = new ArrayList<File>();
		fileList = new ArrayList<File>();
		selectedList = new ArrayList<File>();
		int size = context.getResources().getDimensionPixelSize(R.dimen.dp_30);
		Rect rect = new Rect(0, 0, size, size);
		favorite = context.getResources().getDrawable(R.drawable.favorite);
		discard = context.getResources().getDrawable(R.drawable.discard);
		favorite.setBounds(rect);
		discard.setBounds(rect);
		parentLabel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (parent == null)
					return;
				if (IPathService.containPath(parent.getAbsolutePath())) {
					if (IPathService.deleteBYPath(parent.getAbsolutePath())) {
						parentLabel.setCompoundDrawables(discard, null, null,
								null);
					}
				} else {
					if (IPathService.insert(parent)) {
						parentLabel.setCompoundDrawables(favorite, null, null,
								null);
					}
				}
			}
		});
	}

	// changeMode
	public boolean toggleMode() {
		if (multiselectorMode) {
			normalMode();
		} else {
			multiselectorMode();
		}
		notifyDataSetChanged();
		return multiselectorMode;
	}

	public void multiselectorMode() {
		selectedList.clear();
		multiselectorMode = true;
		toggleMode.setImageResource(R.drawable.file_explorer);
		dmc.setVisibility(View.VISIBLE);
	}

	public void normalMode() {
		selectedList.clear();
		multiselectorMode = false;
		toggleMode.setImageResource(R.drawable.multiselector);
		dmc.setVisibility(View.GONE);
	}

	public void selectedAll() {
		multiselectorMode();
		for (int i = 0; i < fileList.size(); i++) {
			selectedList.add(fileList.get(i));
		}
		notifyDataSetChanged();
	}

	public void updateSelectedList() {
		for (int i = 0; i < selectedList.size(); i++) {
			File file = selectedList.get(i);
			if (!file.exists()) {
				selectedList.remove(file);
				i--;
			}
		}
	}

	
	public List<File>  getSelectedList(){
		updateSelectedList();
		return selectedList;
	}

	// copy
	public boolean addToCopyList(File file, View convertView) {
		return FileOperationService.addToCopyList(file, convertView, copyList, moveList);
	}

	// move
	public boolean  addToMoveList(File file, View convertView) {
		return FileOperationService.addToMoveList(file, convertView, parent, copyList, moveList);
	}

	public void addSelectedToCopyList() {
		updateSelectedList();
		if (selectedList.isEmpty()) {
			CommonUtils.toast("无选中文件");
			return;
		}
		int count = 0;
		for (int i = 0; i < selectedList.size(); i++) {
			File file = selectedList.get(i);
			if (file.canRead()) {
				if (!copyList.contains(file)) {
					if (moveList.contains(file)) {
						moveList.remove(file);
					}
					copyList.add(file);
				}
				count++;
			}
		}
		if (count == selectedList.size()) {
			CommonUtils.toast("复制 " + count + " 个文件");
		} else {
			CommonUtils.toast("复制 " + count + " 个文件, 忽略 "
					+ (selectedList.size() - count) + " 个不可读取文件");
		}
		refresh(true);

	}

	public void addSelectedToMoveList() {
		updateSelectedList();
		if (selectedList.isEmpty()) {
			CommonUtils.toast("无选中文件");
			return;
		}
		if (!parent.canWrite()) {
			CommonUtils.toast("当前位置下的文件不可移动");
		} else {
			int count = 0;
			for (int i = 0; i < selectedList.size(); i++) {
				File file = selectedList.get(i);
				if (file.canRead() ) {
					if (!moveList.contains(file)) {
						if (copyList.contains(file)) {
							copyList.remove(file);
						}
						moveList.add(file);
					}
					count++;
				}

			}
			if (count == selectedList.size()) {
				CommonUtils.toast("剪切 " + count + " 个文件");
			} else {
				CommonUtils.toast("剪切 " + count + " 个文件, 忽略 "
						+ (selectedList.size() - count) + " 个不可读取的文件");
			}
		}
		refresh(true);

	}

	// sortBYType
	public boolean sort(int type) {
		if (comparator.setType(type) && !fileList.isEmpty()) {
			Collections.sort(fileList, comparator);
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	// screenByType
	public boolean screen(int type) {
		if (filter.setType(type)) {
			screenNsort(true);
			return true;
		}
		return false;
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

		if (selectedList.contains(file)) {
			convertView.setActivated(true);
		} else
			convertView.setActivated(false);
		FileUtils.setFileIconByType(file, holder.icon, holder.label);
		return convertView;
	}

	// refresh
	public void refresh(boolean normalMode) {
		changeDirectory(parent,  false,normalMode);
	}

	// changeDir
	public boolean changeDir(File file) {
		return changeDirectory(file,  true,true);
	}

	// dirChanged
	public boolean changeDirectory(File file, boolean resetSort,boolean normalMode) {
		if (!file.exists())
			return false;
		if (file.isDirectory()) {
			if (resetSort) {
				filter.setType(0);
				comparator.setType(0);
			}
			this.parent = file;
			updateParentLabel();
			files = file.listFiles();
			if (av != null && getCount() > 0)
				av.setSelection(0);
			if (up != null)
				up.setEnabled(parent.getParent() != null);
			screenNsort(normalMode);
			return true;
		}
		return false;
	}

	public void updateParentLabel() {
		if (parentLabel == null)
			return;
		parentLabel.setText(TextUtils.isEmpty(parent.getName()) ? "ROOT"
				: parent.getName());
		if (IPathService.containPath(parent.getAbsolutePath()))
			parentLabel.setCompoundDrawables(favorite, null, null, null);
		else
			parentLabel.setCompoundDrawables(discard, null, null, null);
	}

	// screenNsort
	public void screenNsort(boolean normalMode) {
		fileList.clear();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				File f = files[i];
				if (filter.accept(f))
					fileList.add(f);
			}
			Collections.sort(fileList, comparator);
		}
		if (normalMode) {
			normalMode();
		}
		notifyDataSetInvalidated();
	}

	// levelUp
	public boolean levelUp() {
		boolean b = false;
		if (parent == null) {
			up.setEnabled(false);
		} else {
			File file = parent.getParentFile();
			if (file == null)
				up.setEnabled(false);
			else {
				b = changeDir(file);
			}
		}
		return b;
	}

	
	
	
	
	public int onItemClicked( View self, int position) {
		File file = fileList.get(position);
		if (multiselectorMode) {
			if (selectedList.contains(file)) {
				selectedList.remove(file);
				self.setActivated(false);
			} else {
				selectedList.add(file);
				self.setActivated(true);
			}
		} else {
			if (file.isDirectory()) {
				changeDir(file);
			}else if(FileUtils.isReadableFile(file)){
				int type =FileUtils.getFileTYpe(file);
				if(type>=0&&type<5){
//					SystemUtil.openAs(file, type, context);
					return type;
				}else
					SystemUtil.forwardToFileContentViewer(file, context);
			}else{
				CommonUtils.toast("无法读取文件");
			}
		}
		return -1;
	}


	public void selectFile(File file, View self) {
		if (multiselectorMode) {
			if (selectedList.contains(file)) {
				selectedList.remove(file);
				self.setActivated(false);
			} else {
				selectedList.add(file);
				self.setActivated(true);
			}
		}
	}

	// mkdir
	public String mkDir(String dirname) {
		if(!parent.canWrite())
			return "当前文件夹无法创建";
		int result = FileUtils.mkDir(parent, dirname);
		if (result == -1)
			return IConstants.FILE_ALREADY_EXISTS;
		else if (result == 1) {
			refresh(false);
			return IConstants.MKDIR_SUCCESS;
		} else
			return IConstants.MKDIR_FAIL;
	}
	//createNewFile
	public String createNewFile(String filename) throws IOException{
		if(!parent.canWrite())
			return "当前文件夹无法创建";
		int result = FileUtils.createNewFile(parent, filename);
		if (result == -1)
			return IConstants.FILE_ALREADY_EXISTS;
		else if (result == 1) {
			refresh(false);
			return IConstants.MKDIR_SUCCESS;
		} else
			return IConstants.MKDIR_FAIL;
	}

	// deleteFileAsyn
	public void deleteFileAsynchronized(final File file, Context context) {
		if (!parent.canWrite()) {
			if (file.isDirectory())
				CommonUtils.toast("无法删除该文件夹");
			else
				CommonUtils.toast("无法删除该文件");
			return;
		}
		final ProgressDialog pd = CustomizedDialog.createProgressDialogNshow(
				null, "删除文件。。。", false, context);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				final boolean b = FileUtils.deleteFileRecursively(file, false);
				IConstants.MAIN_HANDLER.post(new Runnable() {
					public void run() {
						if (b) {
							refresh(false);
							CommonUtils.toast(IConstants.DELETE_SUCCESS);
						} else
							CommonUtils.toast(IConstants.DELETE_FAIL);
					}
				});
				pd.dismiss();
			}
		});
	}

	public void deleteSelectedListAsynchronized(Context context) {
		if (selectedList.isEmpty())
			return;
		if (!parent.canWrite()) {
			return;
		}
		final ProgressDialog pd = CustomizedDialog.createProgressDialogNshow(
				null,  context
				.getString(R.string.multiDeleteNotice), false, context);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				boolean b = true;
				for (int i = 0; i < selectedList.size(); i++) {
					b &= FileUtils.deleteFileRecursively(selectedList.get(i),
							false);
				}
				final boolean boo = b;
				IConstants.MAIN_HANDLER.post(new Runnable() {
					public void run() {
						if (boo)
							CommonUtils.toast(IConstants.DELETE_SUCCESS);
						else
							CommonUtils.toast(IConstants.DELETE_FAIL);
						refresh(true);
					}
				});
				pd.dismiss();
			}
		});
	}

	// renameFile
	public int renameFile(File file, String newname) {
		int result = FileOperationService.renameFile(file, newname);
		if(result==1) refresh(false);
		return result;
	}

	// pasteFile
	public boolean pasteFiles(ClipBoardAdapter copy, ClipBoardAdapter move,
			Context context) {
		return FileOperationService.pasteFiles(copy, move, context, parent, this);
	}
	
	public boolean zipFiles(ClipBoardAdapter copy, ClipBoardAdapter move,String name,
			Context context){
		return FileOperationService.deflateFiles(copy, move, context, parent,name, this);
	}
	public boolean inflateFile(File tar,File dest,Context context,BaseAdapter adapter){
		return FileOperationService.inflateFile(tar, dest, context, adapter);
	}

}
