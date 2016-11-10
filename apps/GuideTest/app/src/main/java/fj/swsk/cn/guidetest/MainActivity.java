package fj.swsk.cn.guidetest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import fj.swsk.cn.guidetest.util.CommonUtils;
import fj.swsk.cn.guidetest.util.IConstants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int code = 22;
    private TestSer ser;
    private Messenger msg;
    private MyAIDL binder;
    int flag = 2;
    private ServiceConnection sc=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (flag == 0)
                ser = ((TestSer.LocalBinder) service).getSer();
            else if(flag ==1) {
                msg = new Messenger(service);
            }else if(flag ==2){
                binder = MyAIDL.Stub.asInterface(service);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommonUtils.log("disconnected");

        }
    };
    private boolean bind =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getFragmentManager().beginTransaction().add(android.R.id.content,new TestFrag(),"1").commit();


        IConstants.MAIN_HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                startService(new Intent(MainActivity.this, TestSer.class));
            }
        }, 3000);
         bindSer();

    }
    private void bindSer(){
//        Intent intent = new Intent(MainActivity.this, TestSer.class);
        Intent intent = new Intent("fj.swsk.cn.guidetest.TEST");
        intent.setPackage("fj.swsk.cn.guidetest");

        intent.putExtra("flag", flag);

        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onClick(View v) {
        if (flag == 0)
            ser.print(++code + "===");
        else if(flag ==1){
            try {

                msg.send(Message.obtain(null, 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(flag ==2){
            try {
                CommonUtils.log(binder.getPid() + "========"+binder.getPid());
            }catch (Exception e){e.printStackTrace();}
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}


