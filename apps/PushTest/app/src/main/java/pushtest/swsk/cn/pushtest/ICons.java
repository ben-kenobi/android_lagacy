package pushtest.swsk.cn.pushtest;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by apple on 16/3/30.
 */
public interface ICons {
    public static final ExecutorService THREAD_POOL= Executors.newFixedThreadPool(3);
    public static final Handler MAIN_HANDLER=new Handler(Looper.getMainLooper());

    boolean DEBUG = true;

}
