package cn.swsk.rgyxtqapp.utils;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.net.URLEncoder;
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
                toast(message, con);
            }
        });
    }
    public static String UrlEnc(String str){
        try{
            return URLEncoder.encode(str,"UTF-8");
        }catch (Exception e){}
        return "";
    }
    public static void measure(View v){
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View
                .MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    public static void measure2(View v){
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST), View
                .MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST));
    }
    public static void measure3(View v){
        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY), View
                .MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
    }

    public static  void showSoftInput(final View v,final Context context){
        IConstants.MAIN_HANDLER.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.showSoftInput(v, 0, null);
            }
        }, 300);
    }

    public static void hideSoftInput(View v,Context context){
        InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0, null);
    }
    public static int getId(Context context,String name,String type){
        return context.getResources().getIdentifier
                (name, type, context.getPackageName()) ;
    }


    public static Point getScreenSize(Context context) {
        Point point = new Point();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getSize(point);
        return point;
    }
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm;
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
    public static int getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(dm);
        return (int)dm.density;
    }

    public static String null2zero(Object obj){
        if(obj==null || "null".equalsIgnoreCase(obj.toString())){
            return "0";
        }
        return obj.toString();
    }

    public static String null2blank(Object obj){
        if(obj==null || "null".equalsIgnoreCase(obj.toString())){
            return "";
        }
        return obj.toString();
    }

    public static  void unlockScr(Context context){
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();
    }

    public static void callIntent(String num,Context context){
        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + num));
        call.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(call);

    }

}
