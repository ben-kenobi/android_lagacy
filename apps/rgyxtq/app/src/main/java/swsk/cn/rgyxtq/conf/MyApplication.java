package swsk.cn.rgyxtq.conf;

import android.app.Application;
import android.os.StrictMode;

import swsk.cn.rgyxtq.util.CommonUtils;

/**
 * Created by apple on 16/2/25.
 */
public class MyApplication extends Application {
    public void onCreate() {
        if (IConstants.DEBUG){
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().
                    detectDiskWrites().detectNetwork().penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
        }


        super.onCreate();
        CommonUtils.context=this;
    }

    public void onTerminate() {
        CommonUtils.context=null;
        super.onTerminate();
    }
}
