package fj.swsk.cn.guidetest2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import fj.swsk.cn.guidetest.MyAIDL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    MyAIDL binder;
    private ServiceConnection sc=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

                binder = MyAIDL.Stub.asInterface(service);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(View v) {
        try {
            Log.e("iiitag", binder.getPid() + "================");
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(sc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent("fj.swsk.cn.guidetest.TEST");
        intent.setPackage("fj.swsk.cn.guidetest");

        intent.putExtra("flag", 2);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }
}
