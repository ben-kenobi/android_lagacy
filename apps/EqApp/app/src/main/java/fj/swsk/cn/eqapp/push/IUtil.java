package fj.swsk.cn.eqapp.push;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by apple on 16/3/30.
 */
public class IUtil {
    public static Application sApp;
    public static final String PREFS_NAME = "JPUSH_EXAMPLE";
    public static final String PREFS_DAYS = "JPUSH_EXAMPLE_DAYS";
    public static final String PREFS_START_TIME = "PREFS_START_TIME";
    public static final String PREFS_END_TIME = "PREFS_END_TIME";
    public static final String KEY_APP_KEY = "JPUSH_APPKEY";
    public static final Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");


    public static void log(String log){
        Log.e("itag",log);
    }

    public static boolean isEmpty(String s){
        return null==s || s.trim().length()==0;
    }
    public static boolean isValidTagNAlias(String s){
        return p.matcher(s).matches();
    }
    public static String metaData(String key){
        String val="";
        try {
            ApplicationInfo ai = sApp.getPackageManager().getApplicationInfo(sApp.getPackageName(), PackageManager.GET_META_DATA);
            if(ai!=null) {
                Bundle metadata = ai.metaData;
                if(null!=metadata){
                    val = metadata.getString(key);
                }
            }

        }catch (Exception e){}
        return val;
    }

    public static String getAppKey(){
        String key = metaData(KEY_APP_KEY);
        if(null==key || key.length()!=24)
            return null;
        return key;
    }
    public static String getVersion(){
        try {
            return sApp.getPackageManager().getPackageInfo(sApp.getPackageName(),0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getVersionCode(){
        try {
            return sApp.getPackageManager().getPackageInfo(sApp.getPackageName(),0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public static String getDeviceId(){
        return JPushInterface.getUdid(sApp);
    }

    public static void asynToast(final String mes){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(sApp,mes,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public static boolean isConnected(){
        ConnectivityManager cm = (ConnectivityManager)sApp.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info!=null && info.isConnected();
    }

    public static String getImei(String imei){
        try {
            TelephonyManager tm = (TelephonyManager) sApp.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        }catch (Exception e){
           log(e.getMessage());
        }
        return imei;
    }

    public static  void unlockScr(){
        KeyguardManager km = (KeyguardManager) sApp.getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km .newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) sApp.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire();
        wakeLock.release();
    }

    public static void callIntent(String num){
        Intent call = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + num));
        call.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_NEW_TASK);
        sApp.startActivity(call);

    }


}
