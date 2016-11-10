package com.what.yunbao.util;

import com.what.yunbao.util.Notes.CollectionColumns;
import com.what.yunbao.util.Notes.AddressColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class CommonDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "note.db"; 

    private static final int DB_VERSION = 4;

    public interface TABLE {
//        public static final String NOTE = "note";

        public static final String ADDRESS = "address";
        
        public static final String ORDER = "myorder";//系统存在order表？
        
        public static final String COLLECTION = "collection";
    }

    private static final String TAG = "NotesDatabaseHelper";

    private static CommonDatabaseHelper mInstance;

    
    public CommonDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * 地址表
     */
    private static final String CREATE_ADDRESS_TABLE_SQL =
        "CREATE TABLE " + TABLE.ADDRESS + "(" +
            AddressColumns.ID + " INTEGER PRIMARY KEY," +
            AddressColumns.COUNT + " INTEGER NOT NULL DEFAULT 0," +
            AddressColumns.USER_ID + " TEXT NOT NULL DEFAULT ''," +    
            AddressColumns.CONTENT + " TEXT NOT NULL DEFAULT ''," +
            AddressColumns.PHONE + " TEXT NOT NULL DEFAULT ''," +
            AddressColumns.ADDRESS + " TEXT NOT NULL DEFAULT ''," +
            AddressColumns.CREATED_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000)," +
            AddressColumns.MODIFIED_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000)" +            
        ")";

    /**
     * 收藏表
     */
    private static final String CREATE_COLLECTION_TABLE_SQL = 
    	"CREATE TABLE " + TABLE.COLLECTION + "(" +
	    	CollectionColumns.ID + " INTEGER PRIMARY KEY," + 
	    	CollectionColumns.BUSINESS + " TEXT NOT NULL DEFAULT ''," + 
	    	CollectionColumns.PARENT_ID + " INTEGER NOT NULL DEFAULT 0," +
	    	CollectionColumns.CREATED_DATE + " INTEGER NOT NULL DEFAULT (strftime('%s','now') * 1000)," +
	    	CollectionColumns.BUSINESS_ID + " INTEGER UNIQUE," +
	    	CollectionColumns.ADDRESS + " TEXT NOT NULL DEFAULT ''," +
	    	CollectionColumns.DISTANCE + " REAL NOT NULL DEFAULT 0" + 
        ")";

    /**
     * address  Increase count when add
     */
    private static final String ADDRESS_INCREASE_COUNT_ON_INSERT_TRIGGER = 
    	"CREATE TRIGGER increase_address_count_on_insert " +
    		" AFTER INSERT ON " + TABLE.ADDRESS + 
    		" BEGIN " +
    		" UPDATE " + TABLE.ADDRESS +
    		" SET " + AddressColumns.COUNT + "=" + AddressColumns.COUNT + " + 1 " + ";" +
    		" END";
    /**
     * address  Decrease count when add
     */
    private static final String ADDRESS_DECREASE_COUNT_ON_DELETE_TRIGGER = 
    	"CREATE TRIGGER decrease_address_count_on_delete " +
    		" AFTER DELETE ON " + TABLE.ADDRESS + 
    		" BEGIN " +
    		" UPDATE " + TABLE.ADDRESS +
    		" SET " + AddressColumns.COUNT + "=" + AddressColumns.COUNT + " - 1 " + ";" +
    		" END";
//    /**
//     * 历史表
//     */
//    
//    private static final String CREATE_HISTORY_TABLE_SQL =
//    		"CREATE TABLE " + TABLE.
//    private static final String CREATE_DATA_NOTE_ID_INDEX_SQL =
//        "CREATE INDEX IF NOT EXISTS note_id_index ON " +
//        TABLE.Address + "(" + AddressColumns.NOTE_ID + ");";

//    /**
//     * Increase folder's note count when move note to the folder
//     */
//    private static final String NOTE_INCREASE_FOLDER_COUNT_ON_UPDATE_TRIGGER =
//        "CREATE TRIGGER increase_folder_count_on_update "+
//        " AFTER UPDATE OF " + NoteColumns.PARENT_ID + " ON " + TABLE.NOTE +
//        " BEGIN " +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.NOTES_COUNT + "=" + NoteColumns.NOTES_COUNT + " + 1" +
//        "  WHERE " + NoteColumns.ID + "=new." + NoteColumns.PARENT_ID + ";" +
//        " END";
//
//    /**
//     * Decrease folder's note count when move note from folder
//     */
//    private static final String NOTE_DECREASE_FOLDER_COUNT_ON_UPDATE_TRIGGER =
//        "CREATE TRIGGER decrease_folder_count_on_update " +
//        " AFTER UPDATE OF " + NoteColumns.PARENT_ID + " ON " + TABLE.NOTE +
//        " BEGIN " +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.NOTES_COUNT + "=" + NoteColumns.NOTES_COUNT + "-1" +
//        "  WHERE " + NoteColumns.ID + "=old." + NoteColumns.PARENT_ID +
//        "  AND " + NoteColumns.NOTES_COUNT + ">0" + ";" +
//        " END";
//
//    /**
//     * Increase folder's note count when insert new note to the folder
//     */
//    private static final String NOTE_INCREASE_FOLDER_COUNT_ON_INSERT_TRIGGER =
//        "CREATE TRIGGER increase_folder_count_on_insert " +
//        " AFTER INSERT ON " + TABLE.NOTE +
//        " BEGIN " +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.NOTES_COUNT + "=" + NoteColumns.NOTES_COUNT + " + 1" +
//        "  WHERE " + NoteColumns.ID + "=new." + NoteColumns.PARENT_ID + ";" +
//        " END";

