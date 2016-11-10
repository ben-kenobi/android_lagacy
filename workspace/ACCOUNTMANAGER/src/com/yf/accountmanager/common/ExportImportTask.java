package com.yf.accountmanager.common;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.View;

import com.yf.accountmanager.sqlite.AccountService;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.sqlite.ContactsService;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.IOUtils;

public class ExportImportTask extends AsyncTask<File, Void, String> {
	public ProgressDialog pd;
	private CustomizedDialog noticeDialog;
	private Context context;
	private String platform;
	private Runnable exeAfterSuccess;
	public ExportImportTask(Context context ,String platform,Runnable exeAfterSuccess){
		this.context=context;
		this.platform=platform;
		this.exeAfterSuccess=exeAfterSuccess;
	}

	@Override
	protected void onPreExecute() {
		pd = CustomizedDialog.createProgressDialogNshow(null, "稍等。。",
				false, context);
	}

	@Override
	protected String doInBackground(File... params) {
		if (params[0].isDirectory())
			try {
				return doExport(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return "发生异常,缺少该文件夹写入权限";
			}
		else if (params[0].isFile())
			try {
				return doImport(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return "发生异常";
			}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		pd.dismiss();
		if (result == null)
			return;
		initMessageOnlyDialogNshow(result);
	}

	private String doExport(File file) throws Exception {
		if (!file.canWrite())
			return "缺少该文件夹写入权限";
		Cursor cursor=CommonService.queryAllByPlatform(platform);
		JSONArray ja = CommonUtils
				.encapsulateCursorAsJSONArray(cursor);
		if (ja == null)
			return "没有记录可以导出";
		String str = ja.toString();
		File backup = null;
		for (int i = 0; i >= 0; i++) {
			backup = FileUtils.get_backupFile(file, i,platform);
			if (!backup.exists())
				break;
		}
		FileOutputStream fos = new FileOutputStream(backup, false);
		fos.write(str.getBytes("utf-8"));
		fos.flush();
		fos.close();
		return "导出完毕,文件保存在\n" + backup.getPath();
	}

	private String doImport(File file) {
		if (!file.canRead())
			return "缺少读取文件权限";
		String content = null;
		try {
			content = IOUtils.readStringFromFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			return "读取文件异常";
		}
		List<ContentValues> cvs = null;
		try {
			cvs = CommonUtils
					.convertJSONArrayToContentValuesList(new JSONArray(
							content));
		} catch (JSONException e) {
			e.printStackTrace();
			return "文件格式不符";
		}
		CommonService.batchInsertByPlatform(platform, cvs);
		if(exeAfterSuccess!=null)
			exeAfterSuccess.run();
		return "导入完成";
	}
	
	private void initMessageOnlyDialogNshow(String message) {
		initNoticeDialog(message);
		noticeDialog.negativeButton.setVisibility(View.GONE);
		noticeDialog.setPositiveButton("确定", null);
		noticeDialog.show();
	}
	private void initNoticeDialog(String message) {
		if (noticeDialog == null) {
			noticeDialog = CustomizedDialog.initDialog("notice", "", null, 17,
					context);
			noticeDialog.setNegativeButton("取消", null);
		}
		noticeDialog.message.setText(message);
	}

}

