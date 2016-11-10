package fj.swsk.cn.guidetest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import fj.swsk.cn.guidetest.util.CommonUtils;

/**
 * Created by apple on 16/7/18.
 */
public class TestSer extends Service  {
    private IBinder mBinder = new LocalBinder();

    private  MyAIDL.Stub binder = new MyAIDL.Stub() {
        @Override
        public int getPid() throws RemoteException {
            CommonUtils.log("eeeeeeeee");
            return 11;
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }
    };



    private Handler h = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                print(msg.toString());
            }
        }
    };
    private Messenger msg = new Messenger(h);

    public class LocalBinder extends Binder{
        public TestSer getSer(){
            return TestSer.this;
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        int flag = intent.getIntExtra("flag",0);
        CommonUtils.log("onbind");
        if(flag ==0){
            return mBinder;
        }else if(flag ==1){
            return msg.getBinder();

        }else if(flag ==2){
            return binder;
        }
        return null;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        CommonUtils.log("onrebind");

    }

    public void print(String str){

        CommonUtils.log(str);
    }

}
