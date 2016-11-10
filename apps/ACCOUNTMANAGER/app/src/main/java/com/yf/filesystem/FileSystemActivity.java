package com.yf.filesystem;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.action.BasePopWindow;
import com.yf.accountmanager.adapter.ClipBoardAdapter;
import com.yf.accountmanager.adapter.FileListAdapter;
import com.yf.accountmanager.adapter.ShareListAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.sqlite.IPathService;
import com.yf.accountmanager.sqlite.ISQLite.IPathColumns;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.ui.CustomizedDialog.FileSearcherDialog;
import com.yf.accountmanager.ui.CustomizedDialog.FileSystemSettingsDialog;
import com.yf.accountmanager.ui.FileChooser;
import com.yf.accountmanager.ui.StrAryPopWindow;
import com.yf.accountmanager.ui.StrAryPopWindow.OnPopListClickListener;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.MenuUtil;
import com.yf.accountmanager.util.SharePrefUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileSystemActivity extends Activity implements OnClickListener,
		OnLongClickListener {

	private String platform = IConstants.FILESYSTEM;

	private int resId = R.layout.activity_filesystem,
			selectedMenuIndicator = android.R.drawable.presence_online;

	private File root;

	private CustomizedDialog editDialog,

	fileOperationOption,

	noticeDialog,

	clipBoardDialog,

	collectionDialog,

	fileChooserDialog,
	
	shareDialog
	;

	
	private FileSearcherDialog fileSearcherDialog;

	private FileSystemAdapter filesystemAdapter;

	private ShareListAdapter shareListAdapter;


	private FileSystemSettingsDialog settingsDialog;
	private CustomizedDialog.FileSystemUsageDialog usageDialog;

	private OptionsAdapter optionsAdapter;

	private ClipBoardAdapter copyBoardAdapter, moveBoardAdapter;

	private IPathAdapter ipathAdapter;

	private FileListAdapter fileListAdapter;

	private GridView gv;

	private ImageButton levelup, more, clipBoard, mkdir, toggleMode, refresh,
			newfile, delete, move, copy;

	private TextView title;

	private View floatMenu, dmc;

	private BasePopWindow openAsPopWindow;

	public static final int REQUEST_CODE_FILEDETAIL = 2;

	private MenuItem[] sortGroup, screenGroup;
	
	private int gvColNum;

	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(resId);
		MenuUtil.commonActionBarDisplayOption(this);

		// /////////////////////////////////////////////////////////////

		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().
		// detectDiskReads().detectDiskWrites().detectNetwork().
		// penaltyLog().build()); StrictMode.setVmPolicy(new
		// StrictMode.VmPolicy.Builder().
		// detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath() .build());
		//
		// /////////////////////////////////////////////////////////////

		gvColNum=SharePrefUtil.getFileSystemGridColumnNum();

		File dir = (File) getIntent()
				.getSerializableExtra(IConstants.DIRECTORY);
		if (dir == null || !dir.isDirectory()) {
			root = new File(File.separator);
		} else {
			root = dir;
		}
		init();
		initGv();
		bindListeners();
	}

	/**
	 * base procedure
	 */

	
	
	private boolean  setGvNumColumn(int col){
		if(col==gvColNum) return true;
		if(SharePrefUtil.saveFileSystemGridColumnNum(col)){
			this.gvColNum=col;
			onGvColChange();
			return true;
		}
		return false;
	}
	
	private void onGvColChange(){
		gv.setNumColumns(gvColNum);
		if(fileSearcherDialog!=null){
			fileSearcherDialog.gv.setNumColumns(gvColNum);
		}
	}
	
	
	private void bindListeners() {
		MenuUtil.bindFloatMenuTrigger(more, floatMenu, this);
		levelup.setOnClickListener(this);
		mkdir.setOnClickListener(this);
		clipBoard.setOnClickListener(this);
		refresh.setOnClickListener(this);
		toggleMode.setOnClickListener(this);
		delete.setOnClickListener(this);
		move.setOnClickListener(this);
		copy.setOnClickListener(this);
		newfile.setOnClickListener(this);
		toggleMode.setOnLongClickListener(this);

	}

	private void init() {
		title = ((TextView) findViewById(R.id.title));
		levelup = (ImageButton) findViewById(R.id.back);
		gv = (GridView) findViewById(R.id.gridView1);
		mkdir = (ImageButton) findViewById(R.id.mkdir);
		clipBoard = (ImageButton) findViewById(R.id.clipBoard);
		refresh = (ImageButton) findViewById(R.id.refresh);
		more = (ImageButton) findViewById(R.id.more);
		toggleMode = (ImageButton) findViewById(R.id.toggleMode);
		floatMenu = findViewById(R.id.linearLayout1);
		dmc = findViewById(R.id.linearLayout2);
		delete = (ImageButton) findViewById(R.id.delete);
		move = (ImageButton) findViewById(R.id.move);
		copy = (ImageButton) findViewById(R.id.copy);
		newfile = (ImageButton) findViewById(R.id.newfile);
	}

	private void initGv() {
		gv.setNumColumns(gvColNum);
		gv.setAdapter(filesystemAdapter = new FileSystemAdapter(this, title,
				toggleMode, levelup, dmc, gv));
		filesystemAdapter.changeDir(root);
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self, int position,
					long id) {
				 int type = filesystemAdapter.onItemClicked(self, position);
				 if(type!=-1){
					 initShareDialogNshow(type, filesystemAdapter.fileList.get(position));
				 }
			}
		});
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View self,
					int position, long id) {
				initOptionsDialogNshow(null,
						filesystemAdapter.fileList.get(position), self,
						filesystemAdapter.multiselectorMode);
				return true;
			}
		});
	}

	/**
	 * 
	 * init Dialogs
	 */

	// fileChooserDialog be called when inflate be click
	private void initFileChooserDialogNshow() {
		if (fileChooserDialog == null) {
			fileChooserDialog = FileChooser.initFileChooserDialog(this);
			fileListAdapter = (FileListAdapter) fileChooserDialog.gv
					.getAdapter();
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (v == fileChooserDialog.back) {
						fileListAdapter.levelUp(fileChooserDialog.back,
								fileChooserDialog.positiveButton,
								fileChooserDialog.gv);
					} else if (v == fileChooserDialog.add) {
						initEditDialogNshow(getString(R.string.mkDirHint),
								getString(R.string.newDir));
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
															fileChooserDialog.positiveButton,
															fileChooserDialog.back));
											editDialog.dismiss();
										}
									}
								});
					} else if (v == fileChooserDialog.positiveButton) {
						if (!fileListAdapter.parent.canWrite()) {
							CommonUtils.toast("该文件夹无法写入");
							return;
						}
						filesystemAdapter.inflateFile(optionsAdapter.file,
								fileListAdapter.parent,
								FileSystemActivity.this, fileListAdapter);
						fileChooserDialog.dismiss();
						IConstants.MAIN_HANDLER.postDelayed(new Runnable(){
							public void run(){
								filesystemAdapter.refresh(false);
							}
						}, 600);
						
					}
				}
			};
			fileChooserDialog.add.setOnClickListener(listener);
			fileChooserDialog.back.setOnClickListener(listener);
			fileChooserDialog.positiveButton.setText("确定");
			fileChooserDialog.positiveButton.setOnClickListener(listener);
		}

		fileListAdapter.setFileSelectedMode(false,
				fileChooserDialog.positiveButton, fileChooserDialog.gv,
				fileChooserDialog.back);
		fileListAdapter.changeDir(filesystemAdapter.parent,
				fileChooserDialog.positiveButton, fileChooserDialog.gv,
				fileChooserDialog.back);
		fileChooserDialog.show();

	}

	// be called onGvItemLongClick
	private void initOptionsDialogNshow(Cursor cursor, File file,
			View targetView, boolean multiselectorMode) {
		if (fileOperationOption == null) {
			fileOperationOption = CustomizedDialog.initOptionDialog(this);
			fileOperationOption.lv
					.setAdapter(optionsAdapter = new OptionsAdapter(this));
			fileOperationOption.dropDown.setText(getString(R.string.openas));
			fileOperationOption.dropDown
					.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							if (openAsPopWindow == null) {
								try {
									openAsPopWindow = new StrAryPopWindow(
											fileOperationOption.dropDown,
											FileSystemActivity.this,
											getResources().getStringArray(
													R.array.openaslist1),
											false, (int) getResources()
													.getDimension(
															R.dimen.dp_110),
											new OnPopListClickListener() {
												public void onclick(int position) {
													if (position == 5) {
														initFileChooserDialogNshow();
														fileOperationOption
																.dismiss();
													} else{
														initShareDialogNshow(position, optionsAdapter.file);
//														SystemUtil
//																.openAs(optionsAdapter.file,
//																		position,
//																		FileSystemActivity.this);
													}
												}
											});
								} catch (Throwable e) {
									e.printStackTrace();
								}
							}
							if (openAsPopWindow != null)
								openAsPopWindow.showAsDropDown();
						}
					});
			fileOperationOption.lv
					.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View self, int position, long id) {
							if (optionsAdapter.type == OptionsAdapter.IPATHOPTION) {
								if (position == 1) {
									initEditDialogNshow(
											"输入备注名",
											ipathAdapter
													.getCursor()
													.getString(
															ipathAdapter
																	.getCursor()
																	.getColumnIndex(
																			IPathColumns.NAME)));
									editDialog.positiveButton
											.setOnClickListener(new OnClickListener() {
												public void onClick(View v) {
													ipathAdapter
															.handleOption(
																	1,
																	editDialog.editText
																			.getText()
																			.toString(),
																	optionsAdapter.targetView);
													editDialog.dismiss();
													fileOperationOption
															.dismiss();
												}
											});
								} else {
									ipathAdapter.handleOption(position, null,
											optionsAdapter.targetView);
									fileOperationOption.dismiss();
								}
							} else if (optionsAdapter.type == OptionsAdapter.FILEOPTION) {
								if (position == 0) {
									initNoticeDialogNshow(optionsAdapter
											.getDeleteNotice());
									noticeDialog
											.setPositiveButton(
													"确定",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															fileOperationOption
																	.dismiss();
															if (fileSearcherDialog != null
																	&& fileSearcherDialog
																			.isShowing()) {
																fileSearcherDialog.fileSearcherAdapter
																		.deleteFileAsynchronized(
																				optionsAdapter.file,
																				FileSystemActivity.this);
															} else
																filesystemAdapter
																		.deleteFileAsynchronized(
																				optionsAdapter.file,
																				FileSystemActivity.this);
														}
													});
								} else if (position == 1) {
									initEditDialogNshow("输入新的文件名",
											optionsAdapter.file.getName());
									editDialog.positiveButton
											.setOnClickListener(new View.OnClickListener() {
												public void onClick(View v) {
													String text = editDialog.editText
															.getText()
															.toString();
													if (TextUtils.isEmpty(text)) {
														CommonUtils
																.toast("文件名不能为空");
													} else {
														int result = 0;
														if (fileSearcherDialog != null
																&& fileSearcherDialog
																		.isShowing()) {
															result=fileSearcherDialog.fileSearcherAdapter.
																	renameFile(optionsAdapter.file, text);
														} else
															result = filesystemAdapter
																	.renameFile(
																			optionsAdapter.file,
																			text);
														CommonUtils
																.toast(result == 1 ? IConstants.RENAME_SUCCESS
																		: result == -1 ? IConstants.RENAME_CONFLICT
																				: result == -2 ? IConstants.UNCHANGED
																						: IConstants.RENAME_FAIL);
														if (result == 1
																|| result == 0
																|| result == -2) {
															fileOperationOption
																	.dismiss();
															editDialog
																	.dismiss();
														}
													}
												}
											});
								} else if (position == 2) {
									filesystemAdapter.addToCopyList(
											optionsAdapter.file,
											optionsAdapter.targetView);
									fileOperationOption.dismiss();
								} else if (position == 3) {
									filesystemAdapter.addToMoveList(
											optionsAdapter.file,
											optionsAdapter.targetView);
									fileOperationOption.dismiss();
								} else if (position == 4) {
									startActivityForResult(new Intent(
											FileSystemActivity.this,
											FileDetailActivity.class).putExtra(
											IConstants.FILE,
											optionsAdapter.file),
											REQUEST_CODE_FILEDETAIL);
									fileOperationOption.dismiss();
								} else if (position == 5) {
									if (fileSearcherDialog != null
											&& fileSearcherDialog
													.isShowing()) {
										fileSearcherDialog.fileSearcherAdapter.selectedAll();
									}else 
									filesystemAdapter.selectedAll();
									fileOperationOption.dismiss();
								} else if (position == 6) {
									if (fileSearcherDialog != null
											&& fileSearcherDialog
													.isShowing()) {
										if (fileSearcherDialog.fileSearcherAdapter.toggleMode()) {
											fileSearcherDialog.fileSearcherAdapter.selectFile(
													optionsAdapter.file,
													optionsAdapter.targetView);
										}
									}else 
									if (filesystemAdapter.toggleMode()) {
										filesystemAdapter.selectFile(
												optionsAdapter.file,
												optionsAdapter.targetView);
									}
									fileOperationOption.dismiss();
								}
							}
						}
					});
		}

		if (FileUtils.isReadableFile(file)) {
			fileOperationOption.dropDown.setVisibility(View.VISIBLE);
		} else {
			fileOperationOption.dropDown.setVisibility(View.GONE);
		}

		optionsAdapter.init(cursor, file, fileOperationOption.title,
				targetView, multiselectorMode);
		fileOperationOption.show();
	}

	private void initNoticeDialogNshow(String message) {
		initNoticeDialog(message);
		noticeDialog.negativeButton.setVisibility(View.VISIBLE);
		noticeDialog.show();
	}

	private void initNoticeDialog(String message) {
		if (noticeDialog == null) {
			noticeDialog = CustomizedDialog.initDialog("notice", "", null, 17,
					this);
			noticeDialog.setNegativeButton("取消", null);
		}
		noticeDialog.message.setText(message);
	}

	private void initEditDialogNshow(String hint, String text) {
		if (editDialog == null) {
			editDialog = CustomizedDialog.initEditDialog(this);
			editDialog.setNegativeButton("取消", null);
		}
		editDialog.editText.setHint(hint);
		editDialog.editText.setText(text);
		editDialog.editText
				.setSelection(editDialog.editText.getText().length());
		editDialog.positiveButton.setOnClickListener(null);
		editDialog.show();
	}

	private void initClipBoardNshow() {
		if (clipBoardDialog == null) {
			clipBoardDialog = CustomizedDialog.initClipBoardDialog(this);
			clipBoardDialog.gv
					.setAdapter(copyBoardAdapter = new ClipBoardAdapter(this)
							.setFileList(filesystemAdapter.copyList,
									ClipBoardAdapter.TYPE_COPY));
			clipBoardDialog.gv2
					.setAdapter(moveBoardAdapter = new ClipBoardAdapter(this)
							.setFileList(filesystemAdapter.moveList,
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
						filesystemAdapter.pasteFiles(copyBoardAdapter,
								moveBoardAdapter, FileSystemActivity.this);
						v.setEnabled(false);
					} else if (v == clipBoardDialog.negativeButton) {
						copyBoardAdapter.selectedRemove();
						moveBoardAdapter.selectedRemove();
						setClipBoardDialogButtonEnable();
					} else if (v == clipBoardDialog.zip) {
						initEditDialogNshow("输入文件名", "untitled.xzip");
						editDialog.positiveButton
								.setOnClickListener(new OnClickListener() {
									public void onClick(View v) {
										String text = editDialog.editText
												.getText().toString();
										if (TextUtils.isEmpty(text)) {
											CommonUtils.toast("文件名不能为空");
										} else {
											filesystemAdapter.zipFiles(
													copyBoardAdapter,
													moveBoardAdapter, text,
													FileSystemActivity.this);
											clipBoardDialog.zip
													.setEnabled(false);
											editDialog.dismiss();
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
					filesystemAdapter.refresh(false);
				}
			});
		}
		copyBoardAdapter.reset();
		moveBoardAdapter.reset();
		setClipBoardDialogButtonEnable();
		clipBoardDialog.show();
	}

	private void setClipBoardDialogButtonEnable() {
		boolean b = copyBoardAdapter.selectedFiles.isEmpty()
				&& moveBoardAdapter.selectedFiles.isEmpty();
		clipBoardDialog.positiveButton.setEnabled(!b);
		clipBoardDialog.negativeButton.setEnabled(!b);
		clipBoardDialog.zip.setEnabled(!b);
	}

	private void showModifyAccessKeyDialog(MenuItem item) {
		initEditDialogNshow("输入新密码", "");
		editDialog.positiveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String text = editDialog.editText.getText().toString();
				if (TextUtils.isEmpty(text)) {
					CommonUtils.toast("新密码不能为空");
				} else {
					if (CommonService.modifyAccessKey(
							CommonUtils.getAccessKey(), text)) {
						CommonUtils.setAccessKey(text);
						CommonUtils.toast("修改成功");
					} else
						CommonUtils.toast("修改失败");
					editDialog.dismiss();
				}
			}
		});
	}

	private void initCollectionDialogNshow() {
		if (collectionDialog == null) {
			collectionDialog = CustomizedDialog.initChooseDialog("已标记", this);
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (v == collectionDialog.title) {
						collectionDialog.dismiss();
					} else if (v == collectionDialog.remove) {
						if (IPathService.batchDelete(ipathAdapter.selectedIds))
							ipathAdapter.refresh();
						ipathAdapter.normalMode();
					}
				}
			};
			collectionDialog.title.setOnClickListener(listener);
			collectionDialog.remove.setOnClickListener(listener);
			collectionDialog.gv
					.setOnItemLongClickListener(new OnItemLongClickListener() {
						public boolean onItemLongClick(AdapterView<?> parent,
								View self, int position, long id) {
							System.out.println(ipathAdapter.getCursor()
									.getPosition()
									+ "   :  "
									+ self
									+ "   @FileSystemActivity");
							initOptionsDialogNshow(ipathAdapter.getCursor(),
									null, self, ipathAdapter.multiselectorMode);
							return true;
						}
					});

			collectionDialog.gv.setAdapter(ipathAdapter = new IPathAdapter(
					this, collectionDialog.remove));
			collectionDialog.gv
					.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent,
								View self, int position, long id) {
							File file = ipathAdapter.onItemClick(self);
							if (file == null)
								return;
							if (!file.exists() || !file.isDirectory()) {
								CommonUtils.toast("文件夹不存在");
							} else {
								filesystemAdapter.changeDir(file);
								collectionDialog.dismiss();
							}
						}
					});
			collectionDialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					ipathAdapter.reset();
					filesystemAdapter.updateParentLabel();
				}
			});
		}
		ipathAdapter.normalMode();
		ipathAdapter.refresh();
		collectionDialog.show();
	}

	private void initFileSearcherDialogNshow(File file, List<File> files) {
		if (fileSearcherDialog == null) {
			fileSearcherDialog = CustomizedDialog.initFileSearcherDialog(this,
					filesystemAdapter);
			fileSearcherDialog.gv.setNumColumns(gvColNum);
			fileSearcherDialog.delete.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					fileSearcherDialog.fileSearcherAdapter.updateSelectedList();
					if (fileSearcherDialog.fileSearcherAdapter.selectedList.isEmpty()) {
						CommonUtils.toast("无选中文件");
					} else {
						initNoticeDialogNshow("确认删除 "
								+ fileSearcherDialog.fileSearcherAdapter.selectedList.size() + " 个条目？");
						noticeDialog.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int which) {
										fileSearcherDialog.fileSearcherAdapter
												.deleteSelectedListAsynchronized(FileSystemActivity.this);
									}
								});
					}
				}
			});
			
			fileSearcherDialog.gv.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View self,
										int position, long id) {
					int type = fileSearcherDialog.onItemClicked(self, position);
					if (type >= 0) {
						initShareDialogNshow(type, fileSearcherDialog.fileSearcherAdapter.fileList
								.get(position));
					}
				}
			});
			
			fileSearcherDialog.gv
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						public boolean onItemLongClick(AdapterView<?> parent,
													   View self, int position, long id) {
							initOptionsDialogNshow(
									null,
									fileSearcherDialog.fileSearcherAdapter.fileList
											.get(position),
									self,
									fileSearcherDialog.fileSearcherAdapter.multiselectorMode);
							return true;
						}

					});
		}
		if (file == null)
			fileSearcherDialog.setScope(files);
		else
			fileSearcherDialog.setScope(file);
		fileSearcherDialog.show();
	}
	
	
	
	private void initSettingsDialogNshow() {
		if(settingsDialog==null){
			settingsDialog=CustomizedDialog.initFileSystemSettingsDialog(this);
			settingsDialog.setApplyCallBack(new Runnable() {
				public void run() {
					setGvNumColumn(settingsDialog.getColCount());
					if(fileSearcherDialog!=null)
						fileSearcherDialog.fileSearcherAdapter.setMaxResultCount(settingsDialog.getMaxResultCount());
					else
						SharePrefUtil.saveMaxFileSystemSearchResultCount(settingsDialog.getMaxResultCount());
				}
			});
		}
		
		settingsDialog.show();
	}

	private void initUsageDialogNshow() {
		if(usageDialog==null){
			usageDialog=CustomizedDialog.initFileSystemUsageDialog(this,new FilesystemUsageAdapter(this));

		}

		usageDialog.show();
	}
	
	
	private void initShareDialogNshow(int type,File file) {
		if (shareDialog == null) {
			shareDialog=FileChooser.initNshowShareDialog(type, file,null, null, this);
			shareListAdapter=(ShareListAdapter)shareDialog.gv.getAdapter();
		}else{
			FileChooser.initNshowShareDialog(type, file, shareDialog, shareListAdapter, this);
		}
		
	}
	

	/**
	 * Override
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.filesystemactivity_menu, menu);
		MenuUtil.createAccessKeyMenu(platform, menu);
		sortGroup = new MenuItem[] { menu.findItem(R.id.byNameAsc),
				menu.findItem(R.id.byNameDesc), menu.findItem(R.id.bySizeAsc),
				menu.findItem(R.id.bySizeDesc), menu.findItem(R.id.byTimeAsc),
				menu.findItem(R.id.byTimeDesc) };
		screenGroup = new MenuItem[] { menu.findItem(R.id.allfile),
				menu.findItem(R.id.onlyfile),
				menu.findItem(R.id.onlydirectory),
				menu.findItem(R.id.onlysystemfile),
				menu.findItem(R.id.onlywriteablefile) };
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == levelup) {
			filesystemAdapter.levelUp();
		} else if (v == clipBoard) {
			initClipBoardNshow();
		} else if (v == mkdir) {
			initEditDialogNshow("输入要创建的文件夹名称", "新建文件夹");
			editDialog.positiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String text = editDialog.editText.getText().toString();
					if (TextUtils.isEmpty(text)) {
						CommonUtils.toast("不能为空");
					} else {
						CommonUtils.toast(filesystemAdapter.mkDir(text));
						editDialog.dismiss();
					}
				}
			});
		} else if (v == newfile) {
			initEditDialogNshow("输入要创建的文件名称", "untitled");
			editDialog.positiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String text = editDialog.editText.getText().toString();
					if (TextUtils.isEmpty(text)) {
						CommonUtils.toast("不能为空");
					} else {
						try {
							CommonUtils.toast(filesystemAdapter
									.createNewFile(text));
						} catch (IOException e) {
							e.printStackTrace();
						}
						editDialog.dismiss();
					}
				}
			});
		} else if (v == refresh) {
			filesystemAdapter.refresh(false);
		} else if (v == toggleMode) {
			filesystemAdapter.toggleMode();
		} else if (v == delete) {
			filesystemAdapter.updateSelectedList();
			if (filesystemAdapter.selectedList.isEmpty()) {
				CommonUtils.toast("无选中文件");
			}else if(!filesystemAdapter.parent.canWrite()){
				CommonUtils.toast("当前位置文件无法删除");
			} else {
				initNoticeDialogNshow("确认删除 "
						+ filesystemAdapter.selectedList.size() + " 个条目？");
				noticeDialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								filesystemAdapter
										.deleteSelectedListAsynchronized(FileSystemActivity.this);
							}
						});
			}
		} else if (v == copy) {
			filesystemAdapter.addSelectedToCopyList();
		} else if (v == move) {
			filesystemAdapter.addSelectedToMoveList();
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v == toggleMode) {
			filesystemAdapter.selectedAll();
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean b = true;
		int id = item.getItemId();
		if (id == R.id.search) {
			if (filesystemAdapter.multiselectorMode)
				initFileSearcherDialogNshow(null,
						filesystemAdapter.getSelectedList());
			else
				initFileSearcherDialogNshow(filesystemAdapter.parent, null);
		} else if (id == R.id.mark) {
			initCollectionDialogNshow();
		}else if(id==R.id.settings){
			initSettingsDialogNshow();
		}else if(id==R.id.usage){
			initUsageDialogNshow();
		}else if (id == R.id.sort) {
			setSortGroup(filesystemAdapter.comparator.type);
		} else if (id == R.id.byNameAsc) {
			filesystemAdapter.sort(0);
		} else if (id == R.id.byNameDesc) {
			filesystemAdapter.sort(1);
		} else if (id == R.id.bySizeAsc) {
			filesystemAdapter.sort(2);
		} else if (id == R.id.bySizeDesc) {
			filesystemAdapter.sort(3);
		} else if (id == R.id.byTimeAsc) {
			filesystemAdapter.sort(4);
		} else if (id == R.id.byTimeDesc) {
			filesystemAdapter.sort(5);
		} else if (id == R.id.screen) {
			setScreenGroup(filesystemAdapter.filter.type);
		} else if (id == R.id.allfile) {
			filesystemAdapter.screen(0);
		} else if (id == R.id.onlyfile) {
			filesystemAdapter.screen(1);
		} else if (id == R.id.onlydirectory) {
			filesystemAdapter.screen(2);
		} else if (id == R.id.onlysystemfile) {
			filesystemAdapter.screen(3);
		} else if (id == R.id.onlywriteablefile) {
			filesystemAdapter.screen(4);
		} else if (id == MenuUtil.modify) {
			showModifyAccessKeyDialog(item);
		} else if (id == MenuUtil.toggleAccessKey) {
			if (CommonService.toggleAccessibility(platform)) {
				CommonUtils.toast("操作成功");
			} else
				CommonUtils.toast("操作失败");
			item.setTitle(MenuUtil.getToggleAccessKeyMenuItemTitle(platform));
		} else if (id == android.R.id.home) {
			backOperation();
		} else
			b = super.onOptionsItemSelected(item);
		return b;
	}

	private void setScreenGroup(int screenType) {
		for (int i = 0; i < screenGroup.length; i++) {
			MenuItem item = screenGroup[i];
			if (i == screenType)
				item.setIcon(selectedMenuIndicator);
			else
				item.setIcon(null);
		}
	}

	private void setSortGroup(int sortType) {
		for (int i = 0; i < sortGroup.length; i++) {
			MenuItem item = sortGroup[i];
			if (i == sortType)
				item.setIcon(selectedMenuIndicator);
			else
				item.setIcon(null);
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

	private void backOperation() {
		if (filesystemAdapter != null && filesystemAdapter.multiselectorMode)
			filesystemAdapter.toggleMode();
		else
			finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_FILEDETAIL) {
			if (resultCode != 0) {
				if (filesystemAdapter != null)
					filesystemAdapter.refresh(false);
			}
		}
	}

	@Override
	protected void onResume() {
		filesystemAdapter.refresh(false);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		dismissAllDialog();
		if (fileSearcherDialog != null)
			fileSearcherDialog.stopSearch();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// System.out.println(" onSaveInstanceState   @FileSystemActivity");
		dismissAllDialog();
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void dismissAllDialog() {
		CommonUtils.dismissDialogs(editDialog, fileOperationOption,
				noticeDialog, clipBoardDialog, collectionDialog,
				fileSearcherDialog, fileChooserDialog,settingsDialog,shareDialog);
		if (openAsPopWindow != null)
			openAsPopWindow.dismiss();
	}

}
