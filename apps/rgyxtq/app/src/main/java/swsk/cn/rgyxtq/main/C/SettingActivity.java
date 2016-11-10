package swsk.cn.rgyxtq.main.C;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.util.DialogUtil;

/**
 * Created by apple on 16/2/26.
 */
public class SettingActivity extends Activity {
    EditText mEditText=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Topbar mTopbar = (Topbar)findViewById(R.id.settings);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                DialogUtil.getDefault(SettingActivity.this, "提示", "确定把IP和端口修改为：" +
                        mEditText.getText().toString() + "?").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PushUtils.setServerIPText(SettingActivity.this, mEditText.getText().toString());
                        dialog.dismiss();
                        SettingActivity.this.finish();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

            }
        });


        Intent intent = getIntent();
        mTopbar.setTitleText(intent.getStringExtra("title"));
        mEditText=(EditText)findViewById(R.id.tv_fwdipV);
        mEditText.setText(PushUtils.getServerIPText(this));

    }
}
