package com.what.yunbao.address;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.what.yunbao.util.Notes;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class AddressItemAdapter extends CursorAdapter{
	private static final String TAG = "AddressItemAdapter";
	private HashMap<Integer, Boolean> mSelectedIndex;
    private int mNotesCount;
    private boolean mChoiceMode;
    
    public static class AppWidgetAttribute{
        public int widgetId;
        public int widgetType;
    };
	public AddressItemAdapter(Context context){
		super(context, null);
		mSelectedIndex = new HashMap<Integer, Boolean>();
        mNotesCount = 0;		
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {		
		return new AddressItem(context, cursor);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if (view instanceof AddressItem) {
//           AddressItemData itemData = new AddressItemData(context, cursor);
//            ((AddressItem) view).bind(context, itemData, mChoiceMode,
//                    isSelectedItem(cursor.getPosition()));
			((AddressItem) view).onBind(mChoiceMode,isSelectedItem(cursor.getPosition()));
        }
	}

    public void setCheckedItem(final int position, final boolean checked) {
        mSelectedIndex.put(position, checked);
        notifyDataSetChanged();
    }

    public boolean isInChoiceMode() {
        return mChoiceMode;
    }

    public void setChoiceMode(boolean mode) {
        mSelectedIndex.clear();
        mChoiceMode = mode;
        notifyDataSetChanged();

    }

    public void selectAll(boolean checked) {
        Cursor cursor = getCursor();
        for (int i = 0; i < getCount(); i++) {
            if (cursor.moveToPosition(i)) {             
                setCheckedItem(i, checked);

            }
        }
    }

    public HashSet<Long> getSelectedItemIds() {
        HashSet<Long> itemSet = new HashSet<Long>();
        for (Integer position : mSelectedIndex.keySet()) {
            if (mSelectedIndex.get(position) == true) {
                Long id = getItemId(position);
                if (id == Notes.ID_ROOT_FOLDER) {
                    Log.d(TAG, "Wrong item id, should not happen");
                } else {
                    itemSet.add(id);
                }
            }
        }

        return itemSet;
    }


    public int getSelectedCount() {
        Collection<Boolean> values = mSelectedIndex.values();
        if (null == values) {
            return 0;
        }
        Iterator<Boolean> iter = values.iterator();
        int count = 0;
        while (iter.hasNext()) {
            if (true == iter.next()) {
                count++;
            }
        }
        return count;
    }

    public boolean isAllSelected() {
        int checkedCount = getSelectedCount();
        return (checkedCount != 0 && checkedCount == mNotesCount);
    }

    public boolean isSelectedItem(final int position) {
        if (null == mSelectedIndex.get(position)) {
            return false;
        }
        return mSelectedIndex.get(position);
    }

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
        calcNotesCount();
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        calcNotesCount();
    }

    private void calcNotesCount() {
        mNotesCount = 0;
        for (int i = 0; i < getCount(); i++) {
            Cursor c = (Cursor) getItem(i);
            if (c != null) {
                    mNotesCount++;
            } else {
                Log.e(TAG, "Invalid cursor");
                return;
            }
        }
    }
    
	
	
}
