package fj.swsk.cn.eqapp.subs.collect.Common;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;

import java.util.HashSet;
import java.util.Set;

import fj.swsk.cn.eqapp.conf.IConstants;

public abstract class BaseUnstableCursorAdapter extends CursorAdapter {

	public Set<Integer> selectedIds;



	public boolean isMultiChoiceMode = false;

	public Context context;

	public BaseUnstableCursorAdapter(Context context) {
		super(context, null, true);
		this.context = context;
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

	}

	@Override
	public void changeCursor(Cursor cursor) {
		if (isMultiChoiceMode)
			toggleChoiceMode(false);
		super.changeCursor(cursor);
	}





}
