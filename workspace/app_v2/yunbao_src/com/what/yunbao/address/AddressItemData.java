package com.what.yunbao.address;

import com.what.yunbao.util.Notes.AddressColumns;
import com.what.yunbao.util.Notes.UserContactColumns;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class AddressItemData {
	public static final String [] ADDRESS_COLUMNS= new String[]{
	 	AddressColumns.ID,
        AddressColumns.USER_ID,
        AddressColumns.CREATED_DATE,
        AddressColumns.MODIFIED_DATE,
        AddressColumns.CONTENT,
        AddressColumns.PHONE,
        AddressColumns.ADDRESS,
        AddressColumns.COUNT
	};
	public static final String [] USERCONTACT_COLUMNS= new String[]{
	 	UserContactColumns.ID,
	 	UserContactColumns.ADDRESS,
	 	UserContactColumns.PHONE,
	 	UserContactColumns.PHONENUM,
	 	UserContactColumns.USERNAME,
	 	UserContactColumns.CREATE_TIME,
	 	UserContactColumns.MODIFY_TIME
	};
    private static final int ID_COLUMN                    = 0;
    private static final int USER_ID_COLUMN               = 1;
    private static final int CREATED_DATE_COLUMN          = 2;
    private static final int MODIFIED_DATE_COLUMN         = 3;
    private static final int NAME_COLUMN                  = 4;
    private static final int PHONE_COLUMN                 = 5;
    private static final int ADDRESS_COLUMN               = 6;
    private static final int ADDRESS_COUNT_COLUMN         = 7;
    private static final String TAG = "AddressItemData";
    
    private long mId;
    private long mCreatedDate;
    private long mModifiedDate;
    private String mUserId;
    private String mName;
    private String mPhone;
    private String mAddress;
    private int mCount;
	public AddressItemData(Context context,Cursor cursor){
		Log.e(TAG, "进入查询");
		mId = cursor.getLong(ID_COLUMN);
		mUserId = cursor.getString(USER_ID_COLUMN);
		mCreatedDate = cursor.getLong(CREATED_DATE_COLUMN);
		mModifiedDate = cursor.getLong(MODIFIED_DATE_COLUMN);		
		mName = cursor.getString(NAME_COLUMN);
		mPhone = cursor.getString(PHONE_COLUMN);
		mAddress = cursor.getString(ADDRESS_COLUMN);
		mCount = cursor.getInt(ADDRESS_COUNT_COLUMN);
		Log.e("查询结果", "Id "+mId);
		Log.e("查询结果", "creatd "+mCreatedDate);
		Log.e("查询结果", "mModif "+mModifiedDate);
		Log.e("查询结果", "mparent "+mUserId);//判断userid是否是0还是其他的 选大于0
		Log.e("查询结果", "mName "+mName);
		Log.e("查询结果", "mPHONe "+mPhone);
		Log.e("查询结果", "maddre "+mAddress);
	}
	public long getId() {
		return mId;
	}
	public long getCreatedDate() {
		return mCreatedDate;
	}
	public long getModifiedDate() {
		return mModifiedDate;
	}
	public String getUserId() {
		return mUserId;
	}
	public String getName() {
		return mName;
	}
	public String getPhone() {
		return mPhone;
	}
	public String getAddress() {
		return mAddress;
	}
	
}
