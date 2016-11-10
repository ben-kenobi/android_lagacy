package com.yf.accountmanager.sqlite;

import com.yf.accountmanager.common.IConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ISQLite extends SQLiteOpenHelper{
	
	public static final String DB_NAME="accountlib";
	
	public static final int VERSION=4;
	
	public static final String TABLE_ACCOUNT="account";
	
	public static final String TABLE_META_DATA="meta_data";
	
	public static final String TABLE_ACCESS="access";
	
	public static final String TABLE_IPATH="IPATH";
	
	public static final String TABLE_CONTACTS="contacts";
	
	private static ISQLite sqlite;
	private  ISQLite(Context context){
		super(context,DB_NAME,null,VERSION);
	}
	public static ISQLite getInstance(Context context){
		if(sqlite==null)
			synchronized (ISQLite.class) {
				if(sqlite==null) sqlite=new ISQLite(context);
			}
		return sqlite;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		System.out.println("databaseOncreate  @AccountSqlite");
		db.execSQL("create table "+TABLE_ACCOUNT+"("+
				AccountColumns.ID+" integer primary key autoincrement," +
				AccountColumns.MAILBOX+"  text," +
				AccountColumns.PASSPORT+"  text," +
				AccountColumns.PASSWORD+"  text," +
				AccountColumns.SITENAME+"  text," +
				AccountColumns.USERNAME+"  text," +
				AccountColumns.WEBSITE+"  text," +
				AccountColumns.PHONENUM+"  text," +
				AccountColumns.IDENTIFYING_CODE+"  text," +
				AccountColumns.ASK+"  text," +
				AccountColumns.ANSWER+"  text, " +
				AccountColumns.GROUP+" text default '' "+
				")");
		
		db.execSQL("create table "+TABLE_META_DATA+"("+
				MetaDataColumns.LANGUAGE+"  text,"+
				MetaDataColumns.ACCESSKEY+"  text"
				+")");
		db.execSQL("create table  "+TABLE_ACCESS+"  ("+
				AccessColumns.ID+" integer primary key autoincrement," +
				AccessColumns.NAME+"  text,"+
				AccessColumns.ACCESSIBILITY+"   integer"+
				")");
		db.execSQL("create table  "+TABLE_IPATH+"  ("+
				IPathColumns.ID+" integer primary key autoincrement," +
				IPathColumns.NAME+"  text,"+
				IPathColumns.PATH+"   text"+
				")");
		
		db.execSQL("create table  "+TABLE_CONTACTS+"  ("+
				ContactColumns.ID+" integer primary key autoincrement," +
				ContactColumns.NAME+"  text,"+
				ContactColumns.GROUP+"   text,"+
				ContactColumns.PHONE+"   text,"+
				ContactColumns.PHONE2+"   text,"+
				ContactColumns.TEL+"   text,"+
				ContactColumns.CHATACCOUNT+"   text,"+
				ContactColumns.EMAIL+"   text,"+
				ContactColumns.PS+"   text"+
				")");
		
		db.execSQL("insert into  "+TABLE_META_DATA+" values('chinese','')"
				);
		db.execSQL("insert into  "+TABLE_ACCESS+" values(1,?,1)"
				,new Object[]{IConstants.ACCOUNT});
		db.execSQL("insert into  "+TABLE_ACCESS+" values(2,?,1)"
				,new Object[]{IConstants.FILESYSTEM});
		db.execSQL("insert into  "+TABLE_ACCESS+" values(3,?,1)"
				,new Object[]{IConstants.CONTACTS});
		
		db.execSQL("insert into  "+TABLE_IPATH+"  values(1,?,?)",
				new Object[]{"root","/"});
		db.execSQL("insert into  "+TABLE_IPATH+"  values(2,?,?)",
				new Object[]{"mnt","/mnt"});
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
	
	
	public static interface AccountColumns{
		
		String ID="_id",
				
		WEBSITE="website",
		
		SITENAME="sitename",
		
		USERNAME="username",
		
		PASSWORD="password",
		
		PASSPORT="passport",
		
		MAILBOX="mailbox",
		
		PHONENUM="phonenum",
		
		IDENTIFYING_CODE="identifying_code",
		
		ASK="ask",
		
		ANSWER="answer",
		
		GROUP="groupname";
		
		
		
	}
	
	public static interface MetaDataColumns{
		
		String LANGUAGE="language",
				
				ACCESSKEY="accesskey";
				
	}
	
	public static interface AccessColumns{
		
		String ID="_id",
				
				NAME="name",
				
				ACCESSIBILITY="accessibility";
				
	}
	
	public static interface IPathColumns{
		String ID="_id",
				
				NAME="name",
				
				PATH="path";
				
	}
	
	public static interface ContactColumns{
		String ID=IConstants.ID,
				
				NAME="name",
				
				GROUP="groupname",
				
				PHONE="phone",
				
				PHONE2="phone2",
				
				TEL="tel",
				
				CHATACCOUNT="chataccount",
				
				EMAIL="email",
				
				PS="postscript";
				
	}

}
