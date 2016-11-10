package com.what.yunbao.address;

import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.AddressColumns;
import com.what.yunbao.util.Notes.UserContactColumns;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

public class WorkingAddress {
	private static final String TAG = "WorkingAddress";
    private long mDataId;   
    private String mContent;    
    private String mPhone;    
    private String mAddress;
    private Context mContext;  

    public static final String[] DATA_PROJECTION = new String[] {
//        AddressColumns.ID,
        AddressColumns.CONTENT,
        AddressColumns.PHONE,
        AddressColumns.ADDRESS
    };
    public static final String[] PROJECTION = new String[] {
//      AddressColumns.ID,
      UserContactColumns.USERNAME,
      UserContactColumns.PHONENUM,
      UserContactColumns.ADDRESS
  };

//	private static final int DATA_ID_COLUMN = 0;
	
	private static final int DATA_CONTENT_COLUMN = 0;
	
	private static final int DATA_PHONE_COLUMN = 1;
	
	private static final int DATA_ADDRESS_COLUMN = 2;


    // New address construct

    private WorkingAddress(Context context, long dataId) {
        mContext = context;
        mDataId = dataId; //AddressId from editActivity
        loadAddress();
    }
    
    private WorkingAddress(Context context){
    	mContext = context;
    }

    private void loadAddress() {
        Cursor cursor = mContext.getContentResolver().query(ContentUris.withAppendedId(Notes.
        		USER_CONTACT_URI,mDataId),PROJECTION,null,null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                    mContent = cursor.getString(DATA_CONTENT_COLUMN);//1
//                    //取得关联表的id 为更新时 更新关联表
//                    mNote.setTextDataId(cursor.getLong(DATA_ID_COLUMN));//0
                    mPhone = cursor.getString(DATA_PHONE_COLUMN);
                    mAddress = cursor.getString(DATA_ADDRESS_COLUMN);
            }
            cursor.close();
        } else {
            throw new IllegalArgumentException("Unable to find note's data with id " + mDataId);
        }
    }

    public static WorkingAddress createEmptyNote(Context context) {
        WorkingAddress note = new WorkingAddress(context);
        return note;
    }

    /**
     * Load address infomations by id
     * @param context
     * @param id
     * @return
     */
    public static WorkingAddress load(Context context, long id) {
        return new WorkingAddress(context, id);
    }

    public synchronized boolean saveAddress(String[] text) throws Exception {
    	if(!setWorkingText(text))return true;
          if (existInDatabase()) {
            return Address.syncNote(mContext, mDataId,AppUtils.createContentValues(PROJECTION, text));
          }else{
        	  return Address.insertNew(mContext,AppUtils.createContentValues(PROJECTION, text))>0;
          }
    }
    
    
    
    public boolean existInDatabase() {
        return mDataId > 0;
    }


    public boolean setWorkingText(String[] text) {
        if (TextUtils.equals(mContent, text[0])&&TextUtils.equals(mPhone+"", text[1])&&TextUtils.equals(mAddress, text[2])) 
        	return false;
        else{
        	 mContent = text[0];
             mPhone = text[1];
             mAddress = text[2]; 
             return true;
        }
    }

    public String getContent() {
        return mContent;
    }

    public String getPhone() {
		return mPhone;
	}

	public String getmAddress() {
		return mAddress;
	}

    public long getDataId() {
        return mDataId;
    }
}
