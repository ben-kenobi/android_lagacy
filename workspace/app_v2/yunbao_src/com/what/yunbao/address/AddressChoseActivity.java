package com.what.yunbao.address;

import com.what.yunbao.R;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.AddressColumns;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AddressChoseActivity extends Activity {
	private static final String TAG = "AddressChoseActivity";
	private ListView address_lv;
	private ImageButton address_back_ib;
	private AddressItemAdapter adapter;
	private BackgroundQueryHandler mBackgroundQueryHandler;
    
    private static final int FOLDER_NOTE_LIST_QUERY_TOKEN = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_manager_chose);
		setUpView();

	}
	
	@Override
	protected void onStart() {
		Log.e(TAG, "onstart()....");
		super.onStart();
		startAsyncNotesListQuery();
	}

	private void setUpView() {
		Log.e(TAG, "setupView()....");
        mBackgroundQueryHandler = new BackgroundQueryHandler(this.getContentResolver());
        
		address_lv = (ListView) findViewById(R.id.lv_address_list);
		address_lv.setOnItemClickListener(new OnListItemClickListener());
		
		address_back_ib = (ImageButton) findViewById(R.id.ib_address_back);
		address_back_ib.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});
		
		adapter = new AddressItemAdapter(this);
		address_lv.setAdapter(adapter);
	}

	/**
	 * 查询信息
	 */
    private void startAsyncNotesListQuery() { 
		   Log.e(TAG, "startQuery()...");
	       mBackgroundQueryHandler.startQuery(FOLDER_NOTE_LIST_QUERY_TOKEN, null,
	                Notes.CONTENT_ADDRESS_URI, AddressItemData.ADDRESS_COLUMNS, AddressColumns.USER_ID +
	                	"=? OR " + AddressColumns.USER_ID + "=?", 
	                new String[] {"0","1"}, AddressColumns.MODIFIED_DATE + " DESC");
	}
    
	
	private final class BackgroundQueryHandler extends AsyncQueryHandler {
        public BackgroundQueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
             adapter.changeCursor(cursor);//会自动回收
        }
    }
	
    private class OnListItemClickListener implements OnItemClickListener {

    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view instanceof AddressItem) {
                AddressItemData item = ((AddressItem) view).getItemData();
                if (adapter.isInChoiceMode()) {
                    position = position - address_lv.getHeaderViewsCount();
                    adapter.setCheckedItem(position, !adapter.isSelectedItem(position)); 
                    return;
                }
                setAddrResult(item); 
            }
        }
    }
    /**
     * 修改地址
     * @param data
     */
    private void setAddrResult(AddressItemData data) {
        Intent intent = new Intent();
        intent.putExtra("name", data.getName());
        intent.putExtra("address", data.getAddress());
        intent.putExtra("phone", data.getPhone());
        setResult(RESULT_OK, intent);
        finish();
    }
    @Override
    protected void onResume() {   	
    	super.onResume();
    	Log.e(TAG, "onresume()....");
    }
    @Override
    protected void onPause() {    	
    	super.onPause();   	
    	Log.e(TAG, "onpause()....");
    }
    @Override
    protected void onStop() {
    	super.onStop();
    	Log.e(TAG, "onstop()....");
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	Log.e(TAG, "ondestory()....");
    }

}
