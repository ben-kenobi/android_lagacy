package cn.swsk.rgyxtqapp.contants;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by tom on 2015/10/17.
 */
public class Configure {
    public static boolean isMove=false;
    public static boolean isChangingPage=false;
    public static boolean isDelDark = false;
    public static int curentPage=0;
    public static int countPages=0;
    public static int removeItem=0;
    public static int screenHeight=0;
    public static int screenWidth=0;
    public static float screenDensity=0;

    public static void init(Activity context) {
        if(screenDensity==0||screenWidth==0||screenHeight==0){
            DisplayMetrics dm = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(dm);
            Configure.screenDensity = dm.density;
            Configure.screenHeight = dm.heightPixels;
            Configure.screenWidth = dm.widthPixels;
        }
    }

}
