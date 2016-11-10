package com.icanit.app_v2.sqlite;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;
import com.icanit.app_v2.util.AppUtil;

public class UserCollectionDaoImpl implements UserCollectionDao {
	private AppSQLite sqlite;

	public UserCollectionDaoImpl(Context context) {
		sqlite = AppSQLite.getInstance(context);
	}

	public boolean addToCollection(AppMerchant merchant, String phone) throws AppException {
		ContentValues values = new ContentValues();
		values.put("merid", merchant.id);
		values.put("phone", phone);
		values.put("merType",merchant.type);
		values.put("merName", merchant.merName);
		values.put("location", merchant.location);
		values.put("merPhone", merchant.phone);
		values.put("pic", "");
		values.put("bookable", false);
		values.put("minCost",0);
//		values.put("detail", merchant.detail);
		return sqlite.getWritableDatabase().insert(AppSQLite.TABLE_COLLECTION, null, values) != -1;
	}

	public boolean removeCollection(int merId, String phone)
			throws AppException {
		return sqlite.getWritableDatabase().delete(AppSQLite.TABLE_COLLECTION,
				"merid=? AND phone=?", new String[] { merId + "", phone }) > 0;
	}

	public int clearAllCollections(String phone) throws AppException {
		int count = sqlite.getWritableDatabase()
				.delete(AppSQLite.TABLE_COLLECTION, "phone=?",new String[]{phone});
		return count;
	}

	public Set<Integer> listCollections(String phone) throws AppException {
		Cursor cursor=null;
		try {
			Set<Integer> merIdSet = new HashSet<Integer>();
			 cursor= sqlite.getReadableDatabase().rawQuery(
					"SELECT merid FROM collection WHERE phone=?",
					new String[] { phone });
			if (cursor.moveToFirst()) {
				do {
					merIdSet.add(cursor.getInt(0));
				} while (cursor.moveToNext());
			}
			return merIdSet;
		} finally {
			if(cursor!=null) cursor.close();
		}
	}

	public void collectMerchant(AppMerchant merchant, String phone) throws AppException {
		if(getCollectionCountByMerId(merchant.id, phone)==0)
		addToCollection(merchant, phone);
	}

	public void cancelCollection(int merId, String phone) throws AppException {
		if (removeCollection(merId, phone)) {
//			AppUtil.appContext.collectedMerIdSet.remove(merId);
		}
	}

	public void clearCollections(String phone) throws AppException {
		if (clearAllCollections(phone) > 0) {
//			AppUtil.appContext.collectedMerIdSet.clear();
		}
	}

	@Override
	public int getCollectionCountByMerId(int merId, String phone)
			throws AppException {
		Cursor cursor=null;
		try{
			cursor=sqlite.getReadableDatabase().query(AppSQLite.TABLE_COLLECTION, new String[]{"_id"},"merid=? AND phone=?", 
					new String[] { merId + "", phone },null, null,null);
			int count =0;
			if(cursor!=null)
			count=cursor.getCount();
			return count;
		}finally{
			if(cursor!=null) cursor.close();
		}
	}


}
