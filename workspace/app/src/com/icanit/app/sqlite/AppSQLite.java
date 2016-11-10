package com.icanit.app.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;
import android.util.Log;

public final  class AppSQLite extends SQLiteOpenHelper{
	private static AppSQLite appSqlite;
	private  AppSQLite(Context context){
		super(context,"iptvAppDB",null,8);
		Log.d("sqlite","@  AppSQLiteConstructor");
	}
	public static AppSQLite getInstance(Context context){
		if(appSqlite==null)
			appSqlite=new AppSQLite(context);
		return appSqlite;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("sqlite","oncreateDB:"+db.getVersion()+","+db+" @  AppSQLite onCreate");
		db.execSQL("create table merchant_info(_id integer primary key autoincrement,name text,location text)");
		db.execSQL("create table district_info(_id integer primary key autoincrement,merchandize_name text)");
		db.execSQL("create table shopping_cart(_id integer primary key autoincrement,phone char(11)," +
				"status char(1),quantity integer,prod_id integer,prod_name nvarchar(30),prod_desc nvarchar(200),add_time integer,present_price real,former_price real,snap text)");
		db.execSQL("insert into merchant_info values(null,'·������','���ֿڴ���ٻ�')");
		db.execSQL("insert into merchant_info values(null,'ʩ��������','���ֿڶ����ٻ�')");
		db.execSQL("insert into merchant_info values(null,'������','�ҼҺ����ҹ�')");
		db.execSQL("INSERT INTO district_info values(null,'����')");
		db.execSQL("INSERT INTO district_info values(null,'�Ϻ�')");
		db.execSQL("INSERT INTO district_info values(null,'����')");
		db.execSQL("INSERT INTO district_info values(null,'����')");
		db.execSQL("INSERT INTO district_info values(null,'����')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d("sqlite",db.getVersion()+"|"+oldVersion+"|"+newVersion+" @  AppSQLite onUpgrade");
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
