package com.yf.filesystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yf.accountmanager.R;
import com.yf.accountmanager.action.BasePopWindow;
import com.yf.accountmanager.adapter.ShareListAdapter;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.ui.FileChooser;
import com.yf.accountmanager.ui.StrAryPopWindow;
import com.yf.accountmanager.ui.StrAryPopWindow.OnPopListClickListener;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.accountmanager.util.FileUtils;
import com.yf.accountmanager.util.FileUtils.Md5sumer;
import com.yf.accountmanager.util.MenuUtil;
import com.yf.accountmanager.util.SystemUtil;

public class FileDetailActivity extends Activity implements
		View.OnClickListener {
	private int resId = R.layout.activity_filedetail, editButtonId = 0x11,
			shareButtonId = 0x12, viewButtonId = 0x13;
	private boolean contentChanged = false;

	private TextView fileType, fileLocation, fileSize, modifyTime, openAs;
	private Button filenameDisposer, apply, md5sum;
	private ImageView icon;
	private EditText filename;
	private TextView md5sumEt;
	private CheckedTextView readable, writeable, executable;

	private ProgressBar md5sumPb;

	private File file;

	private CustomizedDialog shareDialog;

	private ShareListAdapter shareListAdapter;

	private BasePopWindow openAsPopWindow;

	private Md5sumer md5sumer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		MenuUtil.commonActionBarDisplayOption(this);
		init();
		if (savedInstanceState == null) {
			file = (File) getIntent().getSerializableExtra(IConstants.FILE);
			blockFocus();
		} else {
			file = (File) savedInstanceState.get(IConstants.FILE);
			if (savedInstanceState.getBoolean("block"))
				blockFocus();
			else
				requestFocus();
		}
		if (file == null) {
			finish();
			return;
		}
		fillInfo();
		bindListeners();
	}

	private void fillInfo() {
		if (FileUtils.isReadableFile(file)) {
			openAs.setVisibility(View.VISIBLE);
			md5sum.setVisibility(View.VISIBLE);
		} else {
			openAs.setVisibility(View.GONE);
			md5sum.setVisibility(View.GONE);
		}
		FileUtils.setFileIconByType(file, icon, null);
		fileType.setText(FileUtils.getFileTypeName(file));
		fileSize.setText("...");
		FileUtils.displayFileSize(file, fileSize);
		fillInfo1();
	}

	private void fillInfo1() {
		fileLocation.setText(file.getAbsolutePath());
		modifyTime.setText(new SimpleDateFormat(IConstants.TIME_PATTERN,
				Locale.CHINA).format(new Date(file.lastModified())));
		filename.setText(file.getName());
		readable.setChecked(file.canRead());
		writeable.setChecked(file.canWrite());
		executable.setChecked(file.canExecute());
	}

	private void init() {
		fileType = (TextView) findViewById(R.id.textView1);
		fileLocation = (TextView) findViewById(R.id.textView2);
		fileSize = (TextView) findViewById(R.id.textView3);
		modifyTime = (TextView) findViewById(R.id.textView4);
		filenameDisposer = (Button) findViewById(R.id.button1);
		apply = (Button) findViewById(R.id.button2);
		md5sum = (Button) findViewById(R.id.md5sum);
		filename = (EditText) findViewById(R.id.editText1);
		md5sumEt = (TextView) findViewById(R.id.editText2);
		icon = (ImageView) findViewById(R.id.imageView1);
		readable = (CheckedTextView) findViewById(R.id.checkedTextView1);
		writeable = (CheckedTextView) findViewById(R.id.checkedTextView4);
		executable = (CheckedTextView) findViewById(R.id.checkedTextView3);
		openAs = (TextView) findViewById(R.id.dropdown);
		md5sumPb = (ProgressBar) findViewById(R.id.progressBar1);
	}

	private void bindListeners() {
		CommonUtils.bindEditTextNtextDisposer(filename, filenameDisposer);
		readable.setOnClickListener(this);
		writeable.setOnClickListener(this);
		executable.setOnClickListener(this);
		apply.setOnClickListener(this);
		openAs.setOnClickListener(this);
		md5sum.setOnClickListener(this);
	}

	private void toggleBlockState() {
		if (filename.isEnabled())
			blockNreset();
		else
			requestFocus();
	}

	private void blockNreset() {
		blockFocus();
		fillInfo1();
	}

	private void blockFocus() {
		filename.setEnabled(false);
		apply.setVisibility(View.GONE);
		readable.setEnabled(false);
		writeable.setEnabled(false);
		executable.setEnabled(false);
		filenameDisposer.setVisibility(View.GONE);
		md5sumEt.setEnabled(false);
	}

	private void requestFocus() {
		filename.setEnabled(true);
		apply.setVisibility(View.VISIBLE);
		readable.setEnabled(true);
		writeable.setEnabled(true);
		executable.setEnabled(true);
		md5sumEt.setEnabled(true);
	}

	// OVERRIDE

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putBoolean("block", !filename.isEnabled());
		outState.putSerializable(IConstants.FILE, file);
		dismissAllDialog();
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		dismissAllDialog();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			backOperation();
			return true;
		} else if (id == editButtonId) {
			toggleBlockState();
			return true;
		} else if (id == viewButtonId) {
			SystemUtil.forwardToFileContentViewer(file, this);
			return true;
		} else if (id == shareButtonId) {
			initShareDialogNshow(ShareListAdapter.SHARE,file);
			return true;
		} else
			return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, editButtonId, 1, R.string.edit)
				.setIcon(android.R.drawable.ic_menu_edit)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		if (FileUtils.isReadableFile(file)) {
			menu.add(0, shareButtonId, 0, R.string.share)
					.setIcon(android.R.drawable.ic_menu_share)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			menu.add(0, viewButtonId, 0, R.string.view)
					.setIcon(android.R.drawable.ic_menu_view)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return true;
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
		if (contentChanged)
			setResult(1);
		stopMd5sum();
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v == readable) {
			readable.setChecked(!readable.isChecked());
		} else if (v == writeable) {
			writeable.setChecked(!writeable.isChecked());
		} else if (v == executable) {
			executable.setChecked(!executable.isChecked());
		} else if (v == openAs) {
			initOpenAsPopWindowNshow();
		} else if (v == md5sum) {
			if (md5sumer != null && !md5sumer.isStop())
				stopMd5sum();
			else
				calNshowMd5sum();
		} else if (v == apply) {
			String name = filename.getText().toString();
			if (file.getName().equalsIgnoreCase(name)) {
				if (modifyFileAuthority()) {
					CommonUtils.toast("修改成功");
				} else {
					CommonUtils.toast("未能执行该操作");
				}
				blockNreset();
			} else {
				File newFile = new File(file.getParent() + File.separator
						+ name);
				if (newFile.exists()) {
					CommonUtils.toast("该文件名已存在");
				} else {
					if (file.renameTo(newFile)) {
						contentChanged = true;
						file = newFile;
						file.setLastModified(System.currentTimeMillis());
						CommonUtils.toast("修改完成");
						modifyFileAuthority();
						blockNreset();
					} else {
						CommonUtils.toast("无法修改此文件");
						blockNreset();
					}
				}
			}
		}

	}

	private void calNshowMd5sum() {
		md5sumer = new Md5sumer(file);
		md5sum.setText(getString(R.string.cancel));
		md5sumPb.setMax(100);
		md5sumPb.setVisibility(View.VISIBLE);
		Runnable run = new Runnable() {
			public void run() {
				if (md5sumer != null && !md5sumer.isStop()) {
					md5sumPb.setProgress((int) md5sumer.getDonePercent());
					IConstants.MAIN_HANDLER.postDelayed(this, 500);
				}
			}
		};
		IConstants.MAIN_HANDLER.post(run);
		IConstants.THREAD_POOL.submit(new Runnable() {
			public void run() {
				final String str = md5sumer.start();
				if (md5sumer != null && !md5sumer.isStop()) {
					IConstants.MAIN_HANDLER.post(new Runnable() {
						public void run() {
							stopMd5sum();
							md5sum.setVisibility(View.GONE);
							md5sumEt.setVisibility(View.VISIBLE);
							md5sumEt.setText(str);
						}
					});
				}
			}
		});
	}

	private void stopMd5sum() {
		if (md5sumer != null) {
			md5sumer.stop();
			md5sumer = null;
			md5sumPb.setVisibility(View.GONE);
			md5sum.setText(getString(R.string.md5sum));
		}
	}

	private void initShareDialogNshow(int type,File file) {
		if (shareDialog == null) {
			shareDialog=FileChooser.initNshowShareDialog(type, file,null, null, this);
			shareListAdapter=(ShareListAdapter)shareDialog.gv.getAdapter();
		}else{
			FileChooser.initNshowShareDialog(type, file, shareDialog, shareListAdapter, this);
		}
	
	}

	// initOpenAsPopWindowNshow
	private void initOpenAsPopWindowNshow() {
		if (openAsPopWindow == null) {
			try {
				openAsPopWindow = new StrAryPopWindow(openAs, this,
						getResources().getStringArray(R.array.openaslist2),
						false, (int) getResources()
								.getDimension(R.dimen.dp_110),
						new OnPopListClickListener() {
							public void onclick(int position) {
								initShareDialogNshow(position,file);
							}
						});
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (openAsPopWindow != null)
			openAsPopWindow.showAsDropDown();
	}

	private boolean modifyFileAuthority() {
		boolean b = true;
		if (executable.isChecked() != file.canExecute())
			b &= file.setExecutable(executable.isChecked(), false);
		if (readable.isChecked() != file.canRead())
			b &= file.setReadable(readable.isChecked(), false);
		if (writeable.isChecked() != file.canWrite())
			b &= file.setWritable(writeable.isChecked(), false);
		return b;
	}

	private void dismissAllDialog() {
		CommonUtils.dismissDialogs(shareDialog);
		if (openAsPopWindow != null)
			openAsPopWindow.dismiss();
	}

}
