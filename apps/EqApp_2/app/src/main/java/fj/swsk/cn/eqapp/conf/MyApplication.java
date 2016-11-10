package fj.swsk.cn.eqapp.conf;

import android.app.Application;
import android.os.StrictMode;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import fj.swsk.cn.eqapp.push.IUtil;
import fj.swsk.cn.eqapp.subs.collect.M.T_Media;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.collect.M.TsceneProg;
import fj.swsk.cn.eqapp.util.CommonUtils;


/**
 * Created by apple on 16/2/25.
 */
public class MyApplication extends Application {

    public T_Media curEdit;
    public Tscene curscene;

    public Map<Long,TsceneProg> uploadingMap = new HashMap<>();


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

    }

    public void onTerminate() {
        CommonUtils.context=null;
        CommonService.context=null;

        super.onTerminate();
    }
}
