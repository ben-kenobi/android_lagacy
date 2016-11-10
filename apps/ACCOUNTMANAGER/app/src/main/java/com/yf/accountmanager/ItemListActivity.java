package com.yf.accountmanager;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.yf.accountmanager.adapter.BaseUnstableCursorAdapter;
import com.yf.accountmanager.adapter.GvPickerAdapter;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;
import com.yf.accountmanager.ui.CustomizedDialog;
import com.yf.accountmanager.ui.FileChooser;
import com.yf.accountmanager.util.CommonUtils;

public abstract class ItemListActivity extends FileChooserActivity implements
		OnClickListener {

	public ListView lv;

	public BaseUnstableCursorAdapter cursorAdapter;

	public View deleteButton;

	public static final int REQUEST_CODE_ITEMDETAIL = 1;

	public void onAddMenuItemClicked() {
		startActivityForResult(
				CommonService.getItemDetailIntentByPlatform(platform, this),
				REQUEST_CODE_ITEMDETAIL);
	}

	
	protected void initPicker() {
		picker = CustomizedDialog.initChooseDialog("ȫ��", this);
		picker.title.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Cursor cursor = CommonService.queryAllByPlatform(platform);
				cursorAdapter.changeCursor(cursor);
				picker.dismiss();
			}
		});
		picker.gv.setAdapter(pickerAdapter = new GvPickerAdapter(this));
		picker.gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				if (pickerAdapter.cursor.moveToPosition(position)) {
					Cursor cursor = CommonService.queryByPlatformNColumn(
							platform,
							pickerAdapter.cursor.getColumnName(0),
							pickerAdapter.cursor.getString(0));
					cursorAdapter.changeCursor(cursor);
				}
				picker.dismiss();
			}
		});
		picker.setOnDismissListener(new OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				pickerAdapter.requery(null, null);
			}
		});
	}
	
	public void initPickerNshow() {
		if (picker == null) {
			initPicker();
		}
		pickerAdapter.requery(platform, null);
		picker.show();
	}


	protected void initMainContent() {
		lv.setAdapter(cursorAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View self,
					int position, long id) {
				if (cursorAdapter.isMultiChoiceMode) {
					int aid = cursorAdapter.getCursor().getInt(
							cursorAdapter.getCursor().getColumnIndex(
									AccountColumns.ID));
					if (cursorAdapter.selectedIds.contains(aid)) {
						self.setActivated(false);
						cursorAdapter.selectedIds.remove(aid);
					} else {
						cursorAdapter.selectedIds.add(aid);
						self.setActivated(true);
					}

				} else {
					startActivityForResult(
							CommonService
									.getItemDetailIntentByPlatform(platform,
											ItemListActivity.this)
									.putExtra(
											platform,
											CommonUtils
													.encapsulateCursorAsContentValues(cursorAdapter
															.getCursor())),
							REQUEST_CODE_ITEMDETAIL);
				}
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View self,
					int position, long id) {
				return !cursorAdapter.toggleChoiceMode(false);
			}
		});

	}

	@Override
	protected void onDeleteMenuItemClicked() {
		if (cursorAdapter != null)
			cursorAdapter.toggleChoiceMode(true);
	}

	protected void bindListeners() {
		deleteButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == deleteButton) {
			deleteSelectedItems();
		}
	}

	private void deleteSelectedItems() {
		if (cursorAdapter == null || cursorAdapter.selectedIds.isEmpty()) {
			CommonUtils.toast("δѡ���κ���Ŀ");
			return;
		}

		FileChooser
				.initNoticeDialogNshow(
						"ȷ��ɾ���� " + cursorAdapter.selectedIds.size()
								+ " ����Ŀ?\nɾ���󽫲��ɻָ�", this);
		noticeDialog.setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (CommonService.deleteItemsByPlatform(
								cursorAdapter.selectedIds, platform)) {
							CommonUtils.toast("ɾ���ɹ�");
							cursorAdapter.getCursor().requery();
							if (cursorAdapter.isMultiChoiceMode)
								cursorAdapter.toggleChoiceMode(false);
						} else {
							CommonUtils.toast("ɾ��ʧ��");
						}
					}
				});
		noticeDialog.setNegativeButton("ȡ��",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (cursorAdapter.isMultiChoiceMode)
							cursorAdapter.toggleChoiceMode(false);
					}
				});

	}

	/**
	 * override
	 */

	protected void backOperation() {
		if (cursorAdapter != null && cursorAdapter.isMultiChoiceMode)
			cursorAdapter.toggleChoiceMode(false);
		else {
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_ITEMDETAIL) {
			if (resultCode != 0)
				if (cursorAdapter != null && cursorAdapter.getCursor() != null)
					cursorAdapter.getCursor().requery();
		} else
			super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		if (cursorAdapter != null && cursorAdapter.isMultiChoiceMode)
			cursorAdapter.toggleChoiceMode(false);
		super.onResume();
	}

	@Override
	protected void doOnDestroy() {
		if (cursorAdapter != null && cursorAdapter.getCursor() != null)
			cursorAdapter.changeCursor(null);
	}

}
