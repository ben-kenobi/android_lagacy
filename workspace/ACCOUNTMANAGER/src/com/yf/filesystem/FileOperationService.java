package com.yf.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.adapter.ClipBoardAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.serveice.FileTransferService;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.EncapsulationUtil.FileINDEflator;
import com.yf.accountmanager.util.FileUtils.FileTransfer;

public class FileOperationService {
	// copy
	public static boolean addToCopyList(File file, View convertView,
			List<File> copyList, List<File> moveList) {
		if (!file.canRead()) {
			CommonUtils.toast("无法读取该文件");
			return false;
		}
		if (!copyList.contains(file)) {
			if (moveList.contains(file)) {
				moveList.remove(file);
				convertView.setEnabled(true);
			}
			copyList.add(file);
			ShareViewHolder holder = (ShareViewHolder) convertView.getTag();
			holder.label.setEnabled(false);
		}
		return true;
	}

	// move
	public static boolean addToMoveList(File file, View convertView,
			File parent, List<File> copyList, List<File> moveList) {
		if (!file.canRead()) {
			CommonUtils.toast("无法读取该文件");
			return false;
		}
		if (!parent.canWrite()) {
			CommonUtils.toast("无法移动该文件");
			return false;
		}
		if (!moveList.contains(file)) {
			if (copyList.contains(file)) {
				copyList.remove(file);
				ShareViewHolder holder = (ShareViewHolder) convertView.getTag();
				holder.label.setEnabled(true);
			}
			moveList.add(file);
			convertView.setEnabled(false);
		}
		return true;
	}

	// renameFile
	public static int renameFile(File file, String newname) {
		if (file.getName().equalsIgnoreCase(newname))
			return -2;
		int result = 0;
		if (!file.getParentFile().canWrite())
			return result;
		File newfile = new File(file.getParent() + File.separator + newname);
		if (newfile.exists())
			result = -1;
		else {
			boolean b = true;
			if(!newfile.getParentFile().exists()){
				b = newfile.getParentFile().mkdirs();
			}
			if (b&&file.renameTo(newfile)) {
				result = 1;
				newfile.setLastModified(System.currentTimeMillis());
			}
		}
		return result;
	}

	// pasteFile
	public static boolean pasteFiles(ClipBoardAdapter copy,
			ClipBoardAdapter move, Context context, File parent,
			BaseAdapter adapter) {
		if (copy.selectedFiles.isEmpty() && move.selectedFiles.isEmpty())
			return false;
		if (!parent.isDirectory() || !parent.canWrite()) {
			CommonUtils.toast("该文件夹无法执行写入操作");
			return false;
		}
		List<File> conflictList = new ArrayList<File>();
		for (int i = 0; i < copy.selectedFiles.size(); i++) {
			File file = copy.selectedFiles.get(i);
			if (new File(parent.getAbsolutePath() + File.separator
					+ file.getName()).exists())
				conflictList.add(file);
		}
		for (int i = 0; i < move.selectedFiles.size(); i++) {
			File file = move.selectedFiles.get(i);
			if (new File(parent.getAbsolutePath() + File.separator
					+ file.getName()).exists())
				conflictList.add(file);
		}
		if (conflictList.isEmpty()) {
			startFileTransferService(copy, move, context, 0, parent, adapter);
			return true;
		} else {
			initConflictNoticeNshow(copy, move, context, conflictList, parent,
					adapter);
			return false;
		}
	}

	// zipFiles
	public static boolean deflateFiles(ClipBoardAdapter copy,
			ClipBoardAdapter move, Context context, File parent, String name,
			BaseAdapter adapter) {
		if (copy.selectedFiles.isEmpty() && move.selectedFiles.isEmpty())
			return false;
		if (!parent.isDirectory() || !parent.canWrite()) {
			CommonUtils.toast("该文件夹无法执行写入操作");
			return false;
		}
		startCompression(copy, move, context, parent, name, adapter);
		return true;
	}

	public static boolean inflateFile(File file, File dest, Context context,
			BaseAdapter adapter) {
		if (file == null || !file.exists() || !file.isFile() || !file.canRead())
			return false;
		if (!dest.isDirectory() || !dest.canWrite()) {
			CommonUtils.toast("该文件夹无法执行写入操作");
			return false;
		}
		IConstants.THREAD_POOL.submit(new FileINDEflator(file, dest, context));

		CommonUtils.toast("开始提取文件");
		if (adapter != null)
			adapter.notifyDataSetChanged();
		return true;
	}

	private static void startCompression(ClipBoardAdapter copy,
			ClipBoardAdapter move, final Context context, final File parent,
			final String name, BaseAdapter adapter) {
		List<File> copyList = copy.selectedFiles;
		List<File> moveList = move.selectedFiles;
		final File[] files = new File[copyList.size() + moveList.size()];
		int count = 0;
		for (int i = 0; i < copyList.size(); i++) {
			files[count++] = copyList.get(i);
		}
		for (int i = 0; i < moveList.size(); i++) {
			files[count++] = moveList.get(i);
		}

		IConstants.THREAD_POOL.submit(new FileINDEflator(files, parent, name,
				context));

		CommonUtils.toast("开始");
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	private static void startFileTransferService(ClipBoardAdapter copy,
			ClipBoardAdapter move, Context context, int type, File parent,
			BaseAdapter adapter) {
		// TODO
		context.startService(new Intent(context, FileTransferService.class)
				.putExtra(IConstants.FILE_COPY_LIST, copy.selectedFiles)
				.putExtra(IConstants.FILE_MOVE_LIST, move.selectedFiles)
				.putExtra(IConstants.FILE_PARENT, parent)
				.putExtra(IConstants.FILE_PASTE_TYPE, type));
		move.selectedRemove();
		CommonUtils.toast("开始");
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	private static void initConflictNoticeNshow(final ClipBoardAdapter copy,
			final ClipBoardAdapter move, final Context context,
			List<File> conflictList, final File parent,
			final BaseAdapter adapter) {
		StringBuffer sb = new StringBuffer("--以下名字的文件已存在\n");
		for (int i = 0; i < conflictList.size() && i < 5; i++) {
			sb.append(conflictList.get(i).getName() + "\n");
		}
		if (conflictList.size() > 5) {
			sb.append("--etc...");
		}
		sb.append("--请选择操作方式");
		final CustomizedDialog conflictNotice = CustomizedDialog
				.initMultiButtonDialog(context, null);
		View.OnClickListener listener = new View.OnClickListener() {
			public void onClick(View v) {
				if (v == conflictNotice.button1) {
					startFileTransferService(copy, move, context,
							FileTransfer.RETAIN, parent, adapter);
				} else if (v == conflictNotice.button2) {
					startFileTransferService(copy, move, context,
							FileTransfer.COVER, parent, adapter);
				} else if (v == conflictNotice.button3) {
					startFileTransferService(copy, move, context,
							FileTransfer.SKIP, parent, adapter);
				} else if (v == conflictNotice.button4) {
				}
				conflictNotice.dismiss();
			}
		};
		conflictNotice.button1.setOnClickListener(listener);
		conflictNotice.button2.setOnClickListener(listener);
		conflictNotice.button3.setOnClickListener(listener);
		conflictNotice.button4.setOnClickListener(listener);
		conflictNotice.message.setText(sb.toString());
		conflictNotice.show();

	}

	public static class ShareViewHolder {
		public ImageView icon;
		public TextView label;

		public ShareViewHolder(View v) {
			icon = (ImageView) v.findViewById(R.id.imageView1);
			label = (TextView) v.findViewById(R.id.textView1);
			v.setTag(this);
		}
	}
}
