package com.yf.accountmanager.adapter;

import java.io.File;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.SystemUtil;

public class ShareListAdapter extends BaseAdapter {

	public static final int SHARE = 6;

	Context context;

	public List<ResolveInfo> activityList;
	int resId = R.layout.item4gv_shareitem;
	private int type = -1;

	private File file;

	public ShareListAdapter(Context context) {
		super();
		this.context = context;
	}

	public void setShareInent() {
		if (this.type != SHARE) {
			Intent intent = new Intent(Intent.ACTION_SEND).setType("*/*");
			findActivityListByIntent(intent);
			this.type = SHARE;
		}
	}

	public void setIntentByType(int type) {
		if (this.type != type) {
			Intent intent = new Intent(Intent.ACTION_VIEW).setDataAndType(
					Uri.fromFile(new File("")), getFileTypeByType(type));
			findActivityListByIntent(intent);
			this.type = type;
		}
	}

	public void setIntentType(int type, TextView title, File file) {
		if (this.type != type || !this.file.equals(file)) {
			this.file = file;
			if (type == SHARE) {
				setShareInent();
				title.setText(context.getString(R.string.shareSthTo,
						file.getName()));
			} else if (type ==FileUtils.APK) {
				SystemUtil.openAsInstallation(file, context);
			} else {
				setIntentByType(type);
				title.setText(context.getString(R.string.chooseActivity,
						file.getName()));
			}

		}
	}

	private void findActivityListByIntent(Intent intent) {
		activityList = context.getPackageManager().queryIntentActivities(
				intent, PackageManager.MATCH_DEFAULT_ONLY);
		ResolveInfo.DisplayNameComparator comparator = new ResolveInfo.DisplayNameComparator(
				context.getPackageManager());
		Collections.sort(activityList, comparator);
	}

	public void onItemClicked(int position) {
		if (type == SHARE) {
			SystemUtil.shareFile(file, activityList.get(position), context);
		} else {
			SystemUtil.viewFileByType(file, activityList.get(position),
					getFileTypeByType(type), context);
		}
	}

	public String getFileTypeByType(int type) {
		if (type < 0 || type > FileUtils.FILE_TYPE.length - 1)
			return "*/*";
		return FileUtils.FILE_TYPE[type];
	}

	@Override
	public int getCount() {
		if (activityList == null)
			return 0;
		return activityList.size();
	}

	@Override
	public Object getItem(int position) {
		if (activityList == null)
			return null;
		return activityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resId,
					container, false);
			holder = new ViewHolder(convertView);
		} else
			holder = (ViewHolder) convertView.getTag();
		ResolveInfo ri = activityList.get(position);
		holder.imageView.setImageDrawable(ri.loadIcon(context
				.getPackageManager()));
		holder.tv.setText(ri.loadLabel(context.getPackageManager()));
		return convertView;
	}

	class ViewHolder {
		ImageView imageView;
		TextView tv;

		public ViewHolder(View convertView) {
			imageView = (ImageView) convertView.findViewById(R.id.imageView1);
			tv = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(this);
		}

	}

}
