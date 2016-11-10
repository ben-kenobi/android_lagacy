package fj.swsk.cn.eqapp.conf;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.mozillaonline.providers.downloads.DownloadService;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import fj.swsk.cn.eqapp.push.IUtil;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.collect.M.TsceneProg;
import fj.swsk.cn.eqapp.subs.setting.C.autoOfflineMapDownload;
import fj.swsk.cn.eqapp.util.CommonUtils;


/**
 * Created by apple on 16/2/25.
 */
public class MyApplication extends Application {

    public T_Media curEdit;
    public Tscene curscene;

    public Map<Long,TsceneProg> uploadingMap = new HashMap<>();

    public static boolean isStartDownloadService = false;
    public autoOfflineMapDownload task;
    public void onCreate() {
        if (IConstants.DEBUG){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                    detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }


        super.onCreate();
        CommonUtils.context=this;
        CommonService.context=this;

        IUtil.sApp=this;
        JPushInterface.setDebugMode(IConstants.DEBUG);
        JPushInterface.init(this);
        task = new autoOfflineMapDownload(this);
        if (isWifiDownload()) {
            startDownloadService();
            isStartDownloadService = true;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager connectivityManager =
                            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                    if (info != null && info.getTypeName().equals("WIFI")) {
                        if (info.isAvailable() && !MyApplication.isStartDownloadService
                                && isWifiDownload()) {
                            startDownloadService();
                            task.startDownloading();
                        }
                    } else {
                        stopDownloadService();
                        task.stopDownloading();
                    }
                }
            }
        }, filter);
    }
    private boolean isWifiDownload(){
        //是否设置了wifi自动更新
        SharedPreferences mySharedPreferences = getSharedPreferences("eqApp",
                Activity.MODE_PRIVATE);
        return mySharedPreferences.getBoolean("isWifiDownload", false);
    }
    /**
     * 启动下载服务
     */
    public void startDownloadService() {
        Intent intent = new Intent();
        intent.setClass(this, DownloadService.class);
        startService(intent);
        MyApplication.isStartDownloadService = true;
    }
    public void stopDownloadService(){
        Intent intent = new Intent();
        intent.setClass(this, DownloadService.class);
        stopService(intent);
    }


    public void onTerminate() {
        CommonUtils.context=null;
        CommonService.context=null;

        super.onTerminate();
    }
}
