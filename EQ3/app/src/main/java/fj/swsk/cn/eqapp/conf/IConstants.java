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
    String SMALLFILEPREFIX="___";

    String TIME_PATTERN="yyyy-MM-dd  HH:mm:ss";
    SimpleDateFormat TIMESDF = new SimpleDateFormat(TIME_PATTERN);
    String DATE_PATTERN="yyyy-MM-dd";
    SimpleDateFormat DATESDF = new SimpleDateFormat(DATE_PATTERN);

    String defServerIP="3dspace.xicp.net:20080";


//    String getDefServerIP="61.154.9.242:5551";



    String poiServiceUrl = "http://www.fjmap.net/fjmapsvc/api/POI/search/";
    String loginUrl="fjseism/ma/user/login";
    String uploadUrl="fjseism/ma/sitecollection/dataSub";
    String upPwd="fjseism/ma/user/upPwd";
    String retrievePwd="fjseism/ma/user/resetPwd";
    String upUserInfo="fjseism/ma/user/upUserInfo";
    String eqHisUrl="fjseism/ha/sr/history";
    String lifelineLayerInfoUrl = "http://192.168.200.125:8080/fjseism/ma/thematicmap/lifelineLayerInfoList";

//    String pushMapServicePattern="http://3dspace.xicp.net:20081/geoserver/fjseism/wms" ;
            //"?service=WMS&version=1.1.0&request=GetMap&layers=fjseism:%s&styles=fjseism_%s&bbox=116.835177,23.55163,120.135177,27.84163&width=590&height=768&srs=EPSG:4326&format=application/openlayers";





}
