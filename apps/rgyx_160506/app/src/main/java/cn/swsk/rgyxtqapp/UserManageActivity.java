package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class UserManageActivity extends Activity {

    private Button btn_history_command; //历史指令按钮
    private Button btn_exit; //退出按钮
    //等待状态提示框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_manage);
        //设置标题栏
        topbar mTopbar = (topbar) findViewById(R.id.userManageTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        btn_history_command = (Button) findViewById(R.id.btn_history_command);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout(UserManageActivity.this);
                finish();
            }
        });
        btn_history_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                Intent intent = new Intent(UserManageActivity.this, InstructionsListActivity.class);
                startActivity(intent);
            }
        });
    }


    public static class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        private Activity con;
        private ProgressDialog dialog = null;
        public TomAsyncTask(Activity con){
            this.con=con;
            dialog = new ProgressDialog(con);
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.showDialog();
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭ProgressDialog

            if(stringObjectMap == null){
//                Toast.makeText(con, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            String jsonString1 = HttpUtils.getJsonContent(path);
            if (jsonString1 == null) {
                return null;
            }
            Map<String, Object> map = JsonTools.getMap(jsonString1);
            return map;
        }
    }

    public static void logout(Activity con){
        String path = "http://" + PushUtils.getServerIPText(con) + "/rgyx/appUser/logout";
        path += "?token=" + PushUtils.token;
        new UserManageActivity.TomAsyncTask(con).execute(path);

        PushUtils.token = ""; //清除令牌信息
        //检查 Push Service 是否已经被停止
        if(!JPushInterface.isPushStopped(con.getApplicationContext())){
            // 停止推送服务
            JPushInterface.stopPush(con.getApplicationContext());
        }
        //跳转界面
        Intent intent = new Intent(con, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        con.startActivity(intent);
        //将当前Activity移出栈
    }
}
