package fj.swsk.cn.eqapp.main.Common;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import fj.swsk.cn.eqapp.conf.CommonService;
import fj.swsk.cn.eqapp.conf.ISQLite;
import fj.swsk.cn.eqapp.main.M.LocationInfo;

/**
 * Created by apple on 16/7/27.
 */
public class LocationInfoService  extends CommonService {
    public static LocationInfoService ins=new LocationInfoService(ISQLite.TABLE_LOCATIONINFO);

    public LocationInfoService(String tableName) {
        super(tableName);
    }

    public List<LocationInfo> findByParent(LocationInfo parent){
        List<LocationInfo> items = new ArrayList<>();
        Cursor cursor = null;
        if(parent ==null){
            cursor =ins.queryBy("LV=1",new String[]{},"REGION_ID");

        }else{
            cursor =ins.queryBy(" PARENT_REGION_ID=? ",new String[]{parent.REGION_ID},"REGION_ID");

        }
        if (cursor.moveToFirst()) {

            do {
                int i = 0;
                LocationInfo item = new LocationInfo(cursor);


                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return items;
    }


}
