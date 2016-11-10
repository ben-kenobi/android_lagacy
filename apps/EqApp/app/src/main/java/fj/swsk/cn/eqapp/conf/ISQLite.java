package fj.swsk.cn.eqapp.conf;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;


public class ISQLite extends SQLiteOpenHelper {

    public static final String DB_NAME = "scenedatas.db";

    public static final int VERSION = 2;

    public static final String TABLE_MEDIA_DATAS = "t_media";
    public static final String TABLE_SCENE = "t_scene";
    public static final String TABLE_LOCATIONINFO = "t_locationinfo";


    private static ISQLite sqlite;

    private ISQLite(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static ISQLite getInstance(Context context) {
        if (sqlite == null)
            synchronized (ISQLite.class) {
                if (sqlite == null) sqlite = new ISQLite(context);
            }
        return sqlite;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_MEDIA_DATAS + "(" +
                T_MEDIA.ID + " integer primary key autoincrement," +
                T_MEDIA.SCENEID + "  integer ," +
                T_MEDIA.CONTENTNAME + "  text," +
                T_MEDIA.CONTENTPATH + "  text," +
                T_MEDIA.FLAG + "  integer DEFAULT 0" +

                ");");

        db.execSQL("create table " + TABLE_SCENE + "(" +
                T_SCENE.ID + " integer primary key autoincrement," +
                        T_SCENE.EVENTID +" text default '0',"+

                        T_SCENE.LOGINNAME +" text,"+


                        T_SCENE.FLAG +" integer DEFAULT 0,"+

                        T_SCENE.ADDTIME +" integer DEFAULT (strftime('%s','now')*1000),"+
                T_SCENE.TITLE + " text,"+

                        T_SCENE.DETAIL + " text,"+

                T_SCENE.REMARK + " text,"+

                T_SCENE.EQLEVELIDX + " integer,"+

                T_SCENE.SUMMARYIDX + " integer,"+
                        T_SCENE.LOC_LAT + " real,"+
                T_SCENE.LOC_LON + " real,"+
                T_SCENE.SUBMITTIME +" integer DEFAULT (strftime('%s','now')*1000)"+

                ");");
        db.execSQL("create table " + TABLE_LOCATIONINFO + "(" +
                "ID integer ," +
                "REGION_ID text," +
                "REGION_NAME text," +
                "PARENT_REGION_ID text," +

                "LON real," +
                "LAT real," +
                "LV text" +

                ");");
        String sql = "";
        try {
            sql = HttpUtils.changeInputStream(CommonUtils.context.getAssets().open("locationinfosql.txt"));

        }catch (Exception e){e.printStackTrace();}
        for(String str:sql.split("\n")){
            db.execSQL(str);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CommonUtils.log(oldVersion + "--------------" + newVersion);
        if(oldVersion>=newVersion) return ;
        db.execSQL("create table " + TABLE_LOCATIONINFO + "(" +
                "ID integer ," +
                "REGION_ID text," +
                "REGION_NAME text," +
                "PARENT_REGION_ID text," +

                "LON real," +
                "LAT real," +
                "LV text" +

                ");");
        String sql = "";
        try {
            sql = HttpUtils.changeInputStream(CommonUtils.context.getAssets().open("locationinfosql.txt"));
        }catch (Exception e){e.printStackTrace();}
        for(String str:sql.split("\n")){
            db.execSQL(str);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


    public  interface T_MEDIA {

        String ID = "_id",
                SCENEID = "scene_id",



        FLAG = "flag",


        CONTENTNAME = "content_name",

        CONTENTPATH = "content_path";



    }


    public  interface T_SCENE {

        String ID = "_id",
                EVENTID = "event_id",

        LOGINNAME = "loginname",


        FLAG = "flag",

        ADDTIME = "addtime",
                TITLE = "title",

        DETAIL = "detail",

        REMARK = "remark",

        EQLEVELIDX = "eqlevelidx",

        SUMMARYIDX = "summaryidx",
                LOC_LAT = "loc_lat",
                LOC_LON = "loc_lon",
                SUBMITTIME = "submittime";


    }


}
