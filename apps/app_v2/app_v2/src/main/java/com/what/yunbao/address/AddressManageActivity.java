package com.what.yunbao.address;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.what.yunbao.R;
import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.Notes;

public class AddressManageActivity extends Activity implements OnItemLongClickListener{
	private static final String TAG = "AddressManagerActivity";
	private ListView address_lv;
	private ImageButton address_edit_ib;
	private ImageButton address_back_ib;
	private AddressItemAdapter adapter;
	private ContentResolver mContentResolver;
	private BackgroundQueryHandler mBackgroundQueryHandler;
	private RelativeLayout delete_rl;
		
	private long mCurrentFolderId;
	
    private final static int REQUEST_CODE_OPEN_NODE = 102;
    private final static int REQUEST_CODE_NEW_NODE  = 103;
    
    private static final int FOLDER_NOTE_LIST_QUERY_TOKEN =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_manager);
		setUpView();
		((LinearLayout)findViewById(R.id.rl_address_add)).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(AddressManageActivity.this,AddressEditActivity.class);
				intent.setAction(Intent.ACTION_INSERT_OR_EDIT);
				AddressManageActivity.this.startActivityForResult(intent, REQUEST_CODE_NEW_NODE);
			}
		});	
		try {
			startAsyncNotesListQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (resultCode == RESULT_OK
                && (requestCode == REQUEST_CODE_OPEN_NODE || requestCode == REQUEST_CODE_NEW_NODE)) {
//             adapter.changeCursor(null);
         } else {
             super.onActivityResult(requestCode, resultCode, data);
         }
     }
	
	private void setUpView() {
		Log.e(TAG, "setupView()....");
		mContentResolver = this.getContentResolver();
        mBackgroundQueryHandler = new BackgroundQueryHandler(this.getContentResolver());
        
		address_lv = (ListView) findViewById(R.id.lv_address_list);
		address_lv.setOnItemClickListener(new OnListItemClickListener());
		address_lv.setOnItemLongClickListener(this);
		
		address_edit_ib = (ImageButton) findViewById(R.id.ib_address_edit);
		address_edit_ib.setOnClickListener(new MenuClickEvent());
		address_back_ib = (ImageButton) findViewById(R.id.ib_address_back);
		address_back_ib.setOnClickListener(new MenuClickEvent());
		
		delete_rl = (RelativeLayout) findViewById(R.id.what);
		delete_rl.setOnClickListener(new MenuClickEvent());
		
		adapter = new AddressItemAdapter(this);
		address_lv.setAdapter(adapter);
		
		mCurrentFolderId = Notes.ID_ROOT_FOLDER;
	}

	/**
	 * 查询信息
	 * 原先想法：PARENT_ID 用于区分是否是删除的address 地址还在，不显示
	 * 现在直接从库表删除地址 显示的地址：user_id 默认为空+登录的user_id的地址
	 * @throws Exception 
	 */
    private void startAsyncNotesListQuery() throws Exception { 
	       mBackgroundQueryHandler.startQuery(FOLDER_NOTE_LIST_QUERY_TOKEN, null,
	                Notes.USER_CONTACT_URI, AddressItemData.USERCONTACT_COLUMNS,"phone=?", 
	                new String[] {AppUtils.getLoginPhoneNum()}, "modifyTime DESC");
	}
    

	/**
	 * 删除信息
	 */
    private void batchDelete() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... unused) {
                if (batchMoveToFolder(mContentResolver, adapter.getSelectedItemIds(), Notes.ID_TRASH_FOLER)) {
                    Log.e(TAG, "Move addr to trash folder error, should not happens");
                }
                return null;
            }
			protected void onPostExecute(Void what) {
				adapter.setChoiceMode(false);
		        address_lv.setLongClickable(true);
		        delete_rl.setVisibility(View.GONE);
			};

        }.execute();
    }
    
    /**
     * 原先想法:删除到回收文件夹
     * 现在直接删除
     * @param resolver
     * @param ids
     * @param folderId
     * @return
     */
    public  boolean batchMoveToFolder(ContentResolver resolver, HashSet<Long> ids,
            long folderId) {
        if (ids == null) {
            return true;
        }

        ArrayList<ContentProviderOperation> operationList=new ArrayList<ContentProviderOperation>();
        for (long id : ids) {
        	ContentProviderOperation.Builder builder=ContentProviderOperation.newDelete(
        			ContentUris.withAppendedId(Notes.USER_CONTACT_URI, id));
        	operationList.add(builder.build());
        }

        try {
            ContentProviderResult[] results = resolver.applyBatch(Notes.AUTH, operationList);
            if (results == null || results.length == 0 || results[0] == null) {
                return false;
            }
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, String.format("%s: %s", e.toString(), e.getMessage()));
        } catch (OperationApplicationException e) {
            Log.e(TAG, String.format("%s: %s", e.toString(), e.getMessage()));
        }
        return false;
    }
    
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view, int position,
			long id) {	
		delete_rl.setVisibility(View.VISIBLE);		
		adapter.setChoiceMode(true);
        address_lv.setLongClickable(false);		
		return false;
	}
	
	class MenuClickEvent implements OnClickListener{

		@Override
		public void onClick(View v) {	
			if (v.getId() == R.id.what) {
				if (adapter.getSelectedCount() == 0) {
	                Toast.makeText(AddressManageActivity.this, "未选择任何项",
	                        Toast.LENGTH_SHORT).show();
	                return;
	            }
				AlertDialog.Builder builder = new AlertDialog.Builder(AddressManageActivity.this);
				builder.setTitle("删除信息");
				builder.setIcon(android.R.drawable.ic_dialog_alert);
				builder.setMessage(getString(R.string.alert_message_delete_notes,
	                                     adapter.getSelectedCount()));
				builder.setPositiveButton(android.R.string.ok,
	                                     new DialogInterface.OnClickListener() {
	                                         public void onClick(DialogInterface dialog,
	                                                 int which) {
	                                             batchDelete();
	                                         }
	                                     });
				builder.setNegativeButton(android.R.string.cancel, null);
				builder.show();
			} else if (v.getId() == R.id.ib_address_edit) {
				if(delete_rl.getVisibility()==View.GONE){
					delete_rl.setVisibility(View.VISIBLE);
					adapter.setChoiceMode(true);
			        address_lv.setLongClickable(false);
			        
				}else{
					adapter.setChoiceMode(false);
			        address_lv.setLongClickable(true);
			        delete_rl.setVisibility(View.GONE);
				}
			} else if (v.getId() == R.id.ib_address_back) {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
			}				
		}		
	}
	
	private final class BackgroundQueryHandler extends AsyncQueryHandler {
        public BackgroundQueryHandler(ContentResolver contentResolver) {
            super(contentResolver);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
             adapter.changeCursor(cursor);//会自动回收
             startManagingCursor(cursor);
        }
    }
	
    private class OnListItemClickListener implements OnItemClickListener {

    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (view instanceof AddressItem) {
//                AddressItemData item = ((AddressItem) view).getItemData();
                if (adapter.isInChoiceMode()) {
                    position = position - address_lv.getHeaderViewsCount();
                    adapter.setCheckedItem(position, !adapter.isSelectedItem(position));                       
                    return;
                }
//                openNode(item); 
                openNode(adapter.getCursor());
            }
        }
    }
    /**
     * 修改地址
     * @param data
     */
    private void openNode(AddressItemData data) {
        Intent intent = new Intent(this, AddressEditActivity.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.putExtra(Intent.EXTRA_UID, data.getId());
        
        this.startActivityForResult(intent, REQUEST_CODE_OPEN_NODE);
    }
    private void openNode(Cursor cursor){
    	 Intent intent = new Intent(this, AddressEditActivity.class);
         intent.setAction(Intent.ACTION_VIEW);
         intent.putExtra(Intent.EXTRA_UID, cursor.getLong(0));
         this.startActivityForResult(intent, REQUEST_CODE_OPEN_NODE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//    	int action = event.getAction();
    	if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
    		if(delete_rl.getVisibility()==View.VISIBLE){
    			adapter.setChoiceMode(false);
		        address_lv.setLongClickable(true);
		        delete_rl.setVisibility(View.GONE); 
		        return true;
		        
			}else {
				finish();
				overridePendingTransition(R.anim.anim_fromleft_toup6, R.anim.anim_down_toright6);
				return true;
			}
    	}else{
    		return super.onKeyDown(keyCode, event);
    	}
	
    }
    

}
