package com.yf.filesystem;

import java.io.File;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.yf.accountmanager.R;
import com.yf.accountmanager.action.BasePopWindow;
import com.yf.accountmanager.adapter.FileListAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.ui.FileChooser;
import com.yf.accountmanager.ui.StrAryPopWindow;
import com.yf.accountmanager.ui.StrAryPopWindow.OnPopListClickListener;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.FileUtils.FileSaver;

public abstract class BaseSaveasActivity extends Activity {
	
	public static final String[] CHARSET=new String[]{"UTF-8","GBK"};

	public CustomizedDialog editDialog,

	saveasDialog,

	noticeDialog,

	saveConfirmDialog,
	
	conflictNoticeDialog,
	
	warningDialog,
	
	progressDialog;

	public FileListAdapter fileListAdapter;
	
	protected BasePopWindow textOptionsPop;
	
	protected int curCharsetIndex=0;
	
	protected FileSaver fileSaver;
	
	protected Runnable fileSaveProgressUpdater;

	/**
	 * saveasDialog
	 */

	protected void initNshowSaveasDialog(boolean fileSelectedMode,int charSetIndex) {
		if (saveasDialog == null) {
			saveasDialog = FileChooser.initSaveasDialog(this);
			fileListAdapter = (FileListAdapter) saveasDialog.gv.getAdapter();
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					if (v == saveasDialog.back) {
						fileListAdapter.levelUp(saveasDialog.back,
								saveasDialog.positiveButton, saveasDialog.gv);
					} else if (v == saveasDialog.add) {
						FileChooser.initEditDialogNshow(getString(R.string.mkDirHint),
								BaseSaveasActivity.this,getString(R.string.newDir));
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
															saveasDialog.positiveButton,
															saveasDialog.back));
											editDialog.dismiss();
										}
									}
								});
					} else if (v == saveasDialog.dropDown) {
						initNshowTextOption();
					}
				}
			};
			saveasDialog.add.setOnClickListener(listener);
			saveasDialog.back.setOnClickListener(listener);
			saveasDialog.dropDown.setOnClickListener(listener);
			saveasDialog.positiveButton.setText("确定");
			saveasDialog.positiveButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(!fileListAdapter.parent.canWrite()){
						CommonUtils.toast("该文件夹无法写入");
						return ;
					}
					File file = new File(fileListAdapter.parent.getAbsolutePath()+File.separator+
					saveasDialog.editText.getText().toString());
					if(file.exists()){
						initNshowConflictNoticeDialog();
					}else{
						saveas(file,CHARSET[curCharsetIndex]);
					}
				}
			});
		}
		
		if(charSetIndex==0||charSetIndex==1){
			saveasDialog.dropDown.setVisibility(View.VISIBLE);
			curCharsetIndex=charSetIndex;
			saveasDialog.dropDown.setText(CHARSET[curCharsetIndex]);
		}else{
			saveasDialog.dropDown.setVisibility(View.GONE);
		}
		fileListAdapter
				.setFileSelectedMode(fileSelectedMode,
						saveasDialog.positiveButton, saveasDialog.gv,
						saveasDialog.back);
		saveasDialog.show();
	}

	protected void initNshowSaveConfirmDialog(final int charsetIndex) {
		if (saveConfirmDialog == null) {
			saveConfirmDialog = CustomizedDialog.initMultiButtonDialog(this,
					new String[] { getString(R.string.save),
							getString(R.string.saveas), "",
							getString(R.string.cancel) });
			saveConfirmDialog.message.setText("文件已经被改动,是否保存?");
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					saveConfirmDialog.dismiss();
					if (v == saveConfirmDialog.button1) {
						save();
					} else if (v == saveConfirmDialog.button2) {
						onSaveas(charsetIndex);
					} else if (v == saveConfirmDialog.button4) {}
				}
			};
			saveConfirmDialog.button1.setOnClickListener(listener);
			saveConfirmDialog.button2.setOnClickListener(listener);
			saveConfirmDialog.button4.setOnClickListener(listener);
		}
		saveConfirmDialog.show();
	}
	
	
	protected void initNshowConflictNoticeDialog() {
		if (conflictNoticeDialog == null) {
			conflictNoticeDialog = CustomizedDialog.initMultiButtonDialog(this,
					new String[] { getString(R.string.overlay),
							"", "",
							getString(R.string.cancel) });
			conflictNoticeDialog.message.setText("该文件已经存在,是否覆盖?");
			OnClickListener listener = new OnClickListener() {
				public void onClick(View v) {
					conflictNoticeDialog.dismiss();
					if (v == conflictNoticeDialog.button1) {
						saveas(new File(fileListAdapter.parent.getAbsolutePath()+File.separator+
					saveasDialog.editText.getText().toString()),CHARSET[curCharsetIndex]);
					}else if (v == conflictNoticeDialog.button4) {}
				}
			};
			conflictNoticeDialog.button1.setOnClickListener(listener);
			conflictNoticeDialog.button4.setOnClickListener(listener);
		}
		conflictNoticeDialog.show();
	}
	
	
	protected void initNshowWarningDialog(String message,View.OnClickListener listener ){
		if (warningDialog == null) {
			warningDialog = CustomizedDialog.initMultiButtonDialog(this,
					new String[] {getString(R.string.confirm),
							"", "",
							getString(R.string.cancel) });
			warningDialog.button4.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					warningDialog.dismiss();
				}
			});
		}
		warningDialog.message.setText(message);
		warningDialog.button1.setOnClickListener(listener);
		warningDialog.show();
	}
	
	protected void initNshowProgressDialog(){
		if(progressDialog==null){
			progressDialog=CustomizedDialog.initHorizontalProgressDialog(this);
			progressDialog.label1.setText("保存文件...");
			progressDialog.pb1.setMax(100);
			progressDialog.imgButton1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					stopFileSaving();
				}
			});
			progressDialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface arg0) {
					stopFileSaving();
				}
			});
			fileSaveProgressUpdater=new Runnable(){
				public void run(){
					if(fileSaver==null||fileSaver.isStop()) return ;
					double donePercent=fileSaver.getDonePercent();
					progressDialog.pb1.setProgress((int)donePercent);
					progressDialog.label2.setText(String.format("%.1f", donePercent) + "% ( "
							+ FileUtils.formatFileSize(fileSaver.doneBytes) + " / "
							+ FileUtils.formatFileSize(fileSaver.sumBytes) + " )");
					IConstants.MAIN_HANDLER.postDelayed(fileSaveProgressUpdater, 250);
				}
			};
		}
		progressDialog.label2.setText("0.0% ( ... / ... )");
		progressDialog.pb1.setProgress(0);
		IConstants.MAIN_HANDLER.removeCallbacks(fileSaveProgressUpdater);
		fileSaveProgressUpdater.run();
		progressDialog.show();
	}
	protected void stopFileSaving(){
		if(fileSaver!=null) 
			fileSaver.stop();
	}
	
	private void initNshowTextOption() {
		if(textOptionsPop==null){
			try {
				textOptionsPop=new StrAryPopWindow(saveasDialog.dropDown,this,CHARSET,true,
						(int)getResources().getDimension(R.dimen.dp_100),
						new OnPopListClickListener() {
							public void onclick(int position) {
								curCharsetIndex=position;
							}
						} );
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if(textOptionsPop!=null)
			textOptionsPop.showAsDropDown();
	}
	
	
	
	
	
	
	

	/**
	 * override
	 */

	@Override
	protected void onDestroy() {
		doOnDestroy();
		dismissAllDialogs();
		super.onDestroy();
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
	protected void onSaveInstanceState(Bundle outState) {
		dismissAllDialogs();
		super.onSaveInstanceState(outState);
	}
	
	
	
	protected void dismissAllDialogs() {
		CommonUtils.dismissDialogs(editDialog, noticeDialog, saveasDialog,
				saveConfirmDialog,conflictNoticeDialog,warningDialog,progressDialog);
	}

	protected void onSaveas(int charsetINdex) {
		initNshowSaveasDialog(false,charsetINdex);
	}

	protected abstract void backOperation();

	protected abstract void doOnDestroy();

	protected abstract void save();

	protected abstract void saveas(File dest,String charset);

	
	
	
	
	
	
	
	
	
	
	
	/***
	 * innerclass
	 * @author Administrator
	 *
	 */
	
/**	class TextOptionPopupWindow extends BasePopAction implements OnItemClickListener{
		
		ListView lv;
		
		StringAryAdapter adapter;
		
		Context context;
		
		
		public TextOptionPopupWindow(TextView trig,Context context) throws Throwable {
			super(R.layout.popupwindow_options, LayoutInflater.from(context),
					trig,(int)context.getResources().getDimension(R.dimen.dp_100),-2);
			this.context=context;
			execute();
			
		}

		@Override
		public void execute() throws Throwable {
			lv=(ListView)contentView.findViewById(R.id.listView1);
			lv.setAdapter(adapter=new StringAryAdapter(context));
			adapter.setSary(CHARSET);
			lv.setOnItemClickListener(this);
		}

		
		
		
		@Override
		public void resetTrigger() {}

		@Override
		public void onItemClick(AdapterView<?> parent, View self, int position,
				long id) {
			curCharsetIndex=position;
			trigger.setText(String.valueOf(adapter.getItem(position)));
			pop.dismiss();
		}
		
	}*/


}
