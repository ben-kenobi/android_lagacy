package com.icanit.app_v2.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public final  class AppSQLite extends SQLiteOpenHelper{
	
	public static final String DB_NAME="iptvAppDB";
	
	public static final int VERSION=16;
	
	public static final String TABLE_USER_CONTACT="user_contact";
	
	public static final String TABLE_MERCHANT_INFO="merchant_info";
	
	public static final String TABLE_DISTRICT_INFO="district_info";
	
	public static final String TABLE_SHOPPING_CART="shopping_cart";
	
	public static final String TABLE_COLLECTION="collection";
	
	public static final String TABLE_BROWSED_MERCHANT="browsed_merchant";
	
	private static AppSQLite appSqlite;
	private  AppSQLite(Context context){
		super(context,DB_NAME,null,VERSION);
		Log.d("sqlite","@  AppSQLiteConstructor");
	}
	public static AppSQLite getInstance(Context context){
		if(appSqlite==null)
			synchronized (AppSQLite.class) {
				if(appSqlite==null) appSqlite=new AppSQLite(context);
			}
		return appSqlite;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("sqlite","oncreateDB:"+db.getVersion()+","+db+" @  AppSQLite onCreate");
		
		db.execSQL("create table "+TABLE_USER_CONTACT+"(_id integer primary key autoincrement,phoneNum char(11)," +
				"username nvarchar(30),address nvarchar(200),phone char(11),createTime INTEGER  " +
				" DEFAULT (strftime('%s','now')*1000),modifyTime INTEGER DEFAULT (strftime('%s','now')*1000))");
		
		db.execSQL("create table "+TABLE_MERCHANT_INFO+"(_id integer primary key autoincrement,name text,location text)");
		
		db.execSQL("create table "+TABLE_DISTRICT_INFO+"(_id integer primary key autoincrement,merchandize_name text)");
		
		db.execSQL("create table "+TABLE_SHOPPING_CART+"(_id integer primary key autoincrement,phone char(11)," +
				"status char(1),quantity integer,prod_id text,prod_name nvarchar(30),prod_desc nvarchar(200)," +
				"add_time integer,present_price real,former_price real,snap text," +
				"postscript nvarchar(200),mer_id integer,cate_id integer,mer_name text,mer_location text)");
		
		db.execSQL("create table "+TABLE_COLLECTION+"(_id integer primary key autoincrement,merid integer," +
				"phone char(11),merType integer,merName nvarchar(30),location text,map text,merPhone text," +
				"pic text,bookable text,minCost real,addTime INTEGER DEFAULT(strftime('%s','now')*1000))");
		
		db.execSQL("create table "+TABLE_BROWSED_MERCHANT+"(_id integer primary key autoincrement,merid integer," +
				"phone char(11),merType integer,merName nvarchar(30),location text,map text,merPhone text," +
				"pic text,bookable text,minCost real,addTime INTEGER DEFAULT(strftime('%s','now')*1000))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("sqlite",db.getVersion()+"|"+oldVersion+"|"+newVersion+" @  AppSQLite onUpgrade");
		db.execSQL("create table "+TABLE_BROWSED_MERCHANT+"(_id integer primary key autoincrement,merid integer," +
				"phone char(11),merType integer,merName nvarchar(30),location text,map text,merPhone text," +
				"pic text,bookable text,minCost real,addTime INTEGER DEFAULT(strftime('%s','now')*1000))");
	}
	public Cursor getMerchantInfoSet(){
		SQLiteDatabase db = getReadableDatabase();
		Log.d("sqlite",db.getVersion()+" @  AppSQLite getMerchantInfoSet");
		Cursor cursor=db.rawQuery("SELECT * FROM merchant_info",null);
		return cursor;
	}
	public Cursor getDistrictInfoSet(){
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor=db.rawQuery("SELECT * FROM district_info",null);
		ContentValues cv = new ContentValues();
		return cursor;
	}
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
	}
	
	

}
