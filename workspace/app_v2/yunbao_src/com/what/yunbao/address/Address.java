package com.what.yunbao.address;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.AddressColumns;
import com.what.yunbao.util.Notes.UserContactColumns;


public class Address {
    private static final String TAG = "Address";
    /**
     * Create a new Address id for adding a new Address to databases
     * @throws Exception 
     */
    public static synchronized long insertNew(Context context,ContentValues cv) throws Exception {
        cv.put(UserContactColumns.PHONE,AppUtils.getLoginPhoneNum());
        Uri uri = context.getContentResolver().insert(Notes.USER_CONTACT_URI, cv);
        long addressId = 0;
        try {
            addressId =ContentUris.parseId(uri);
        } catch (NumberFormatException e) {
            addressId = 0;
        }
        return addressId;
    }


    /**
     * Address save
     */
    public static boolean syncNote(Context context, long dataId,ContentValues cv) {
    	if(dataId<=0) return false;
        cv.put(UserContactColumns.MODIFY_TIME, System.currentTimeMillis());               
        return  context.getContentResolver().update(
        	ContentUris.withAppendedId(Notes.USER_CONTACT_URI, dataId), cv, null,null) ==1;	
    }
}
