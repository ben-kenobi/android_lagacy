package com.what.yunbao.collection;

import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;

public class CollectionItemAdapter extends CursorAdapter implements OnCheckedChangeListener{
	public Set<Long> checkedIds;
	Context context;
	private int count;
	public  boolean isChoiceMode=false;
	
	public CollectionItemAdapter(Context context) {
		super(context,null,true);
		checkedIds=new HashSet<Long>();
		this.context=context;
		count = 0;
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return new CollectionItem(context,this);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		if(view instanceof CollectionItem){
			((CollectionItem)view).onBind(isChoiceMode,
                   checkedIds.contains(cursor.getLong(0)),cursor);
		}		
	}

    public boolean  toggleChoiceMode(View view) {
    	checkedIds.clear();
        if(isChoiceMode = !isChoiceMode){
        	if(view!=null)
        	view.setVisibility(View.VISIBLE);
        }else
        	if(view!=null)
        	view.setVisibility(View.GONE);
        notifyDataSetChanged();
        return isChoiceMode;
    }

    public void selectAll(boolean checked) {
        Cursor cursor = getCursor();
        for (int i = 0; i < getCount(); i++) {
            if (cursor.moveToPosition(i)) {             
                checkedIds.add(cursor.getLong(0));

            }
        }
    }




  

    @Override
    protected void onContentChanged() {
        super.onContentChanged();
        if(getCount()==0&&context instanceof CollectionActivity){
        	((CollectionActivity)context).showEmptyTip();
        }
        calcNotesCount();
        
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
        calcNotesCount();
    }

    private void calcNotesCount() {
        count = 0;
        for (int i = 0; i < getCount(); i++) {
            Cursor c = (Cursor) getItem(i);
            if (c != null) {
                    count++;
            } else {
                Log.e("what", "Invalid cursor");
                return;
            }
        }
    }

	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		Long id = (Long)buttonView.getTag();
		if(isChecked)
		checkedIds.add(id);
		else checkedIds.remove(id);
	}

}
