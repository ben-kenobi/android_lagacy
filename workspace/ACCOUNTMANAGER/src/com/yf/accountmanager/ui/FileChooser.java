package com.yf.accountmanager.ui;

import java.io.File;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.yf.accountmanager.FileChooserActivity;
import com.yf.accountmanager.R;
import com.yf.accountmanager.adapter.FileListAdapter;
import com.yf.accountmanager.adapter.OptionListAdapter;
import com.yf.accountmanager.adapter.ShareListAdapter;
import com.yf.accountmanager.common.FileManager;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.filesystem.BaseSaveasActivity;
import com.yf.filesystem.FileDetailActivity;

public class FileChooser {
	public static CustomizedDialog initFileChooser(Context context){
		final CustomizedDialog fileChooser = CustomizedDialog.initFileChooser(context);
		final FileListAdapter fileListAdapter;
		fileChooser.gv.setAdapter(fileListAdapter = new FileListAdapter(
				context, fileChooser.title));
		fileChooser.setNegativeButton("取消", null);
		fileChooser.gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				fileListAdapter.levelDown(fileChooser.back, self, position,
						fileChooser.positiveButton, fileChooser.gv);
			}
		});
		return fileChooser;
		
	}
	public static CustomizedDialog initSaveasDialog(Context context){
		final CustomizedDialog saveasDialog = CustomizedDialog.initSaveasDialog(context);
		final FileListAdapter fileListAdapter;
		saveasDialog.gv.setAdapter(fileListAdapter = new FileListAdapter(
				context, saveasDialog.title));
		saveasDialog.setNegativeButton("取消", null);
		saveasDialog.gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				fileListAdapter.levelDown(saveasDialog.back, self, position,
						saveasDialog.positiveButton, saveasDialog.gv);
			}
		});
		return saveasDialog;
	}
	
	public static CustomizedDialog initFileChooserDialog(Context context){
		final CustomizedDialog dialog = CustomizedDialog.initSaveasDialog(context);
		View view = dialog.findViewById(R.id.relativeLayout1);
		if(view!=null)
			view.setVisibility(View.GONE);
		final FileListAdapter fileListAdapter;
		dialog.gv.setAdapter(fileListAdapter = new FileListAdapter(
				context, dialog.title));
		dialog.setNegativeButton("取消", null);
		dialog.gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				fileListAdapter.levelDown(dialog.back, self, position,
						dialog.positiveButton, dialog.gv);
			}
		});
		return dialog;
	}
	
	
	
	public static void initEditDialogNshow(String hint,FileChooserActivity context,String text) {
		if (context.editDialog == null) {
			context.editDialog = CustomizedDialog.initEditDialog(context);
			context.editDialog.setNegativeButton("取消", null);
		}
		context.editDialog.editText.setHint(hint);
		context.editDialog.editText.setText(text);
		context.editDialog.editText.setSelection(context.editDialog.editText
								.getText().length());
		context.editDialog.positiveButton.setOnClickListener(null);
		context.editDialog.show();
	}
	
	
	public static void initEditDialogNshow(String hint,BaseSaveasActivity context,String text) {
		if (context.editDialog == null) {
			context.editDialog = CustomizedDialog.initEditDialog(context);
			context.editDialog.setNegativeButton("取消", null);
		}
		context.editDialog.editText.setHint(hint);
		context.editDialog.editText.setText(text);
		context.editDialog.editText.setSelection(context.editDialog.editText
				.getText().length());
		context.editDialog.positiveButton.setOnClickListener(null);
		context.editDialog.show();
	}

	public static void initNoticeDialog(String message,FileChooserActivity context) {
		if (context.noticeDialog == null) {
			context.noticeDialog = CustomizedDialog.initDialog("notice", "", null, 16,
					context);
		}
		context.noticeDialog.message.setText(message);
		context.noticeDialog.setNegativeButton("取消", null);
	}
	
	public static void initNoticeDialogNshow(String message,FileChooserActivity context) {
		initNoticeDialog(message,context);
		context.noticeDialog.negativeButton.setVisibility(View.VISIBLE);
		context.noticeDialog.show();
	}

	/**
	 * modify click event
	 * 
	 * @param item
	 */
	public static void showModifyAccessKeyDialog(MenuItem item,final FileChooserActivity context) {
		initEditDialogNshow("输入新密码",context,"");
		context.editDialog.positiveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String text = context.editDialog.editText.getText().toString();
				if (TextUtils.isEmpty(text)) {
					CommonUtils.toast("新密码不能为空");
				} else {
					if (CommonService.modifyAccessKey(
							CommonUtils.getAccessKey(), text)) {
						CommonUtils.setAccessKey(text);
						CommonUtils.toast("修改成功");
					} else
						CommonUtils.toast("修改失败");
					context.editDialog.dismiss();
				}
			}
		});
	}

	
	
	public static void initOptionDialogNshow(File file, int type, View targetView,final FileChooserActivity context) {
		// TODO
		if (context.fileOperationOption == null) {
			context.fileOperationOption = CustomizedDialog.initOptionDialog(context);
			context.fileOperationOption.lv
					.setAdapter(context.optionListAdapter = new OptionListAdapter(context));
			context.fileOperationOption.lv
					.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View self, int position, long id) {
							int type = context.optionListAdapter.type;
							if (type == FileManager.TYPE_SORT) {
								context.fileListAdapter.sort(position);
								context.fileOperationOption.dismiss();
							} else if (type == FileManager.TYPE_SCREEN) {
								context.fileListAdapter.screen(position,
										context.fileChooser.positiveButton);
								context.fileOperationOption.dismiss();
							} else if (type == FileManager.TYPE_OPTION) {
								if (position == 0) {
									initNoticeDialogNshow(context.optionListAdapter
											.getDeleteNotice(),context);
									context.noticeDialog
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															context.fileOperationOption
																	.dismiss();
															context.fileListAdapter
																	.deleteFileAsynchronized(
																			context.optionListAdapter.file,
																			context.fileChooser.positiveButton,
																			context);
														}
													});
								} else if (position == 1) {
									initEditDialogNshow("输入新的文件名",context,context.optionListAdapter.file
											.getName());
									context.editDialog.positiveButton
											.setOnClickListener(new OnClickListener() {
												public void onClick(View v) {
													String text = context.editDialog.editText
															.getText()
															.toString();
													if (TextUtils.isEmpty(text)) {
														CommonUtils
																.toast("文件名不能为空");
													} else {
														int result = context.fileListAdapter
																.renameFile(
																		context.optionListAdapter.file,
																		text,
																		context.fileChooser.positiveButton);
														CommonUtils
																.toast(result == 1 ? IConstants.RENAME_SUCCESS
																		: result == -1 ? IConstants.RENAME_CONFLICT
																				: result == -2 ? IConstants.UNCHANGED
																						: IConstants.RENAME_FAIL);
														if (result == 1
																|| result == 0
																|| result == -2) {
															context.fileOperationOption
																	.dismiss();
															context.editDialog
																	.dismiss();
														}
													}
												}
											});
								} else if (position == 2) {
									context.fileListAdapter.addToCopyList(
											context.optionListAdapter.file,
											context.optionListAdapter.targetView);
									context.fileOperationOption.dismiss();
								} else if (position == 3) {
									context.fileListAdapter.addToMoveList(
											context.optionListAdapter.file,
											context.optionListAdapter.targetView);
									context.fileOperationOption.dismiss();
								} else if (position == 4) {
									context.startActivityForResult(new Intent(
											context,
											FileDetailActivity.class).putExtra(
											IConstants.FILE,
											context.optionListAdapter.file),
											context.REQUEST_CODE_FILEDETAIL);
									context.fileOperationOption.dismiss();
								}
							}
						}
					});
		}
		context.optionListAdapter.setType(file, context.fileOperationOption.title, type,
				targetView, context.fileListAdapter.comparator, context.fileListAdapter.filter);
		context.fileOperationOption.show();
	}
	
	public static CustomizedDialog initNshowShareDialog(int type,File file,CustomizedDialog shareDialog,
			ShareListAdapter shareListAdapter,Context context){
		if (shareDialog == null) {
			shareDialog = CustomizedDialog.initShareDialog("", context);
			shareDialog.gv.setAdapter( shareListAdapter=new ShareListAdapter(context));
			// TODO
			final CustomizedDialog dialog=shareDialog;
			final ShareListAdapter adapter = shareListAdapter;
			 shareDialog.gv
			 .setOnItemClickListener(new OnItemClickListener() {
			 public void onItemClick(AdapterView<?> parent,
			 View self, int position, long id) {
				adapter.onItemClicked(position);
				dialog.dismiss();
			 }
			 });
		}

		shareListAdapter.setIntentType(type, shareDialog.title,file);
		if(type!=FileUtils.APK)
			shareDialog.show();
		return shareDialog;
	}

	
}
