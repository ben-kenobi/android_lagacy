package fj.swsk.cn.eqapp.subs.collect.M;


import android.content.ContentValues;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.collect.Common.MediaService;
import fj.swsk.cn.eqapp.subs.collect.Common.SceneService;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.SharePrefUtil;


/**
 * Created by apple on 16/7/11.
 */
public class Tscene {

    public long _id;
    public String event_id= SharePrefUtil.getObsTime()+"", loginname = PushUtils.getLoginName(), title, detail, remark;
    public int flag, eqlevelidx, summaryidx;
    public Date addtime = new Date(), submittime = new Date();
    public double loc_lat, loc_lon;

    public List<File> list = new ArrayList<>();

    public void insertOrUpdate() {
        ContentValues cv = new ContentValues();

        cv.put(ISQLite.T_SCENE.EVENTID, event_id);
        cv.put(ISQLite.T_SCENE.LOGINNAME, loginname);

        cv.put(ISQLite.T_SCENE.TITLE, title);
        cv.put(ISQLite.T_SCENE.DETAIL, detail);
        cv.put(ISQLite.T_SCENE.REMARK, remark);
        cv.put(ISQLite.T_SCENE.FLAG, flag);
        cv.put(ISQLite.T_SCENE.EQLEVELIDX, eqlevelidx);
        cv.put(ISQLite.T_SCENE.SUMMARYIDX, summaryidx);
        cv.put(ISQLite.T_SCENE.ADDTIME, addtime.getTime());
        cv.put(ISQLite.T_SCENE.SUBMITTIME, submittime.getTime());
        cv.put(ISQLite.T_SCENE.LOC_LAT, loc_lat);
        cv.put(ISQLite.T_SCENE.LOC_LON, loc_lon);

        if (_id == 0) {
            _id = SceneService.ins.insert(cv);
            for (File file : list) {
                ContentValues mcv = new ContentValues();
                mcv.put(ISQLite.T_MEDIA.CONTENTNAME, file.getName());
                mcv.put(ISQLite.T_MEDIA.CONTENTPATH, file.getAbsolutePath());
                mcv.put(ISQLite.T_MEDIA.SCENEID, _id);
                MediaService.ins.insert(mcv);
            }
        } else {
            SceneService.ins.update(cv, _id);
            MediaService.ins.deleteBySid(_id);
            for (File file : list) {
                ContentValues mcv = new ContentValues();
                mcv.put(ISQLite.T_MEDIA.CONTENTNAME, file.getName());
                mcv.put(ISQLite.T_MEDIA.CONTENTPATH, file.getAbsolutePath());
                mcv.put(ISQLite.T_MEDIA.SCENEID, _id);
                MediaService.ins.insert(mcv);
            }
        }


    }

    public void uploaded(boolean usesrc) {
        ContentValues cv = new ContentValues();
        cv.put(ISQLite.T_SCENE.FLAG, 1);
        cv.put(ISQLite.T_SCENE.SUBMITTIME, System.currentTimeMillis());
        cv.put(ISQLite.T_SCENE.REMARK,usesrc?"1":"0");
        SceneService.ins.update(cv, _id);
        flag = 1;


    }

    public void delete() {

        SceneService.ins.delete(_id);
        MediaService.ins.deleteBySid(_id);
        String path = CommonUtils.context.getFilesDir().getAbsolutePath();
        for (File f : list) {

            if (f.getAbsolutePath().startsWith(path))
                f.delete();
                File f2= new File(CommonUtils.context.getFilesDir(), IConstants.SMALLFILEPREFIX
                +f.getName());
                if(f.exists())
                    f2.delete();
        }
    }


}