//    /**
//     * Decrease folder's note count when delete note from the folder
//     */
//    private static final String NOTE_DECREASE_FOLDER_COUNT_ON_DELETE_TRIGGER =
//        "CREATE TRIGGER decrease_folder_count_on_delete " +
//        " AFTER DELETE ON " + TABLE.NOTE +
//        " BEGIN " +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.NOTES_COUNT + "=" + NoteColumns.NOTES_COUNT + "-1" +
//        "  WHERE " + NoteColumns.ID + "=old." + NoteColumns.PARENT_ID +
//        "  AND " + NoteColumns.NOTES_COUNT + ">0;" +
//        " END";

    
    
//    /**
//     * Update note's content when insert data with type {@link DataConstants#NOTE}
//     */
//    private static final String DATA_UPDATE_NOTE_CONTENT_ON_INSERT_TRIGGER =
//        "CREATE TRIGGER update_note_content_on_insert " +
//        " AFTER INSERT ON " + TABLE.DATA +
//        " WHEN new." + AddressColumns.MIME_TYPE + "='" + DataConstants.NOTE + "'" +
//        " BEGIN" +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.SNIPPET + "=new." + AddressColumns.CONTENT +
//        "  WHERE " + NoteColumns.ID + "=new." + AddressColumns.NOTE_ID + ";" +
//        " END";
//
//    /**
//     * Update note's content when data with {@link DataConstants#NOTE} type has changed
//     */
//    private static final String DATA_UPDATE_NOTE_CONTENT_ON_UPDATE_TRIGGER =
//        "CREATE TRIGGER update_note_content_on_update " +
//        " AFTER UPDATE ON " + TABLE.DATA +
//        " WHEN old." + AddressColumns.MIME_TYPE + "='" + DataConstants.NOTE + "'" +
//        " BEGIN" +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.SNIPPET + "=new." + AddressColumns.CONTENT +
//        "  WHERE " + NoteColumns.ID + "=new." + AddressColumns.NOTE_ID + ";" +
//        " END";
//
//    /**
//     * Update note's content when data with {@link DataConstants#NOTE} type has deleted
//     */
//    private static final String DATA_UPDATE_NOTE_CONTENT_ON_DELETE_TRIGGER =
//        "CREATE TRIGGER update_note_content_on_delete " +
//        " AFTER delete ON " + TABLE.DATA +
//        " WHEN old." + AddressColumns.MIME_TYPE + "='" + DataConstants.NOTE + "'" +
//        " BEGIN" +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.SNIPPET + "=''" +
//        "  WHERE " + NoteColumns.ID + "=old." + AddressColumns.NOTE_ID + ";" +
//        " END";

//    /**
//     * Delete datas belong to note which has been deleted
//     */
//    private static final String NOTE_DELETE_DATA_ON_DELETE_TRIGGER =
//        "CREATE TRIGGER delete_data_on_delete " +
//        " AFTER DELETE ON " + TABLE.NOTE +
//        " BEGIN" +
//        "  DELETE FROM " + TABLE.DATA +
//        "   WHERE " + AddressColumns.NOTE_ID + "=old." + NoteColumns.ID + ";" +
//        " END";
//    
////    	after delelte on note begin delete from data where data.noteid =old.note.id
//
//    /**
//     * Delete notes belong to folder which has been deleted
//     */
//    private static final String FOLDER_DELETE_NOTES_ON_DELETE_TRIGGER =
//        "CREATE TRIGGER folder_delete_notes_on_delete " +
//        " AFTER DELETE ON " + TABLE.NOTE +
//        " BEGIN" +
//        "  DELETE FROM " + TABLE.NOTE +
//        "   WHERE " + NoteColumns.PARENT_ID + "=old." + NoteColumns.ID + ";" +
//        " END";

