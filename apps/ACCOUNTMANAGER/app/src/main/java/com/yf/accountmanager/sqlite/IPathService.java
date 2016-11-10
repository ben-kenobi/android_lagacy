package com.yf.accountmanager.sqlite;

import java.io.File;
import java.util.Set;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.yf.accountmanager.sqlite.ISQLite.IPathColumns;

public class IPathService {
	public static String TABLE = ISQLite.TABLE_IPATH,

	ID = IPathColumns.ID,

	NAME = IPathColumns.NAME,

	PATH = IPathColumns.PATH;

	public static boolean containPath(String path) {
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getReadableDatabase();
		Cursor c = null;
		try {
			c = db.rawQuery("select " + ID + " from " + TABLE + " where "
					+ PATH + "=?", new String[] { path });
			boolean b = c != null && c.getCount() > 0;
			return b;
		} finally {
			if (c != null)
				c.close();
		}
	}

	public static boolean insert(File file) {
		if (file == null)
			return false;
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(NAME, file.getName());
		cv.put(PATH, file.getAbsolutePath());
		return db.insert(TABLE, null, cv) > 0;
	}

	public static boolean deleteBYPath(String path) {
		if (TextUtils.isEmpty(path))
			return false;
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getWritableDatabase();
		return db.delete(TABLE, PATH + "=?", new String[] { path }) > 0;
	}

	public static boolean deleteById(int id) {
		if (id == 0)
			return false;
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getWritableDatabase();
		return db.delete(TABLE, ID + "=?",
				new String[] { Integer.toString(id) }) > 0;
	}
	
	
	public static boolean batchDelete(Set<Integer> ids){
		if(ids==null||ids.isEmpty()) return false;
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context).getWritableDatabase();
		int count = db.delete(TABLE, ID+"  in("
				+ CommonService.concatenateIds(ids) + ")", null);
		return count > 0;
	}

	public static boolean update(String name, String path, int id) {
		if (id == 0)
			return false;
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getWritableDatabase();
		ContentValues cv = new ContentValues();
		if (name != null)
			cv.put(NAME, name);
		if (!TextUtils.isEmpty(path))
			cv.put(PATH, path);
		return db.update(TABLE, cv, ID +"=?",
				new String[] { Integer.toString(id) }) > 0;
	}

	public static Cursor query() {
		SQLiteDatabase db = ISQLite.getInstance(CommonService.context)
				.getReadableDatabase();
		Cursor c = null;
		c = db.rawQuery("select * from " + TABLE ,null);
		return c;
	}

}
