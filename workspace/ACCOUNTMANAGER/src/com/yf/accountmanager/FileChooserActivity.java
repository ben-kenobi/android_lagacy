package com.yf.accountmanager;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.yf.accountmanager.adapter.ClipBoardAdapter;
import com.yf.accountmanager.adapter.FileListAdapter;
import com.yf.accountmanager.adapter.GvPickerAdapter;
import com.yf.accountmanager.adapter.OptionListAdapter;
import com.yf.accountmanager.common.ExportImportTask;
import com.yf.accountmanager.common.FileManager;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.ui.FileChooser;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.MenuUtil;

public abstract class FileChooserActivity extends Activity {

	public String platform;

	public int resId;

	public CustomizedDialog editDialog,

	picker,

	fileChooser,

	fileOperationOption,

	noticeDialog,

	clipBoardDialog;

	public FileListAdapter fileListAdapter;

	public GvPickerAdapter pickerAdapter;

	public OptionListAdapter optionListAdapter;

	public ClipBoardAdapter copyBoardAdapter, moveBoardAdapter;

	public static final int REQUEST_CODE_FILEDETAIL = 2;

	/**
	 * im/export click event
	 * 
	 * @param dirOnly
	 */

	protected void initFileChooserNshow(boolean fileSelectedMode) {
		if (fileChooser == null) {
			fileChooser = FileChooser.initFileChooser(this);
			fileListAdapter = (FileListAdapter) fileChooser.gv.getAdapter();
			fileChooser.gv
					.setOnItemLongClickListener(new OnItemLongClickListener() {
						public boolean onItemLongClick(AdapterView<?> parent,
								View self, int position, long id) {
							FileChooser.initOptionDialogNshow(
									fileListAdapter.fileList.get(position),
									FileManager.TYPE_OPTION, self,
									FileChooserActivity.this);
							return true;
						}
					});

			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (v == fileChooser.back) {
						fileListAdapter.levelUp(fileChooser.back,
								fileChooser.positiveButton, fileChooser.gv);
					} else if (v == fileChooser.add) {
						FileChooser.initEditDialogNshow(getString(R.string.mkDirHint),
								FileChooserActivity.this,getString(R.string.newDir));
						editDialog.positiveButton
								.setOnClickListener(new OnClickListener() {
									public void onClick(View v) {
										String text = editDialog.editText
												.getText().toString();
										if (TextUtils.isEmpty(text)) {
											CommonUtils.toast("不能为空");
										} else {
											CommonUtils.toast(fileListAdapter
													.mkDir(text,
															fileChooser.positiveButton,
															fileChooser.back));
											editDialog.dismiss();
										}
									}
								});
					} else if (v == fileChooser.sort) {
						FileChooser.initOptionDialogNshow(null,
								FileManager.TYPE_SORT, null,
								FileChooserActivity.this);
					} else if (v == fileChooser.screen) {
						FileChooser.initOptionDialogNshow(null,
								FileManager.TYPE_SCREEN, null,
								FileChooserActivity.this);
					} else if (v == fileChooser.clipBoard) {
						// TODO
						initClipBoardNshow();
					} else if (v == fileChooser.refresh) {
						fileListAdapter.refresh(fileChooser.positiveButton);
					}
				}
			};
			fileChooser.add.setOnClickListener(listener);
			fileChooser.back.setOnClickListener(listener);
			fileChooser.sort.setOnClickListener(listener);
			fileChooser.screen.setOnClickListener(listener);
			fileChooser.clipBoard.setOnClickListener(listener);
			fileChooser.refresh.setOnClickListener(listener);
			fileChooser.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							File file = fileListAdapter.getSelectedFile();
							if (file != null)
								new ExportImportTask(FileChooserActivity.this,
										platform, null).execute(file);
						}
					});

		}
		fileListAdapter.setFileSelectedMode(fileSelectedMode,
				fileChooser.positiveButton, fileChooser.gv, fileChooser.back);
		fileChooser.show();
	}

	// initClipBoardNshow
	public void initClipBoardNshow() {
		if (clipBoardDialog == null) {
			clipBoardDialog = CustomizedDialog.initClipBoardDialog(this);
			clipBoardDialog.gv
					.setAdapter(copyBoardAdapter = new ClipBoardAdapter(this)
							.setFileList(fileListAdapter.copyList,
									ClipBoardAdapter.TYPE_COPY));
			clipBoardDialog.gv2
					.setAdapter(moveBoardAdapter = new ClipBoardAdapter(this)
							.setFileList(fileListAdapter.moveList,
									ClipBoardAdapter.TYPE_MOVE));
			OnItemClickListener itemClickListener = new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
						int position, long id) {
					if (parent == clipBoardDialog.gv) {
						copyBoardAdapter.onItemClick(position, self);
					} else if (parent == clipBoardDialog.gv2) {
						moveBoardAdapter.onItemClick(position, self);
					}
					setClipBoardDialogButtonEnable();
				}
			};
			OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {
				public boolean onItemLongClick(AdapterView<?> parent,
						View self, int position, long id) {
					if (parent == clipBoardDialog.gv) {
						copyBoardAdapter.toggleSelected(position);
					} else if (parent == clipBoardDialog.gv2) {
						moveBoardAdapter.toggleSelected(position);
					}
					setClipBoardDialogButtonEnable();
					return true;
				}
			};
			clipBoardDialog.gv.setOnItemClickListener(itemClickListener);
			clipBoardDialog.gv2.setOnItemClickListener(itemClickListener);
			clipBoardDialog.gv
					.setOnItemLongClickListener(itemLongClickListener);
			clipBoardDialog.gv2
					.setOnItemLongClickListener(itemLongClickListener);
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (v == clipBoardDialog.back) {
						clipBoardDialog.dismiss();
					} else if (v == clipBoardDialog.positiveButton) {
						// TODO
						fileListAdapter.pasteFiles(copyBoardAdapter,
								moveBoardAdapter, FileChooserActivity.this);
						v.setEnabled(false);
					} else if (v == clipBoardDialog.negativeButton) {
						copyBoardAdapter.selectedRemove();
						moveBoardAdapter.selectedRemove();
						setClipBoardDialogButtonEnable();
					}else if(v==clipBoardDialog.zip){
						FileChooser.initEditDialogNshow("输入文件名",FileChooserActivity.this,"untitled.yf");
						editDialog.positiveButton.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								String text = editDialog.editText
										.getText()
										.toString();
								if (TextUtils.isEmpty(text)) {
									CommonUtils
											.toast("文件名不能为空");
								}else{
									fileListAdapter.zipFiles(copyBoardAdapter,
											moveBoardAdapter,text,FileChooserActivity.this);
									clipBoardDialog.zip.setEnabled(false);
									editDialog
									.dismiss();
								}
							}
						});
						
					}
				}
			};
			clipBoardDialog.back.setOnClickListener(listener);
			clipBoardDialog.zip.setOnClickListener(listener);
			clipBoardDialog.positiveButton.setOnClickListener(listener);
			clipBoardDialog.negativeButton.setOnClickListener(listener);
			clipBoardDialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface self) {
					fileListAdapter.refresh(fileChooser.positiveButton);
				}
			});
		}
		copyBoardAdapter.reset();
		moveBoardAdapter.reset();
		setClipBoardDialogButtonEnable();
		clipBoardDialog.show();
	}

	public void setClipBoardDialogButtonEnable() {
		boolean b = copyBoardAdapter.selectedFiles.isEmpty()
				&& moveBoardAdapter.selectedFiles.isEmpty();
		clipBoardDialog.positiveButton.setEnabled(!b);
		clipBoardDialog.negativeButton.setEnabled(!b);
		clipBoardDialog.zip.setEnabled(!b);
	}

	
	
	/**
	 * override
	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		onCreateMenuItem(menu);
		MenuUtil.createAccessKeyMenu(platform, menu);
		return true;
	}
	
	
	protected boolean onOtherMenuItemClicked(int itemId) {
		return false;
	}

	protected void onCreateMenuItem(Menu menu) {
		if (IConstants.ACCOUNT.equals(platform))
			getMenuInflater().inflate(R.menu.accountlistactivity_menu, menu);
		else if (IConstants.CONTACTS.equals(platform))
			getMenuInflater().inflate(R.menu.contactsactivity_menu, menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add) {
			onAddMenuItemClicked();
		} else if (id == R.id.search) {
			onSearchMenuItemClicked();
		} else if (id == MenuUtil.modify) {
			FileChooser.showModifyAccessKeyDialog(item, this);
		} else if (id == MenuUtil.toggleAccessKey) {
			if (CommonService.toggleAccessibility(platform)) {
				CommonUtils.toast("操作成功");
			} else
				CommonUtils.toast("操作失败");
			item.setTitle(MenuUtil.getToggleAccessKeyMenuItemTitle(platform));
		} else if (id == android.R.id.home) {
			backOperation();
		} else if (id == R.id.export) {
			initFileChooserNshow(false);
		} else if (id == R.id.importE) {
			initFileChooserNshow(true);
		} else if (id == R.id.delete) {
			onDeleteMenuItemClicked();
		} else
			onOtherMenuItemClicked(id);
		return true;
	}

	@Override
	protected void onDestroy() {
		doOnDestroy();
		dismissAllDialogs();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_FILEDETAIL) {
			if (resultCode != 0) {
				if (fileListAdapter != null)
					fileListAdapter.refresh(fileChooser.positiveButton);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backOperation();
			return true;
		} else
			return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MenuUtil.commonActionBarDisplayOption(this);
		/**
		 * StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		 * .detectDiskReads().detectDiskWrites().detectNetwork()
		 * .penaltyLog().build()); StrictMode.setVmPolicy(new
		 * StrictMode.VmPolicy.Builder()
		 * .detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath() .build());
		 */
		setContentView(resId);
		baseProcedure();
		
	}

	private void baseProcedure() {
		init();
		initMainContent();
		bindListeners();
	}

	protected void dismissAllDialogs() {
		CommonUtils.dismissDialogs(editDialog, picker, fileChooser,
				fileOperationOption, noticeDialog, clipBoardDialog);
	}

	protected  void onSearchMenuItemClicked(){
	}

	protected abstract void backOperation();

	protected abstract void onAddMenuItemClicked();

	protected abstract void onDeleteMenuItemClicked();
	
	protected abstract void init();
	
	protected abstract void initMainContent();
	
	protected abstract void bindListeners();

	protected abstract void doOnDestroy();
}
