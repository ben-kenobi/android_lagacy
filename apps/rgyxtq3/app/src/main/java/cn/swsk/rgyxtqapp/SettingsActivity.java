package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.PushUtils;
import cn.swsk.rgyxtqapp.utils.UpdateManager;

/**
 * Created by tom on 2015/10/18.
 */
public class SettingsActivity extends Activity {
    EditText editText = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        topbar mTopbar = (topbar) findViewById(R.id.settings);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

               /* new AlertDialog.Builder(SettingsActivity.this).setTitle("单选框").setSingleChoiceItems(
                        new String[]{"Item1", "Item2"}, 0,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", null).show();*/


                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("确定把IP和端口修改为：" + editText.getText().toString() + "?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PushUtils.setServerIPText(SettingsActivity.this, editText.getText().toString());
                        dialog.dismiss();
                        SettingsActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });
        Intent intent = getIntent();
        mTopbar.setTitleText(intent.getStringExtra("title"));

        editText = (EditText) findViewById(R.id.tv_fwdipV);
        String ipStr = PushUtils.getServerIPText(this);
        if("".equals(ipStr)){
            ipStr = "61.154.9.242:5551";
            PushUtils.setServerIPText(SettingsActivity.this, ipStr);
            editText.setText(ipStr);
        }else{
            editText.setText(ipStr);
        }




        //退出
        Button button = (Button) findViewById(R.id.btn_Quite);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //退出后台线程,以及销毁静态变量
                int nPid = android.os.Process.myPid();
                android.os.Process.killProcess(nPid);
            }
        });

       /* Button buttonUpdate= (Button) findViewById(R.id.btn_update);
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateManager manager = new UpdateManager(SettingsActivity.this);
                // 检查软件更新
                manager.checkUpdate();
            }
        });*/
    }
}
