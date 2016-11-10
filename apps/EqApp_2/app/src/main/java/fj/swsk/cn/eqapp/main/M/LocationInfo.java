package fj.swsk.cn.eqapp.main.M;

import android.database.Cursor;

/**
 * Created by apple on 16/7/27.
 */
public class LocationInfo {

    public int ID,LV;
    public String REGION_ID,REGION_NAME,PARENT_REGION_ID;
    public double LON,LAT;

    public LocationInfo(Cursor c) {
        int idx = 0;
        ID=c.getInt(idx++);
        REGION_ID=c.getString(idx++);
        REGION_NAME=c.getString(idx++);
        PARENT_REGION_ID=c.getString(idx++);
        LON = c.getDouble(idx++);
        LAT = c.getDouble(idx++);
        LV = c.getInt(idx++);
    }
}
