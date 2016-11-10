package com.yf.accountmanager.sqlite;

import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ISQLite.ContactColumns;

public class ContactsService {
	public static String table=ISQLite.TABLE_CONTACTS;
	
	public static long insert(ContentValues cv){
		if(cv==null	) return 0;
		SQLiteDatabase  db = ISQLite.getInstance(CommonService.context).getWritableDatabase();
		cv.remove(IConstants.ID);
		return db.insert(table, null, cv);
	}
	
	public static void batchInsert(List<ContentValues> cvs){
		if(cvs==null||cvs.isEmpty()) return ;
		SQLiteDatabase  db = ISQLite.getInstance(CommonService.context).getWritableDatabase();
		for(int i=0;i<cvs.size();i++){
			ContentValues cv=cvs.get(i);
			cv.remove(IConstants.ID);
			db.insert(table, null, cv);
		}
	}
	
	public static boolean removeContacts(Set<Integer> idset) {
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context).getWritableDatabase();
		int count = db.delete(table, "_id in("
				+ CommonService.concatenateIds(idset) + ")", null);
		return count > 0;
	}
	
	public static boolean updateContacts(ContentValues cv, long id) {
		cv.remove(IConstants.ID);
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context).getWritableDatabase();
		int count = db.update(table, cv, "_id=?",
				new String[] { id + "" });
		return count > 0;
	}
	
	public static Cursor queryByColumn(String colName,String colValue){
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context).getReadableDatabase();
		Cursor cursor = null;
		if (IConstants.MATCH_ALL.equals(colValue)) {
			cursor = db.rawQuery("select * from " + table
					+ "  order by " + ContactColumns.NAME, null);
		} else {
			cursor = db.rawQuery("select * from " + table
					+ "  where  " + colName + "=?  order by "
					+ ContactColumns.NAME, new String[] { colValue });
		}
		return cursor;
	}
	
	
	public static Cursor queryDistinctColumn(String columnName) {
		return ISQLite.getInstance(CommonService.context).getReadableDatabase()
				.rawQuery(
						"select  distinct  " + columnName + "  from "
								+ table  + " order by  " + columnName, null);
	}
	
	public static Cursor queryDistinctColumnWithId(String columnName) {
		return ISQLite.getInstance(CommonService.context).getReadableDatabase()
				.rawQuery(
						"select   " + columnName + " ,_id  from "
								+ table  + " group by "+columnName+" order by  " + columnName, null);
	}
	
	
	public static Cursor queryByColFragment(String fragment,String columnName){
		if(TextUtils.isEmpty(columnName)) return null;
		if(ContactColumns.PHONE.equals(columnName))
			return queryByPhoneNumFragment(fragment);
		else 
			return queryByColumnFragment(fragment, columnName);
	}
	
	private static Cursor queryByColumnFragment(String fragment,String columnName){
		return ISQLite.getInstance(CommonService.context).getReadableDatabase()	
				.rawQuery("select * from " + table
						+ "  where  " + columnName + " like '%"+fragment+"%'  order by "
						+ ContactColumns.NAME,null);
	}
	
	private static Cursor queryByPhoneNumFragment(String fragment){
		return ISQLite.getInstance(CommonService.context).getReadableDatabase()
					.rawQuery("select * from "+table+"  where "+ContactColumns.PHONE+" like '%"+
							fragment+"%' or  "+ContactColumns.PHONE2+" like '%"+fragment+"%'  order by "
							+ContactColumns.NAME, null);
	}
	
}
