package com.what.yunbao.collection;

import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.CollectionColumns;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class Collection {
	private static final String TAG = "Collection";
	public static synchronized long getNewCollectionId(Context context,long businessId){

		ContentValues value = new ContentValues();		
		value.put(CollectionColumns.BUSINESS_ID, businessId);	
        
		Uri uri = context.getContentResolver().insert(Notes.CONTENT_COLLECTION_URI, value);
		long collectionId = 0;
		try {
			collectionId = Long.valueOf(uri.getPathSegments().get(1));
        } catch (NumberFormatException e) {
            Log.e(TAG, "Get id error :" + e.toString());
            collectionId = 0;
        }
		if(collectionId == -1){
			throw new IllegalStateException("ERROR CREATE ID");
		}
		return collectionId;
	}
	public boolean synCollection(Context context,long collectionId,ContentValues cv){
		if(collectionId <= 0){
			return false;
		}
		return pushIntoContentResolver(context, collectionId,cv)==1;
	} 
	private  int pushIntoContentResolver(Context context, long collectionId,ContentValues mValues) {
         mValues.put(CollectionColumns.CREATED_DATE, System.currentTimeMillis());
         mValues.put(CollectionColumns.DISTANCE, 2.85);
         return context.getContentResolver().update(
     		  ContentUris.withAppendedId(Notes.CONTENT_COLLECTION_URI, collectionId), mValues, null,null);
     }
}
