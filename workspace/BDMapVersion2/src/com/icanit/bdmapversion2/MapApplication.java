package com.icanit.bdmapversion2;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;

public class MapApplication extends Application {
	
    private static MapApplication mInstance = null;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String strKey = "B50390F03032E76014B30A719ED95C6E7641FCD6";
    
    //包名
    public static String mPckName;
    
    //版本
    public static String mAppName;

    public static String mDownloadPath;
    public static int mVersionCode;
    public static String mVersionName;
    public static boolean mShowUpdate = true;
	
	@Override
    public void onCreate() {
	    super.onCreate();
	    //地图
		mInstance = this;
		initEngineManager(this);
		//版本
		initLocalVersion();
		//包名
		getAppPackageName();
	}
	
	@Override
	//建议在您app的退出之前调用mapadpi的destroy()函数，避免重复初始化带来的时间消耗
	public void onTerminate() {
	    if (mBMapManager != null) {
            mBMapManager.destroy();
            mBMapManager = null;
        }
		super.onTerminate();
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(MapApplication.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static MapApplication getInstance() {
		return mInstance;
	}
	
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public static class MyGeneralListener implements MKGeneralListener {
          
        @Override
        public void onGetNetworkState(int iError) {       
        }

        @Override
        public void onGetPermissionState(int iError) {

        }
    }
    
   //版本
    public void initLocalVersion(){
        PackageInfo pinfo;
        try {
            pinfo = this.getPackageManager().getPackageInfo(this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            mVersionCode = pinfo.versionCode;
            mVersionName = pinfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }
   //包名
    
    public void getAppPackageName(){
    	mPckName = this.getPackageName();
    }

}