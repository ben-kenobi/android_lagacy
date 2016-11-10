package swsk.cn.rgyxtq.conf;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public interface IConstants {
     ExecutorService THREAD_POOL= Executors.newFixedThreadPool(3);
     Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());

    String ID="_id";
    String PLATFORM_ENCODING="UTF-8";

    Boolean DEBUG = false;

    String TIME_PATTERN="yyyy-MM-dd  HH:mm:ss";
    String DATE_PATTERN="yyyy-MM-dd";

    String defServerIP="192.168.1.129:8080";
//    String getDefServerIP="61.154.9.242:5551";



}
