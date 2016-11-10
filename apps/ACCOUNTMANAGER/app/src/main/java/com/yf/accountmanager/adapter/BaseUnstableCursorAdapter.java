package com.yf.accountmanager.adapter;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.CommonService;
import com.yf.accountmanager.sqlite.ContactsService;

public abstract class BaseUnstableCursorAdapter extends CursorAdapter {

	public Set<Integer> selectedIds;

	public View deleteButton;

	public boolean isMultiChoiceMode = false;

	public Context context;

	public BaseUnstableCursorAdapter(Context context, View deleteButton) {
		super(context, null, true);
		this.context = context;
		this.deleteButton = deleteButton;
		this.selectedIds = new HashSet<Integer>();
	}

	public boolean toggleChoiceMode(boolean selectedAll) {
		if (isMultiChoiceMode)
			normalMode();
		else
			multiChoiceMode(selectedAll);
		notifyDataSetChanged();
		return isMultiChoiceMode;
	}

	public void multiChoiceMode(boolean selectedAll) {
		selectedIds.clear();
		isMultiChoiceMode = true;
		if (deleteButton != null)
			deleteButton.setVisibility(View.VISIBLE);

		if (selectedAll) {
			Cursor cursor = getCursor();
			if (cursor != null && cursor.moveToFirst()) {
				do {
					selectedIds.add(cursor.getInt(cursor
							.getColumnIndex(IConstants.ID)));
				} while (cursor.moveToNext());
			}
		}

	}

	public void normalMode() {
		selectedIds.clear();
		isMultiChoiceMode = false;
		if (deleteButton != null)
			deleteButton.setVisibility(View.GONE);
	}

	@Override
	public void changeCursor(Cursor cursor) {
		if (isMultiChoiceMode)
			toggleChoiceMode(false);
		super.changeCursor(cursor);
	}

	public void queryAll(String platform) {
		changeCursor(CommonService.queryAllByPlatform(platform));
	}

}
