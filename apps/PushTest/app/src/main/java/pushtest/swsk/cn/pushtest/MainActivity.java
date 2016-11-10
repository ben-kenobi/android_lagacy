package pushtest.swsk.cn.pushtest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import fj.swsk.cn.eqapp.R;

public class MainActivity extends InstrumentedActivity implements View.OnClickListener{

    private EditText mMsg;
    public static boolean isForeground=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R
                .layout.main);
        initUI();
        registerMesRec();

    }

    private void initUI(){
        ((TextView)findViewById(R.id.tv_imei)).setText("IMEI: "+IUtil.getImei(""));
        ((TextView)findViewById(R.id.tv_appkey)).setText("AppKey: "+IUtil.getAppKey());
        ((TextView)findViewById(R.id.tv_package)).setText("PackageName: "+getPackageName());
        ((TextView)findViewById(R.id.tv_device_id)).setText("DeviceId: "+IUtil.getDeviceId());
        ((TextView)findViewById(R.id.tv_version)).setText("Version: "+IUtil.getVersion());

        mMsg=(EditText)findViewById(R.id.msg_rec);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.init){
            JPushInterface.init(getApplicationContext());
        }else if(v.getId()==R.id.setting){
            Intent intent = new Intent(MainActivity.this,PushSetActivity.class);
            startActivity(intent);
        }else if(v.getId()==R.id.stopPush){
            JPushInterface.stopPush(getApplicationContext());

        }else if(v.getId()==R.id.resumePush){
            JPushInterface.resumePush(getApplicationContext());
        }
    }

    @Override
    protected void onResume() {
        isForeground=true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground=false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    private MessageReceiver mMessageReceiver;
    public static final String  MESSAGE_RECEIVED_ACTION="MSG_REC_ACTION";
    public static final String MESSAGE="message",EXTRAS="extras",TITLE="title";

    private void registerMesRec(){
        mMessageReceiver=new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver,filter);
    }


    public class MessageReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(MESSAGE_RECEIVED_ACTION.equals(intent.getAction())){
                mMsg.setText("message: "+intent.getStringExtra(MESSAGE)+"\n"+"extras: "+
                intent.getStringExtra(EXTRAS)+"\n");
            }
        }
    }

}
