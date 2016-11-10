package com.yf.accountmanager.sqlite;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yf.accountmanager.AccountDetailActivity;
import com.yf.accountmanager.AccountListActivity;
import com.yf.accountmanager.R;
import com.yf.accountmanager.common.IConstants;
import com.yf.accountmanager.sqlite.ISQLite.AccessColumns;
import com.yf.accountmanager.sqlite.ISQLite.AccountColumns;
import com.yf.accountmanager.sqlite.ISQLite.ContactColumns;
import com.yf.accountmanager.sqlite.ISQLite.MetaDataColumns;
import com.yf.accountmanager.util.CommonUtils;
import com.yf.contacts.ContactDetailActivity;
import com.yf.contacts.ContactsActivity;

public class CommonService {
	public static Context context;

	public static boolean checkAccessKey(String accessKey) {
		if (TextUtils.isEmpty(accessKey))
			return false;
		return accessKey.equalsIgnoreCase(getAccessKey());
	}
	
	public static String getAccessKey(){
		String accessKey=null;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = ISQLite.getInstance(context)
					.getWritableDatabase();
			cursor = db.rawQuery("select " + MetaDataColumns.ACCESSKEY
					+ " from " + ISQLite.TABLE_META_DATA, null);
			if (cursor.moveToFirst()) {
				accessKey= cursor.getString(cursor
						.getColumnIndex(MetaDataColumns.ACCESSKEY));
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return accessKey;
	}
	

	/**
	 * method: login
	 * 
	 * @param accessKey
	 * @return
	 */
	public static boolean login(String accessKey) {
		if (TextUtils.isEmpty(accessKey))
			return false;
		boolean b = false;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = ISQLite.getInstance(context)
					.getWritableDatabase();
			cursor = db.rawQuery("select " + MetaDataColumns.ACCESSKEY
					+ " from " + ISQLite.TABLE_META_DATA, null);
			if (cursor.moveToFirst()) {
				String key = cursor.getString(cursor
						.getColumnIndex(MetaDataColumns.ACCESSKEY));
				// System.out.println(key + "   @key  accountService");
				if (TextUtils.isEmpty(key)) {
					b = true;
					db.execSQL("update " + ISQLite.TABLE_META_DATA + " set "
							+ MetaDataColumns.ACCESSKEY + "=?",
							new Object[] { accessKey });
				} else {
					if (!(b = key.equalsIgnoreCase(accessKey)))
						CommonUtils.toast("ÑéÖ¤Ê§°Ü¡£");

				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return b;
	}

	/**
	 * method:modifyAccessKey
	 * 
	 * @param newAccessKey
	 * @return
	 */

	public static boolean modifyAccessKey(String oldAccessKey,
			String newAccessKey) {
		if (TextUtils.isEmpty(newAccessKey) || TextUtils.isEmpty(oldAccessKey))
			return false;
		boolean b = false;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = ISQLite.getInstance(context)
					.getWritableDatabase();
			cursor = db.rawQuery("select " + MetaDataColumns.ACCESSKEY
					+ " from " + ISQLite.TABLE_META_DATA, null);
			if (cursor.moveToFirst()) {
				String key = cursor.getString(cursor
						.getColumnIndex(MetaDataColumns.ACCESSKEY));
				if (oldAccessKey.equalsIgnoreCase(key)) {
					db.execSQL("update " + ISQLite.TABLE_META_DATA + " set "
							+ MetaDataColumns.ACCESSKEY + "=?",
							new Object[] { newAccessKey });
					b = true;
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return b;
	}

	/**
	 * methodname:getAccessibilityByName
	 * 
	 * @param name
	 * @return
	 */

	public static boolean isAccessKeyEnable(String name) {
		if (TextUtils.isEmpty(name))
			return true;
		Cursor cursor = null;
		try {
			SQLiteDatabase db = ISQLite.getInstance(context)
					.getReadableDatabase();
			cursor = db.rawQuery("select  " + AccessColumns.ACCESSIBILITY
					+ "  from  " + ISQLite.TABLE_ACCESS + "  where "
					+ AccessColumns.NAME + " =?", new String[] { name });
			if (cursor != null && cursor.moveToFirst() && cursor.getInt(0) == 0)
				return false;
			return true;
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	public static boolean modifyAccessibility(String name, int accessibility) {
		if (TextUtils.isEmpty(name))
			return false;
		SQLiteDatabase db = ISQLite.getInstance(context).getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(AccessColumns.ACCESSIBILITY, accessibility);
		int i = db.update(ISQLite.TABLE_ACCESS, cv, AccessColumns.NAME + "=?",
				new String[] { name });
		return i > 0;
	}

	public static boolean toggleAccessibility(String name) {
		if (isAccessKeyEnable(name))
			return disableAccessKey(name);
		else
			return enableAccessKey(name);
	}

	/**
	 * enableAccessKey
	 * 
	 * @param name
	 * @return
	 */
	public static boolean enableAccessKey(String name) {
		return modifyAccessibility(name, 1);
	}

	/**
	 * disableAccessKey
	 * 
	 * @param name
	 * @return
	 */
	public static boolean disableAccessKey(String name) {
		return modifyAccessibility(name, 0);
	}

	public static Intent getIntentByAccessName(String name,Context context) {
		if (IConstants.ACCOUNT.equals(name)) {
			return new Intent(context,AccountListActivity.class);
		} else if (IConstants.FILESYSTEM.equals(name)) {
			return  new Intent(context.getResources().getString(R.string.filesystemIntentAction)).
					addCategory(Intent.CATEGORY_DEFAULT);
		} else if (IConstants.CONTACTS.equals(name)) {
			return new Intent(context,ContactsActivity.class);
		}
		return null;
	}
	
	public static Intent getItemDetailIntentByPlatform(String platform,Context context){
		if (IConstants.ACCOUNT.equals(platform)) {
			return new Intent(context,AccountDetailActivity.class);
		}else if (IConstants.CONTACTS.equals(platform)) {
			return new Intent(context,ContactDetailActivity.class);
		}
		return null;
	}
	
	public static boolean deleteItemsByPlatform(Set<Integer> ids,String platform){
		boolean b = false;
		if (IConstants.ACCOUNT.equals(platform)) {
			b=AccountService.removeAccount(ids);
		} else if (IConstants.CONTACTS.equals(platform)) {
			b=ContactsService.removeContacts(ids);
		}
		return b;
	}
	
	public static Cursor queryDistinctColumnByPlatform(String platform,String column){
		Cursor cursor = null;
		if(IConstants.ACCOUNT.equals(platform)){
			if(TextUtils.isEmpty(column))
				column=AccountColumns.GROUP;
			cursor=AccountService.queryDistinctColumn(column);
		}else if(IConstants.CONTACTS.equals(platform)){
			if(TextUtils.isEmpty(column))
				column=ContactColumns.GROUP;
			cursor=ContactsService.queryDistinctColumn(column);
		}
		return cursor;
	}
	
	public static Cursor queryAllByPlatform(String platform){
		Cursor cursor=null;
		if(IConstants.ACCOUNT.equals(platform))
			cursor=AccountService.queryByColumn("",IConstants.MATCH_ALL);
		else if(IConstants.CONTACTS.equals(platform))
			cursor= ContactsService.queryByColumn("",IConstants.MATCH_ALL);
		return cursor;
	}
	
	public static Cursor queryByPlatformNColumn(String platform,String colName,String colValue){
		Cursor cursor=null;
		if(IConstants.ACCOUNT.equals(platform))
			cursor=AccountService.queryByColumn(colName, colValue);
		else if(IConstants.CONTACTS.equals(platform))
			cursor= ContactsService.queryByColumn(colName, colValue);
		return cursor;
	}
	
	public static void batchInsertByPlatform(String platform,List<ContentValues> cvs){
		if(IConstants.ACCOUNT.equals(platform))
			AccountService.batchAddAccount(cvs);
		else if(IConstants.CONTACTS.equals(platform))
			ContactsService.batchInsert(cvs);
	}

	
	/**
	 * method:concatenateIds
	 * 
	 * @param idset
	 * @return
	 */
	public static String concatenateIds(Set<Integer> idset) {
		StringBuffer sb = new StringBuffer();
		for (Iterator<Integer> it = idset.iterator(); it.hasNext();) {
			sb.append(it.next() + ",");
		}
		if (sb.length() > 1)
			sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
}
