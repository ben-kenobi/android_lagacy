package com.icanit.app_v2.sqlite;

import java.util.HashSet;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.icanit.app_v2.entity.AppMerchant;
import com.icanit.app_v2.exception.AppException;

public class UserBrowsingDaoImpl implements UserBrowsingDao {
	private AppSQLite sqlite;

	public UserBrowsingDaoImpl(Context context) {
		sqlite = AppSQLite.getInstance(context);
	}

	public boolean intsertBrowsedMerchant(AppMerchant merchant, String phone) throws AppException {
		ContentValues values = new ContentValues();
		values.put("merid", merchant.id);
		values.put("phone", phone);
		values.put("merType",merchant.type);
		values.put("merName", merchant.merName);
		values.put("location", merchant.location);
		values.put("merPhone", merchant.phone);
		values.put("pic", "");
		values.put("bookable", false);
		values.put("minCost", 0);
//		values.put("detail", merchant.detail);
		return sqlite.getWritableDatabase().insert(AppSQLite.TABLE_BROWSED_MERCHANT, null, values) != -1;
	}
	public boolean updateAddTime(AppMerchant merchant,String phone) throws AppException{
		ContentValues values = new ContentValues();
		values.put("addTime", System.currentTimeMillis());
		return sqlite.getWritableDatabase().update(AppSQLite.TABLE_BROWSED_MERCHANT, values,
				"merid=? AND phone=?", new String[]{merchant.id+"",phone})==1;
	}

	public boolean removeBrowsedMerchant(int merId, String phone)
			throws AppException {
		return sqlite.getWritableDatabase().delete(AppSQLite.TABLE_BROWSED_MERCHANT,
				"merid=? AND phone=?", new String[] { merId + "", phone }) > 0;
	}

	public int clearAllBrowsedMerchant(String phone) throws AppException {
		int count = sqlite.getWritableDatabase()
				.delete(AppSQLite.TABLE_BROWSED_MERCHANT, "phone=?",new String[]{phone});
		return count;
	}

	public Set<Integer> listBrowsedMerchantId(String phone) throws AppException {
		Cursor cursor=null;
		try {
			Set<Integer> merIdSet = new HashSet<Integer>();
			 cursor= sqlite.getReadableDatabase().rawQuery(
					"SELECT merid FROM "+AppSQLite.TABLE_BROWSED_MERCHANT+" WHERE phone=?",
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

	public void addToBrowsedMerchant(AppMerchant merchant, String phone) throws AppException {
		if(getBrowsedMerchantCountByMerId(merchant.id, phone)==0)
		intsertBrowsedMerchant(merchant, phone);
		else
			updateAddTime(merchant, phone);
	}

	public void cancelBrowsedMerchant(int merId, String phone) throws AppException {
		removeBrowsedMerchant(merId, phone);
	}

	public void clearBrowsedMerchant(String phone) throws AppException {
		clearAllBrowsedMerchant(phone);
	}

	@Override
	public int getBrowsedMerchantCountByMerId(int merId, String phone)
			throws AppException {
		Cursor cursor=null;
		try{
		cursor=sqlite.getReadableDatabase().query(AppSQLite.TABLE_BROWSED_MERCHANT, 
				new String[]{"_id"},"merid=? AND phone=?", 
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
