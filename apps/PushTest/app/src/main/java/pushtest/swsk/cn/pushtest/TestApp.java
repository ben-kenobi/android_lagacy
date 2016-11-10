package pushtest.swsk.cn.pushtest;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by apple on 16/3/30.
 */
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IUtil.sApp=this;
        JPushInterface.setDebugMode(ICons.DEBUG);
        JPushInterface.init(this);

    }
}
