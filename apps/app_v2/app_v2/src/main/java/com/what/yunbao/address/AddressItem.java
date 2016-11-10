package com.what.yunbao.address;

import com.what.yunbao.R;
import com.what.yunbao.util.Notes.UserContactColumns;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddressItem extends LinearLayout{
	private TextView name_tv;
	private TextView phone_tv;
	private TextView address_tv;
	private CheckBox checkBox_cb;
	private ImageView arrow_iv;
	private AddressItemData mItemData;
	private RelativeLayout distinguish_iv;
	public Cursor mCursor;
	Integer [] color = new Integer[]{0xFF7CFC00,0xFF00EEEE,0xFFFFD700,0xFFAAAAAA};
	public AddressItem(Context context,Cursor cursor) {
		super(context);
		
        inflate(context, R.layout.address_item, this);
        name_tv = (TextView) findViewById(R.id.tv_address_list_name);
        phone_tv = (TextView) findViewById(R.id.tv_address_list_phone);
        address_tv = (TextView) findViewById(R.id.tv_address_list_addr);
        checkBox_cb = (CheckBox) findViewById(android.R.id.checkbox);
        arrow_iv = (ImageView) findViewById(R.id.iv_address_arrow);
        distinguish_iv = (RelativeLayout) findViewById(R.id.iv_address_distinguish);
        mCursor = cursor;
	}
	
	public void bind(Context context, AddressItemData data, boolean choiceMode, boolean checked) {
		if (choiceMode) {		
			arrow_iv.setVisibility(View.GONE);
            checkBox_cb.setVisibility(View.VISIBLE);
            checkBox_cb.setChecked(checked);
            
        } else {
        	arrow_iv.setVisibility(View.VISIBLE);
        	checkBox_cb.setVisibility(View.GONE);
        }
		

        mItemData = data;
        name_tv.setText(getFormattedSnippet(data.getName()));
        phone_tv.setText(getFormattedSnippet(data.getPhone()+""));
        address_tv.setText(getFormattedSnippet(data.getAddress()));

		distinguish_iv.setBackgroundColor(color[mCursor.getPosition() % 4]);

    }
	
	public void onBind(boolean isChoiceMode,boolean checked){
		if (isChoiceMode) {		
			arrow_iv.setVisibility(View.GONE);
            checkBox_cb.setVisibility(View.VISIBLE);
            checkBox_cb.setChecked(checked);
            
        } else {
        	arrow_iv.setVisibility(View.VISIBLE);
        	checkBox_cb.setVisibility(View.GONE);
        }
		
		name_tv.setText(mCursor.getString(mCursor.getColumnIndex(UserContactColumns.USERNAME)));
        phone_tv.setText(mCursor.getString(mCursor.getColumnIndex(UserContactColumns.PHONENUM)));
        address_tv.setText(mCursor.getString(mCursor.getColumnIndex(UserContactColumns.ADDRESS)));
		distinguish_iv.setBackgroundColor(color[mCursor.getPosition() % 4]);
	}
    public  String getFormattedSnippet(String snippet) {
        if (snippet != null) {
            snippet = snippet.trim();
            int index = snippet.indexOf('\n');
            if (index != -1) {
                snippet = snippet.substring(0, index);
            }
        }
        return snippet;
    }
    
    public AddressItemData getItemData() {
        return mItemData;
    }
}
