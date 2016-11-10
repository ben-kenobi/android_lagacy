package test.swsk.cn.mapboxtest;

import android.content.Context;

/**
 * Created by apple on 16/4/7.
 */
public class ApiAccess {

    public static String accssToken(Context context){
        return context.getResources().getString(R.string.APPTOKEN);

    }
}
