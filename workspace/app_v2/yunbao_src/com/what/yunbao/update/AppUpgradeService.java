package com.what.yunbao.update;

import java.io.File;
import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.icanit.app_v2.common.IConstants;
import com.what.yunbao.R;
import com.what.yunbao.util.CommonUtil;


public class AppUpgradeService extends Service{
	private static final String TAG = "AppUpgradeService";
//    public static final int APP_VERSION_LATEST = 0;
//    public static final int APP_VERSION_OLDER = 1;

    public static final int mNotificationId = 100;
    private String mDownloadUrl = null;
    private String mAppName = null;
    private NotificationManager mNotificationManager = null;
    private Notification mNotification = null;
    private PendingIntent mPendingIntent = null;

    private File destDir = null;
    private File destFile = null;

    private static final int DOWNLOAD_FAIL = -1;
    private static final int DOWNLOAD_SUCCESS = 0;
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {           	
            case DOWNLOAD_SUCCESS:
                Toast.makeText(getApplicationContext(),"下载完成", Toast.LENGTH_LONG).show();
//                install(destFile);
                break;
            case DOWNLOAD_FAIL:
                Toast.makeText(getApplicationContext(), "下载失败", Toast.LENGTH_LONG).show();
                mNotificationManager.cancel(mNotificationId);
                break;
            default:
                break; 
            }
        }   
    };
    private DownloadUtils.DownloadListener downloadListener = new DownloadUtils.DownloadListener() {
        @Override
        public void downloading(int progress) {
            mNotification.contentView.setProgressBar(R.id.app_upgrade_progressbar, 100, progress, false);
            mNotification.contentView.setTextViewText(R.id.app_upgrade_progresstext, progress + "%");
            mNotificationManager.notify(mNotificationId, mNotification);
        }
        
        @Override
        public void downloaded() {
            Uri uri = Uri.fromFile(destFile);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(uri,"application/vnd.android.package-archive");
			mNotification.defaults = Notification.DEFAULT_VIBRATE;
			mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, 0, intent, 0);
			mNotification.setLatestEventInfo(AppUpgradeService.this,getResources().getString(R.string.app_name), "下载成功，点击安装", mPendingIntent);
			mNotificationManager.notify(mNotificationId, mNotification);      
            if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_SUCCESS;
                mHandler.sendMessage(msg);
            }
//            mNotificationManager.cancel(mNotificationId);
        } 
    };
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
    	Log.e(TAG, "startCommand");
        mDownloadUrl = intent.getStringExtra("downloadUrl");
        mAppName = intent.getStringExtra("app_name");
//        destDir = new File(Environment.getExternalStorageDirectory().getPath() + BaseApplication.mDownloadPath);
        destDir = new File(CommonUtil.getRootFilePath(this) +IConstants.FILE_PATH);
        //注意添加权限
        if(destDir.exists()|| destDir.mkdirs()){
            destFile = new File(destDir.getPath()+"/"+mAppName+".apk");
            if(destFile.exists()&&destFile.isFile()&&checkApkFile(destFile.getPath())){
            	stopSelf();
            	install(destFile);
            	return super.onStartCommand(intent, flags, startId);
            }
        }
        mNotificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotification = new Notification();
        mNotification.contentView = new RemoteViews(getApplication().getPackageName(), R.layout.app_upgrade_notification);

        Intent completingIntent = new Intent();
        completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        completingIntent.setClass(getApplication().getApplicationContext(), AppUpgradeService.class);
        mPendingIntent = PendingIntent.getActivity(AppUpgradeService.this, R.string.app_name, completingIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
        mNotification.icon = R.drawable.ic_launcher;
        mNotification.tickerText = "开始下载";
        mNotification.contentIntent = mPendingIntent;
        mNotification.contentView.setTextViewText(R.id.app_upgrade_progresstext, "0%");        
        mNotification.contentView.setProgressBar(R.id.app_upgrade_progressbar, 100, 0, false);
        
        Date date = new Date();
        String time = date.getHours()+":"+date.getMinutes();
        mNotification.contentView.setTextViewText(R.id.app_upgrade_timetext, time);
        mNotificationManager.cancel(mNotificationId);
        mNotificationManager.notify(mNotificationId, mNotification);
        
        new AppUpgradeThread().start();
        
        return super.onStartCommand(intent, flags, startId);
    }

    class AppUpgradeThread extends Thread{

        @Override
        public void run() {  
        	
            try {
                DownloadUtils.download(mDownloadUrl, destFile, downloadListener);
            } catch (Exception e) { 
                Message msg = mHandler.obtainMessage();
                msg.what = DOWNLOAD_FAIL;
                mHandler.sendMessage(msg);
                e.printStackTrace();
            }finally{
            	 stopSelf();
            }          
        }
    }

    public boolean checkApkFile(String apkFilePath) {
        boolean result = false;
        try{
            PackageManager pManager = getPackageManager(       );
            PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
            if (pInfo == null) {
                result =  false;
            } else {
                result =  true;
            }
        } catch(Exception e) {
            result =  false;
            e.printStackTrace();
        }
        return result;
    }

    public void install(File apkFile){
        Uri uri = Uri.fromFile(apkFile);
//    	Uri uri = Uri.parse("file://" + "/storage/sdcard0/Android/data/com.icanit.application/file/社区便利.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);

    }
}
