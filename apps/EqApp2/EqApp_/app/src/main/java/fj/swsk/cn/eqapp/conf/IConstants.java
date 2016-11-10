package fj.swsk.cn.eqapp.conf;

import android.os.Handler;
import android.os.Looper;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public interface IConstants {
     ExecutorService THREAD_POOL= Executors.newFixedThreadPool(3);
     Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());

    String ID="_id";
    String PLATFORM_ENCODING="UTF-8";

    Boolean DEBUG = false;
    String TIME_PATTERN="yyyy-MM-dd  HH:mm:ss";
    SimpleDateFormat TIMESDF = new SimpleDateFormat(TIME_PATTERN);
    String DATE_PATTERN="yyyy-MM-dd";
    SimpleDateFormat DATESDF = new SimpleDateFormat(DATE_PATTERN);

    String defServerIP="3dspace.xicp.net:20080";


//    String getDefServerIP="61.154.9.242:5551";




    String loginUrl="rgyx/appUser/login";
    String poiServiceUrl = "http://www.fjmap.net/fjmapsvc/api/POI/search/";
    String uploadUrl="fjseism/ma/sitecollection/dataSub";


}
