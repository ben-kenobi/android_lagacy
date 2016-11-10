package fj.swsk.cn.guidetest;

import android.app.Application;

import fj.swsk.cn.guidetest.util.CommonUtils;

/**
 * Created by apple on 16/7/14.
 */
public class MyApplication  extends Application {

    public void onCreate() {


        super.onCreate();
        CommonUtils.context=this;

    }

    public void onTerminate() {
        CommonUtils.context=null;

        super.onTerminate();
    }
}
