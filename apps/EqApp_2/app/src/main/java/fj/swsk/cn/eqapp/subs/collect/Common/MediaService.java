package fj.swsk.cn.eqapp.subs.collect.Common;

import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.conf.CommonService;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;

/**
 * Created by apple on 16/6/24.
 */
public class MediaService extends CommonService {
    public static MediaService ins = new MediaService(ISQLite.TABLE_MEDIA_DATAS);

    public MediaService(String tableName) {
        super(tableName);
    }

    public List<T_Media> findAllBy(String flag,long sid) {
        List<T_Media> items = new ArrayList<T_Media>();
        Cursor cursor=MediaService.ins.queryBy(ISQLite.T_MEDIA.SCENEID + "=? and " +
                        ISQLite.T_MEDIA.FLAG + "=?",
                new String[]{sid+"", flag}, ISQLite.T_MEDIA.ID  + "  ");
        if (cursor.moveToFirst()) {

            do {
                int i = 0;
                T_Media item = new T_Media();
                item._id = cursor.getInt(i++);
                item.sceneid = cursor.getInt(i++);
                item.content_name = cursor.getString(i++);
                item.content_path = cursor.getString(i++);
                item.flag = cursor.getInt(i++);

                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }



    public List<File> findAllFIileBy(String flag,long sid) {
        List<T_Media> items = findAllBy(flag,sid);
        List<File> files = new ArrayList<>();
        for(T_Media me:items){

            files.add(new File(me.content_path));
        }
        return files;

    }


    public int deleteBySid(long sid){
        return db.delete(tableName, ISQLite.T_MEDIA.SCENEID+"="+sid,new String[]{});

    }
}
