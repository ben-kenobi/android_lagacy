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
		pd = CustomizedDialog.createProgressDialogNshow(null, "�Եȡ���",
				false, context);
	}

	@Override
	protected String doInBackground(File... params) {
		if (params[0].isDirectory())
			try {
				return doExport(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return "�����쳣,ȱ�ٸ��ļ���д��Ȩ��";
			}
		else if (params[0].isFile())
			try {
				return doImport(params[0]);
			} catch (Exception e) {
				e.printStackTrace();
				return "�����쳣";
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
			return "ȱ�ٸ��ļ���д��Ȩ��";
		Cursor cursor=CommonService.queryAllByPlatform(platform);
		JSONArray ja = CommonUtils
				.encapsulateCursorAsJSONArray(cursor);
		if (ja == null)
			return "û�м�¼���Ե���";
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
		return "�������,�ļ�������\n" + backup.getPath();
	}

	private String doImport(File file) {
		if (!file.canRead())
			return "ȱ�ٶ�ȡ�ļ�Ȩ��";
		String content = null;
		try {
			content = IOUtils.readStringFromFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			return "��ȡ�ļ��쳣";
		}
		List<ContentValues> cvs = null;
		try {
			cvs = CommonUtils
					.convertJSONArrayToContentValuesList(new JSONArray(
							content));
		} catch (JSONException e) {
			e.printStackTrace();
			return "�ļ���ʽ����";
		}
		CommonService.batchInsertByPlatform(platform, cvs);
		if(exeAfterSuccess!=null)
			exeAfterSuccess.run();
		return "�������";
	}
	
	private void initMessageOnlyDialogNshow(String message) {
		initNoticeDialog(message);
		noticeDialog.negativeButton.setVisibility(View.GONE);
		noticeDialog.setPositiveButton("ȷ��", null);
		noticeDialog.show();
	}
	private void initNoticeDialog(String message) {
		if (noticeDialog == null) {
			noticeDialog = CustomizedDialog.initDialog("notice", "", null, 17,
					context);
			noticeDialog.setNegativeButton("ȡ��", null);
		}
		noticeDialog.message.setText(message);
	}

}

