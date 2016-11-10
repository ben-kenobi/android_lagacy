package swsk.cn.rgyxtq.subs.user.C;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.jpush.android.api.JPushInterface;
import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.C.LoginActivity;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.work.C.InstructionsListActivity;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;

/**
 * Created by apple on 16/3/1.
 */
public class UserManageActivity extends Activity  implements View.OnClickListener{

    private Button btn_history_command;
    private Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        Topbar top = (Topbar)findViewById(R.id.userManageTopbar);
        top.setRightButtonIsvisable(false);
        top.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        btn_history_command=(Button)findViewById(R.id.btn_history_command);
        btn_exit=(Button)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(this);
        btn_history_command.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==btn_history_command){
            startActivity(new Intent(UserManageActivity.this, InstructionsListActivity.class));
        }else if(v==btn_exit){

            NetUtil.commonRequest(this, "rgyx/appUser/logout?token=" + PushUtils.token,
                    null);

            PushUtils.token="";
            if(!JPushInterface.isPushStopped(getApplicationContext())){
                JPushInterface.stopPush(getApplicationContext());
            }
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}
