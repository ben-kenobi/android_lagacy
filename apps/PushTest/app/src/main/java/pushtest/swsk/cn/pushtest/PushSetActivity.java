package pushtest.swsk.cn.pushtest;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import fj.swsk.cn.eqapp.R;

/**
 * Created by apple on 16/3/30.
 */
public class PushSetActivity extends InstrumentedActivity implements View.OnClickListener {

    private EditText tag, alias;

    @Override
    public void onCreate(Bundle arguments) {
        super.onCreate(arguments);
        setContentView(R.layout.push_set_dialog);
        tag = (EditText) findViewById(R.id.et_tag);
        alias = (EditText) findViewById(R.id.et_alias);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_tag) {
            setTag();
        } else if (id == R.id.bt_alias) {
            setAlias();
        } else if (id == R.id.setStyle1) {
            setStyleBasic();
        } else if (id == R.id.setStyle2) {
            setStyleCustom();
        } else if (id == R.id.bu_setTime) {
            Intent intent = new Intent(PushSetActivity.this, SettingActivity.class);
            startActivity(intent);
        }

    }


    private void setTag() {
        String tagstr = tag.getText().toString().trim();
        // 检查 tag 的有效性
        if (TextUtils.isEmpty(tagstr)) {
            Toast.makeText(PushSetActivity.this, R.string.error_tag_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        // ","隔开的多个 转换成 Set
        String[] sArray = tagstr.split(",");
        Set<String> tagSet = new LinkedHashSet<String>();
        for (String sTagItme : sArray) {
            if (!IUtil.isValidTagNAlias(sTagItme)) {
                Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
                return;
            }
            tagSet.add(sTagItme);
        }

        //调用JPush API设置Tag
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_TAGS, tagSet));
    }


    private void setAlias() {
        String aliastv = alias.getText().toString().trim();
        if (TextUtils.isEmpty(aliastv)) {
            Toast.makeText(PushSetActivity.this, R.string.error_alias_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!IUtil.isValidTagNAlias(aliastv)) {
            Toast.makeText(PushSetActivity.this, R.string.error_tag_gs_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        //调用JPush API设置Alias
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
    }

    /**
     * 设置通知提示方式 - 基础属性
     */
    private void setStyleBasic() {
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(PushSetActivity.this);
        builder.statusBarDrawable = R.drawable.ic_launcher;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setPushNotificationBuilder(1, builder);
        Toast.makeText(PushSetActivity.this, "Basic Builder - 1", Toast.LENGTH_SHORT).show();
    }


    /**
     * 设置通知栏样式 - 定义通知栏Layout
     */
    private void setStyleCustom() {
        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(PushSetActivity.this, R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);
        builder.layoutIconDrawable = R.drawable.ic_launcher;
        builder.developerArg0 = "developerArg2";
        JPushInterface.setPushNotificationBuilder(2, builder);
        Toast.makeText(PushSetActivity.this, "Custom Builder - 2", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    IUtil.log(logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    IUtil.log(logs);
                    if (IUtil.isConnected()) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    } else {
                        IUtil.log("No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    IUtil.log(logs);
            }

            IUtil.asynToast(logs);
        }

    };

    private final TagAliasCallback mTagsCallback = new TagAliasCallback() {

        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    IUtil.log(logs);
                    break;

                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    IUtil.log(logs);
                    if (IUtil.isConnected()) {
                        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_TAGS, tags), 1000 * 60);
                    } else {
                        IUtil.log("No network");
                    }
                    break;

                default:
                    logs = "Failed with errorCode = " + code;
                    IUtil.log(logs);
            }

            IUtil.asynToast(logs);
        }

    };


    private static final int MSG_SET_ALIAS = 1001;
    private static final int MSG_SET_TAGS = 1002;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
                    break;

                case MSG_SET_TAGS:
                    JPushInterface.setAliasAndTags(getApplicationContext(), null, (Set<String>) msg.obj, mTagsCallback);
                    break;

                default:
            }
        }
    };
}
