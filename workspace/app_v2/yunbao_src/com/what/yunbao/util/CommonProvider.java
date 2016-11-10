package com.what.yunbao.util;

import com.what.yunbao.util.CommonDatabaseHelper.TABLE;
import com.what.yunbao.util.Notes.CollectionColumns;
import com.what.yunbao.util.Notes.AddressColumns;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CommonProvider extends ContentProvider {
    private static final UriMatcher mMatcher;

    private CommonDatabaseHelper mHelper;

    private static final int URI_NOTE                  = 1;
    private static final int URI_NOTE_ITEM             = 2;
    private static final int URI_ADDRESS               = 3;
    private static final int URI_ADDRESS_ITEM          = 4;
    private static final int URI_COLLECTION            = 5;
    private static final int URI_COLLECTION_ITEM       = 6;


    static {
//    	mMatcher.match(uri)
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(Notes.AUTHORITY, "note", URI_NOTE);
        mMatcher.addURI(Notes.AUTHORITY, "note/#", URI_NOTE_ITEM);
        mMatcher.addURI(Notes.AUTHORITY, "address", URI_ADDRESS);
        mMatcher.addURI(Notes.AUTHORITY, "address/#", URI_ADDRESS_ITEM);
        mMatcher.addURI(Notes.AUTHORITY, "collection", URI_COLLECTION);
        mMatcher.addURI(Notes.AUTHORITY, "collection/#", URI_COLLECTION_ITEM);
//        mMatcher.addURI(Notes.AUTHORITY, "myorder", URI_MYORDER);
//        mMatcher.addURI(Notes.AUTHORITY, "myorder/#", URI_MYORDER_ITEM);
        
    }

    @Override
    public boolean onCreate() {
        mHelper = CommonDatabaseHelper.getInstance(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        Cursor c = null;

        SQLiteDatabase db = mHelper.getReadableDatabase();
        String id = null;
        switch (mMatcher.match(uri)) {
            case URI_ADDRESS:
            	Log.e("查询uridata", "msg....................................");
                c = db.query(TABLE.ADDRESS, projection, selection, selectionArgs, null, null,
                        sortOrder);
                break;
            case URI_ADDRESS_ITEM:
            	Log.e("查询uridataiemt", "msg....................................");
                id = uri.getPathSegments().get(1);
                c = db.query(TABLE.ADDRESS, projection, AddressColumns.ID + "=" + id
                        + parseSelection(selection), selectionArgs, null, null, sortOrder);
                break;
            case URI_COLLECTION:
            	Log.e("查询collection", "msg....................................");
            	c = db.query(TABLE.COLLECTION, projection, selection, selectionArgs, null, null,
                        sortOrder);
            	break;
            	
            case URI_COLLECTION_ITEM:
            	Log.e("查询collection", "msg....................................");
            	id = uri.getPathSegments().get(1);
            	c = db.query(TABLE.COLLECTION, projection, selection, selectionArgs, null, null,
                        sortOrder);
            	break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long dataId = 0,insertedId = 0, collectionId = 0;
        switch (mMatcher.match(uri)) {
//            case URI_NOTE:
//            	Log.e("insert", "note");
//                insertedId = noteId = db.insert(TABLE.NOTE, null, values);
//                break;
            case URI_ADDRESS:
            	Log.e("insert", "data");
            	
//                if (values.containsKey(AddressColumns.NOTE_ID)) {
//                    noteId = values.getAsLong(AddressColumns.NOTE_ID);
//                    
//                } else {
//                    Log.d(TAG, "Wrong data format without note id:" + values.toString());
//                }
                insertedId = dataId = db.insert(TABLE.ADDRESS, null, values);
                break;
            case URI_COLLECTION:
            	Log.e("insert", "collection");
            	insertedId = collectionId = db.insert(TABLE.COLLECTION, null, values);
            	break;
           
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // Notify the what uri
//        if (noteId > 0) {
//            getContext().getContentResolver().notifyChange(
//                    ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, noteId), null);
//        }
        if (dataId > 0) {
            getContext().getContentResolver().notifyChange(
                    ContentUris.withAppendedId(Notes.CONTENT_ADDRESS_URI, dataId), null);
        }
        if(collectionId > 0){
        	getContext().getContentResolver().notifyChange(ContentUris.withAppendedId(Notes.CONTENT_ADDRESS_URI, collectionId), null);
        }
       
        return ContentUris.withAppendedId(uri, insertedId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String id = null;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        boolean deleteData = false;
        switch (mMatcher.match(uri)) {
//            case URI_NOTE:
//                selection = "(" + selection + ") AND " + NoteColumns.ID + ">0 ";
//                count = db.delete(TABLE.NOTE, selection, selectionArgs);
//                break;
//            case URI_NOTE_ITEM:
//                id = uri.getPathSegments().get(1);
//                /**
//                 * ID that smaller than 0 is system folder which is not allowed to
//                 * trash
//                 */
//                long noteId = Long.valueOf(id);
//                if (noteId <= 0) {
//                    break;
//                }
//                count = db.delete(TABLE.NOTE,
//                        NoteColumns.ID + "=" + id + parseSelection(selection), selectionArgs);
//                break;
            case URI_ADDRESS:
            	Log.e("delete", "data");
                count = db.delete(TABLE.ADDRESS, selection, selectionArgs);
               
                deleteData = true;
                break;
            case URI_ADDRESS_ITEM:
            	Log.e("delete", "dataitem");
                id = uri.getPathSegments().get(1);
                count = db.delete(TABLE.ADDRESS,
                        AddressColumns.ID + "=" + id + parseSelection(selection), selectionArgs);
                deleteData = true;
                break;
            case URI_COLLECTION:
            	Log.e("delete", "collection");
            	count = db.delete(TABLE.COLLECTION, selection, selectionArgs);
            	break;          	
            case URI_COLLECTION_ITEM:
            	Log.e("delete", "collectionitem");
            	id = uri.getPathSegments().get(1);
            	count = db.delete(TABLE.COLLECTION, CollectionColumns.ID + "=" + id + parseSelection(selection), selectionArgs);
            	break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
//        db 事务管理？？
        if (count > 0) {
        	//TODO 
//            if (deleteData) {
//                getContext().getContentResolver().notifyChange(Notes.CONTENT_NOTE_URI, null);//删除条目大于0
//            }
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
    	//TODO 把update隐藏
        int count = 0;
        String id = null;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        boolean updateData = false;
        switch (mMatcher.match(uri)) {
//            case URI_NOTE:
//            	Log.e("update", "note");
////                increaseNoteVersion(-1, selection, selectionArgs);
//                count = db.update(TABLE.NOTE, values, selection, selectionArgs);
//                break;
//            case URI_NOTE_ITEM:
//            	Log.e("update", "noteitem");
//                id = uri.getPathSegments().get(1);
////                increaseNoteVersion(Long.valueOf(id), selection, selectionArgs);
//                count = db.update(TABLE.NOTE, values, NoteColumns.ID + "=" + id
//                        + parseSelection(selection), selectionArgs);
//                break;
            case URI_ADDRESS:
            	Log.e("update", "data");
                count = db.update(TABLE.ADDRESS, values, selection, selectionArgs);
                updateData = true;
                break;
            case URI_ADDRESS_ITEM:
            	Log.e("update", "dataitem");
                id = uri.getPathSegments().get(1);
                count = db.update(TABLE.ADDRESS, values, AddressColumns.ID + "=" + id
                        + parseSelection(selection), selectionArgs);
                updateData = true;
                break;
            case URI_COLLECTION:
            	count = db.update(TABLE.COLLECTION, values, selection, selectionArgs);
            	break;
            	
            case URI_COLLECTION_ITEM:
            	id = uri.getPathSegments().get(1);
            	count = db.update(TABLE.COLLECTION, values, CollectionColumns.ID + "=" + id + parseSelection(selection), selectionArgs);
            	break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if (count > 0) {
//            if (updateData) {
//                getContext().getContentResolver().notifyChange(Notes.CONTENT_NOTE_URI, null);
//            }
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    private String parseSelection(String selection) {
        return (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
    }


    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

}
