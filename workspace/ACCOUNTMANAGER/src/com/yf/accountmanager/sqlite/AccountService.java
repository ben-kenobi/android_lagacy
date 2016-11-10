package com.yf.accountmanager.sqlite;

import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;

public class AccountService {
	public static Context context;


	/**
	 * method:addAccount
	 * 
	 * @param cv
	 * @return
	 */

	public static long addAccount(ContentValues cv) {
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		cv.remove(IConstants.ID);
		long id = db.insert(ISQLite.TABLE_ACCOUNT, null, cv);
		return id;
	}

	/**
	 * method:batchAddAccount
	 * 
	 * @param cvs
	 * @return
	 */

	public static boolean batchAddAccount(List<ContentValues> cvs) {
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		for (int i = 0; i < cvs.size(); i++) {
			ContentValues cv = cvs.get(i);
			cv.remove(IConstants.ID);
			long id = db.insert(ISQLite.TABLE_ACCOUNT, null, cv);
		}
		return true;
	}

	/**
	 * method:removeAccount
	 * 
	 * @param idset
	 * @return
	 */
	public static boolean removeAccount(Set<Integer> idset) {
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		int count = db.delete(ISQLite.TABLE_ACCOUNT, "_id in("
				+ CommonService.concatenateIds(idset) + ")", null);
		return count > 0;
	}

	/**
	 * method:removeAccountBySite
	 * 
	 * @param sitename
	 * @return
	 */
	public static boolean removeAccountBySite(String sitename) {
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		int count = db.delete(ISQLite.TABLE_ACCOUNT, AccountColumns.SITENAME
				+ "=?", new String[] { sitename });
		return count > 0;
	}

	/**
	 * method:updateAccount
	 * 
	 * @param cv
	 * @param id
	 * @return
	 */
	public static boolean updateAccount(ContentValues cv, long id) {
		cv.remove(IConstants.ID);
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		int count = db.update(ISQLite.TABLE_ACCOUNT, cv, "_id=?",
				new String[] { id + "" });
		return count > 0;
	}

	/**
	 * method:queryByColName
	 * 
	 * @param colName
	 * @return
	 */
	
	public static Cursor queryByColumn(String colName,String colValue){
		SQLiteDatabase db = ISQLite.getInstance(context).getReadableDatabase();
		Cursor cursor = null;
		if (IConstants.MATCH_ALL.equals(colValue)) {
			cursor = db.rawQuery("select * from " + ISQLite.TABLE_ACCOUNT
					+ "  order by " + AccountColumns.SITENAME, null);
		} else {
			cursor = db.rawQuery("select * from " + ISQLite.TABLE_ACCOUNT
					+ "  where  " + colName + "=?  order by "
					+ AccountColumns.SITENAME, new String[] { colValue });
		}
		return cursor;
	}
	


	/**
	 * method: queryDistinctColumn
	 * 
	 * @param columnName
	 * @return
	 */
	public static Cursor queryDistinctColumnWithId(String columnName) {
		return ISQLite
				.getInstance(context)
				.getReadableDatabase()
				.rawQuery(
						"select  " + columnName + ",_id  from "
								+ ISQLite.TABLE_ACCOUNT + " group by "
								+ columnName + " order by  " + columnName, null);
	}

	
	public static Cursor queryDistinctColumn(String columnName){
		return ISQLite
				.getInstance(context)
				.getReadableDatabase()
				.rawQuery(
						"select distinct " + columnName
								+ "  from " + ISQLite.TABLE_ACCOUNT
								+ "  order by " + columnName, null);
	}

}
