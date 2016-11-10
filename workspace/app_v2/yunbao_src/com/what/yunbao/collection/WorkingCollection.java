package com.what.yunbao.collection;

import java.util.ArrayList;

import com.what.yunbao.util.AppUtils;
import com.what.yunbao.util.Notes;
import com.what.yunbao.util.Notes.CollectionColumns;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;

public class WorkingCollection {
	private Context mContext;
	private long mCollectionId;
	private long id;
	public WorkingCollection(Context context){
		this.mContext = context;
	}
//	public static WorkingCollection createCollection(Context context){
//		return new WorkingCollection(context);
//	}
	public synchronized boolean saveCollection(long businessId,String businessName,String Address){	
		if(!beCollected(businessId)){
			Collection mCollection = new Collection();
			id = Collection.getNewCollectionId(mContext, businessId);
			return mCollection.synCollection(mContext, id,AppUtils.createContentValues(new 
					String[]{CollectionColumns.BUSINESS,CollectionColumns.ADDRESS}, new String[]{businessName,Address}));
		}else{
			return false;
		}		
	}
	/**
	 * bussinessId 是否存在
	 * @param businessId
	 * @return
	 */
	public boolean beCollected(long businessId) {		
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(Notes.CONTENT_COLLECTION_URI, new String[]{CollectionColumns.ID}, 
					CollectionColumns.BUSINESS_ID + "=?", new String[]{String.valueOf(businessId)}, null);
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				id = cursor.getLong(0);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			cursor.close();
		}		
       return false;
    }
	
	public synchronized boolean removeCollection(long businessId){
		//方法一   需考虑事务
//        int i = mContext.getContentResolver().delete(Notes.CONTENT_COLLECTION_URI, 
//        		CollectionColumns.BUSINESS_ID + "='" +businessId+ "'", null);
//        if(i == 0){
//        	return false;
//        }
		
		//方法二
//		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//		ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(Notes.CONTENT_COLLECTION_URI);
//		builder.withSelection(CollectionColumns.BUSINESS_ID, new String[]{String.valueOf(businessId)});
//		ops.add(builder.build());
//		try {
//			ContentProviderResult [] result = mContext.getContentResolver().applyBatch(Notes.AUTHORITY, ops);
//			if(result == null|| result.length == 0||result[0] == null){
//				Log.e("WorkingCollection-error", "delete collection failed where the id is " + id );
//				return false;
//			}
//		} catch (RemoteException e) {	
//			Log.e("WorkingCollection-error", String.format("%s: %s", e.toString(), e.getMessage()));
//			e.printStackTrace();
//		} catch (OperationApplicationException e) {
//			Log.e("WorkingCollection-error", String.format("%s: %s", e.toString(), e.getMessage()));
//			e.printStackTrace();
//		}
		
		//方法三
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(
        		ContentUris.withAppendedId(Notes.CONTENT_COLLECTION_URI, id));
        ops.add(builder.build());
        try {
			ContentProviderResult [] result = mContext.getContentResolver().applyBatch(Notes.AUTHORITY, ops);
			if(result == null ||result.length ==0 || result[0] == null){
				Log.e("WorkingCollection-error", "delete collection failed where the id is " + id );
				return false;
			}
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.e("WorkingCollection-error", String.format("%s: %s", e.toString(), e.getMessage()));
		} catch (OperationApplicationException e) {
			e.printStackTrace();
			Log.e("WorkingCollection-error", String.format("%s: %s", e.toString(), e.getMessage()));
		}

		return false;
	}	
}
