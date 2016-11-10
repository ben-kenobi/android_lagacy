package com.icanit.app_v2.sqlite;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.icanit.app_v2.entity.UserContact;
import com.icanit.app_v2.exception.AppException;

public class UserContactDaoImpl implements UserContactDao {
	private AppSQLite sqlite;

	public UserContactDaoImpl(Context context) {
		sqlite = AppSQLite.getInstance(context);
	}
	@Override
	public List<UserContact> listUserContactsByPhone(String phone) throws AppException {
		if(phone==null||"".equals(phone)) return null;
		List<UserContact> contactList = new ArrayList<UserContact>();
		Cursor cursor = sqlite.getReadableDatabase().rawQuery(
				"SELECT * FROM " + " user_contact  WHERE phone=?",
				new String[] {phone});
		if (cursor.moveToFirst()) {
			do {
				int i = 0;
				UserContact contact = new UserContact();
				contact._id=cursor.getInt(i++);
				contact.phoneNum=cursor.getString(i++);
				contact.username=cursor.getString(i++);
				contact.address=cursor.getString(i++);
				contact.phone=cursor.getString(i++);
				contact.createTime=cursor.getLong(i++);
				contact.modifyTime=cursor.getLong(i++);
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return contactList;
	}

	@Override
	public boolean  saveUserContact(UserContact contact) throws AppException {
		if(contact==null) return false;
		ContentValues values = new ContentValues();
		for (Field field : contact.getClass().getDeclaredFields()) {
			try {
				values.put(field.getName(), 
						field.get(contact) + "");
			} catch (Exception e) {
				e.printStackTrace();
				throw new AppException(e);
			}
		}
		values.remove("_id");
		values.remove("createTime");values.remove("modifyTime");
		contact._id = (int) sqlite.getWritableDatabase().insert("user_contact",
				null, values);
		Log.d("sqliteInfo", "_id=" +contact._id + "  @UserContactDaoImpl  addUserContact");
		return contact._id!=-1;
		
	}

	@Override
	public boolean  deleteContact(UserContact contact) throws AppException {
		if(contact==null) return false;
		int i=sqlite.getWritableDatabase().delete("user_contact", "_id=? AND phone=? ", new String[]{contact._id+"",contact.phone});
		return i==1;
	}
	
}
