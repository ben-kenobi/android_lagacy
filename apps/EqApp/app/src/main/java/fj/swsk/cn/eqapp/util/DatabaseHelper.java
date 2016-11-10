package fj.swsk.cn.eqapp.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xul on 2016/8/4.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "eqdatabase";
    public final static int DATABASE_VERSION = 1;
    public final static String CONTACT = "contact";
    public final static String DOWNLOADINFO = "download_info";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建联系人
        db.execSQL("create table " + CONTACT + " ("
                + "[id] INTEGER  NOT NULL PRIMARY KEY,"
                + "[serverId] VARCHAR(20) NULL,"//服务器端唯一标识
                + "[jobPost] VARCHAR(20)  NULL,"//岗位
                + "[sex] INTEGER  NULL,"//性别 1=男 2=女
                + "[co1ID] VARCHAR(20)  NULL,"//所属市行政区编码
                + "[co1] VARCHAR(20)  NULL,"//所属市
                + "[co2ID] VARCHAR(20)  NULL,"//所属区县行政编码
                + "[co2] VARCHAR(20)  NULL,"//所属区县
                + "[department] VARCHAR(20)  NULL,"//所属部门
                + "[email] VARCHAR(50)  NULL,"//email
                + "[name] VARCHAR(50)  NOT NULL,"
                + "[phone] VARCHAR(11) NOT NULL,"//手机
                + "[createDate] VARCHAR(20)  NULL,"
                + "[remark] VARCHAR(200)  NULL,"
                + "[uploaded] INTEGER NOT NULL,"//是否上传服务器 1未上传插入 0已上传 2 未上传更新
                + "[sortKey] VARCHAR(1)  NOT NULL)");
        //创建下载信息表
        db.execSQL("CREATE TABLE " + DOWNLOADINFO + " (" +
                "[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "[current_bytes] REAL  NOT NULL," +
                "[total_bytes] REAL  NOT NULL," +
                "[status] INTEGER  NOT NULL," +
                "[download_id] REAL  NOT NULL," +
                "[progress] INTEGER  NOT NULL," +
                "[name] VARCHAR(50)  NOT NULL," +
                "[version] FLOAT  NULL," +
                "[url] VARCHAR(50)  NOT NULL" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
