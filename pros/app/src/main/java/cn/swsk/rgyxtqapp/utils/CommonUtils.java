package cn.swsk.rgyxtqapp.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by apple on 16/2/18.
 */
public class CommonUtils {
    public static final ExecutorService THREAD_POOL= Executors.newFixedThreadPool(3);
    public static final Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());

    public static String fullpath(String url,Context con){
        return  "http://" + PushUtils.getServerIPText(con) + url;
    }
    public static void log(String str){
        Log.e("itag",str);
    }
    public static void toast(String message,Context con) {
        Toast.makeText(con, message, Toast.LENGTH_SHORT).show();
    }
    public static void toastOnMainHandler(final String message,final Context con){
        MAIN_HANDLER.post(new Runnable() {
            public void run() {
                toast(message,con);
            }
        });
    }
}