//    /**
//     * Move notes belong to folder which has been moved to trash folder
//     */
//    private static final String FOLDER_MOVE_NOTES_ON_TRASH_TRIGGER =
//        "CREATE TRIGGER folder_move_notes_on_trash " +
//        " AFTER UPDATE ON " + TABLE.NOTE +
//        " WHEN new." + NoteColumns.PARENT_ID + "=" + Notes.ID_TRASH_FOLER +
//        " BEGIN" +
//        "  UPDATE " + TABLE.NOTE +
//        "   SET " + NoteColumns.PARENT_ID + "=" + Notes.ID_TRASH_FOLER +
//        "  WHERE " + NoteColumns.PARENT_ID + "=old." + NoteColumns.ID + ";" +
//        " END";
//    after update on note when new.note.parentid = 1 begin update note set note.parentid = 1 where note.parentid = old.note.id end
    

//    public void createNoteTable(SQLiteDatabase db) {
//        db.execSQL(CREATE_NOTE_TABLE_SQL);
//        reCreateNoteTableTriggers(db);
////        createSystemFolder(db);
//        Log.d(TAG, "note table has been created");
//    }

    private void reCreateNoteTableTriggers(SQLiteDatabase db) {
        db.execSQL("DROP TRIGGER IF EXISTS increase_folder_count_on_update");
        db.execSQL("DROP TRIGGER IF EXISTS decrease_folder_count_on_update");
        db.execSQL("DROP TRIGGER IF EXISTS decrease_folder_count_on_delete");
        db.execSQL("DROP TRIGGER IF EXISTS delete_data_on_delete");
        db.execSQL("DROP TRIGGER IF EXISTS increase_folder_count_on_insert");
        db.execSQL("DROP TRIGGER IF EXISTS folder_delete_notes_on_delete");
        db.execSQL("DROP TRIGGER IF EXISTS folder_move_notes_on_trash");

//        db.execSQL(NOTE_INCREASE_FOLDER_COUNT_ON_UPDATE_TRIGGER);
//        db.execSQL(NOTE_DECREASE_FOLDER_COUNT_ON_UPDATE_TRIGGER);
//        db.execSQL(NOTE_DECREASE_FOLDER_COUNT_ON_DELETE_TRIGGER);
//        db.execSQL(NOTE_DELETE_DATA_ON_DELETE_TRIGGER);
//        db.execSQL(NOTE_INCREASE_FOLDER_COUNT_ON_INSERT_TRIGGER);
//        db.execSQL(FOLDER_DELETE_NOTES_ON_DELETE_TRIGGER);
//        db.execSQL(FOLDER_MOVE_NOTES_ON_TRASH_TRIGGER);
    }

//    private void createSystemFolder(SQLiteDatabase db) {
//        ContentValues values = new ContentValues();
//
//        /**
//         * call record foler for call notes
//         */
//        values.put(NoteColumns.ID, Notes.ID_CALL_RECORD_FOLDER);
//        values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM);
//        db.insert(TABLE.NOTE, null, values);
//
//        /**
//         * root folder which is default folder
//         */
//        values.clear();
//        values.put(NoteColumns.ID, Notes.ID_ROOT_FOLDER);
//        values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM);
//        db.insert(TABLE.NOTE, null, values);
//
//        /**
//         * temporary folder which is used for moving note
//         */
//        values.clear();
//        values.put(NoteColumns.ID, Notes.ID_TEMPARAY_FOLDER);
//        values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM);
//        db.insert(TABLE.NOTE, null, values);
//
//        /**
//         * create trash folder
//         */
//        values.clear();
//        values.put(NoteColumns.ID, Notes.ID_TRASH_FOLER);
//        values.put(NoteColumns.TYPE, Notes.TYPE_SYSTEM);
//        db.insert(TABLE.NOTE, null, values);
//    }

    public void createAddressTable(SQLiteDatabase db) {
        db.execSQL(CREATE_ADDRESS_TABLE_SQL);
        reCreateAddressTableTriggers(db);
//        db.execSQL(CREATE_DATA_NOTE_ID_INDEX_SQL);     
        Log.d(TAG, "address table has been created");
    }

    public void createCollectionTable(SQLiteDatabase db){
    	db.execSQL(CREATE_COLLECTION_TABLE_SQL);
    }

    
    private void reCreateAddressTableTriggers(SQLiteDatabase db) {
        db.execSQL("DROP TRIGGER IF EXISTS increase_address_count_on_insert");
        db.execSQL("DROP TRIGGER IF EXISTS decrease_address_count_on_delete");

        db.execSQL(ADDRESS_INCREASE_COUNT_ON_INSERT_TRIGGER);
        db.execSQL(ADDRESS_DECREASE_COUNT_ON_DELETE_TRIGGER);
    }

    

    static synchronized CommonDatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CommonDatabaseHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        createNoteTable(db);
        createAddressTable(db);
        createCollectionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	db.execSQL("DROP TABLE IF EXISTS notes");
        onCreate(db);
    }
}
