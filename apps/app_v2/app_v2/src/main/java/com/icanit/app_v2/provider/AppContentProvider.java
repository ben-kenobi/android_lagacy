package com.icanit.app_v2.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import com.icanit.app_v2.sqlite.AppSQLite;

public class AppContentProvider extends ContentProvider {
	private static final UriMatcher matcher;
	private static final String ID="_id";
    private AppSQLite sqlite;
    public static final String  AUTHORITY="com.icanit.app_v2";

    private static final int TABLE                 = 1;
    private static final int TABLE_ROW         = 2;


    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "*",TABLE);
        matcher.addURI(AUTHORITY, "*/#", TABLE_ROW);
    }

    @Override
    public boolean onCreate() {
       sqlite=AppSQLite.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase db =sqlite.getReadableDatabase();
        String table=uri.getPathSegments().get(0);
        switch (matcher.match(uri)) {
            case TABLE:
               cursor=db.query(table, projection, selection, selectionArgs,null, null, sortOrder);
                break;
            case TABLE_ROW:
               	cursor = db.query(table, projection,ID + "=" + ContentUris.parseId(uri)
                        + parseSelection(selection), selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("uri not match any resource provide by this provider | " + uri);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
    	long id=-1;
    	SQLiteDatabase db =sqlite.getReadableDatabase();
        String table=uri.getPathSegments().get(0);
        switch (matcher.match(uri)) {
            case TABLE:
              	id=db.insert(table, null, values);
                break;
            default:
                throw new IllegalArgumentException("uri not match any resource provide by this provider | " + uri);
        }
        if(id>0)
        	getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
    	int count=0;
    	SQLiteDatabase db =sqlite.getReadableDatabase();
        String table=uri.getPathSegments().get(0);
        switch (matcher.match(uri)) {
            case TABLE:
              	count=db.delete(table, selection, selectionArgs);
                break;
            case TABLE_ROW:
            	count = db.delete(table, ID+"="+ContentUris.parseId(uri)+parseSelection(selection), selectionArgs);
            	break;
            default:
                throw new IllegalArgumentException("uri not match any resource provide by this provider | " + uri);
        }
        if(count>0)
        	getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    	int count=0;
    	SQLiteDatabase db =sqlite.getReadableDatabase();
        String table=uri.getPathSegments().get(0);
        switch (matcher.match(uri)) {
            case TABLE:
              	count=db.update(table, values, selection, selectionArgs);
                break;
            case TABLE_ROW:
            	count = db.update(table,values, ID+"="+ContentUris.parseId(uri)+parseSelection(selection), selectionArgs);
            	break;
            default:
                throw new IllegalArgumentException("uri not match any resource provide by this provider | " + uri);
        }
        if(count>0)
        	getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    private String parseSelection(String selection) {
        return (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }
}
