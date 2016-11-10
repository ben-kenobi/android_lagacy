package fj.swsk.cn.eqapp.subs.collect.Common;

import android.database.Cursor;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.conf.CommonService;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;

/**
 * Created by apple on 16/6/24.
 */
public class SceneService extends CommonService {
    public static SceneService ins = new SceneService(ISQLite.TABLE_SCENE);

    public SceneService(String tableName) {
        super(tableName);
    }

    public List<Tscene> findAllBy(String username,String flag) {
        List<Tscene> items = new ArrayList<Tscene>();
        Cursor cursor=null;
        // 如果flag=1 即查找历史数据，若当前obstime不是最新地震的obstime，则只查找当前obstime地震的历史数据
        // 若是最新地震，则展示全部历史数据
        if("1".equals(flag)&&EQInfo.getIns()!=null&& !EQInfo.getIns().isCurrentEQ()){
            cursor=SceneService.ins.queryBy(ISQLite.T_SCENE.LOGINNAME + "=? and " +
                            ISQLite.T_SCENE.FLAG + "=? and "+ISQLite.T_SCENE.EVENTID+"=? ",
                    new String[]{username, flag,EQInfo.getIns().obsTime+""}, ISQLite.T_MEDIA.ID  + "  desc ");
        }else {
            cursor = SceneService.ins.queryBy(ISQLite.T_SCENE.LOGINNAME + "=? and " +
                            ISQLite.T_SCENE.FLAG + "=?",
                    new String[]{username, flag}, ISQLite.T_MEDIA.ID + "  desc ");
        }
        if (cursor.moveToFirst()) {

            do {

                int i = 0;
                Tscene item = new Tscene();
                item._id = cursor.getInt(i++);
                item.event_id = cursor.getString(i++);
                item.loginname = cursor.getString(i++);
                item.flag = cursor.getInt(i++);
                item.addtime = new Date(cursor.getLong(i++));
                item.title = cursor.getString(i++);
                item.detail = cursor.getString(i++);
                item.remark = cursor.getString(i++);
                item.eqlevelidx = cursor.getInt(i++);
                item.summaryidx = cursor.getInt(i++);
                item.loc_lat = cursor.getDouble(i++);
                item.loc_lon = cursor.getDouble(i++);
                item.submittime =new Date( cursor.getLong(i++));
                item.list = MediaService.ins.findAllFIileBy("0",item._id);
               
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }
}
