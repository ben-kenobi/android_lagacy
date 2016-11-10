package cn.swsk.rgyxtqapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.swsk.rgyxtqapp.adapter.MainGridViewAdapter;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by tom on 2015/10/17.
 */
public class MenuActivity extends
        FragmentActivity implements
        OnItemClickListener {

    private final static int REQUEST_CODE = 1;
    private String text;
    private ProgressDialog dialog = null; //等待状态提示框

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();

        //令牌不存在
        if ("".equals(PushUtils.token)) {
            //跳转到登录界面
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
            //将当前Activity移出栈
            finish();
            return;
        }else if(PushUtils.sWareHouse!=null){

            Intent intent=null;
            intent=new Intent(MenuActivity.this,SafeManagerActivity.class);
            intent.putExtra("title", "安全管理");
            intent.putExtra("root", true);
            startActivity(intent);
            //将当前Activity移出栈
            finish();
        }

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

        topbar mTopbar = (topbar) findViewById(R.id.menuTopbar);

        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setLeftButtonIsvisable(false);
        mTopbar.setTitleText(PushUtils.sysTitle);

        //DragGrid gview = (DragGrid) findViewById(R.id.gview);
        GridView gview = (GridView) findViewById(R.id.gview);
        //配置适配器
        MainGridViewAdapter gridviewAdt = new MainGridViewAdapter(this);
        gview.setAdapter(gridviewAdt);
        //添加监听
        gview.setOnItemClickListener(this);

        //检查 Push Service 是否已经被停止
        if(true||JPushInterface.isPushStopped(getApplicationContext())) {
            // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
            JPushInterface.init(getApplicationContext());
            //极光推送服务会恢复正常工作
            JPushInterface.resumePush(getApplicationContext());
        }
        //获取设备注册ID
        PushUtils.channelId = JPushInterface.getRegistrationID(getApplicationContext());
        upPushChannelId(this);
    }
    /**
     * 初始化
     */
    private void init(){
        dialog = new ProgressDialog(MenuActivity.this);
    }

    //重写返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        if (isHas == false) {
            Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
            return;
        }
        TextView tv = (TextView) view.findViewById(R.id.menu_gridview_item_name);
        text = tv.getText().toString();
        final Intent intent = new Intent();
        if (text.equals(getString(R.string.lbl_work_status_submit))) {
            //请求服务端
            String PATH = "http://" + PushUtils.getServerIPText(MenuActivity.this) + "/rgyx/appWorkSub/findUnFinishedWork";
            String path = PATH + "?token=" + PushUtils.token;
            new findUnFinishedWorkTomAsyncTask().execute(path);
        } else if (text.equals(getString(R.string.lbl_work_info_submit))) {
            intent.setClass(MenuActivity.this, WorkPlanSelListActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("作业方案查看")) {
            intent.setClass(MenuActivity.this, WorkPlanReadActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("设置")) {
            intent.setClass(MenuActivity.this, SettingsActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("安全管理")) {
            intent.setClass(MenuActivity.this, SafeManagerActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("用户管理")) {
            intent.setClass(MenuActivity.this, UserManageActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE && resultCode == AddWorkPointActivity.RESULT_CODE) {
//            Bundle bundle = data.getExtras();
//            String[] results = bundle.getStringArray("result");
//            //Toast.makeText(MenuActivity.this, results[0] + "," + results[1], Toast.LENGTH_SHORT).show();
//            //callWebService();
//            Intent intent = new Intent();
//            intent.setClass(MenuActivity.this, WorkStatusActivity.class);
//            intent.putExtra("title", text);
//            intent.putExtra("workType", results[1]);
//            intent.putExtra("destination", results[0]);
//            intent.putExtra("destinationNo", results[2]);
//            startActivity(intent);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        update();
//    }

    /**
     * 更新推送渠道ID
     */ 
    public static  void upPushChannelId(Context context) {
        if(TextUtils.isEmpty( PushUtils.channelId )||TextUtils.isEmpty( PushUtils.token )){
            return;
        }
        String path = "http://" + PushUtils.getServerIPText(context) + "/rgyx/appUser/upPushChannelId?pushChannelId=" + PushUtils.channelId + "&token=" + PushUtils.token;
        new TomAsyncTask(context).execute(path);
    }

    static class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        private Context mContext;
        public TomAsyncTask(Context context) {
            super();
            mContext=context;

        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            if (stringObjectMap == null) {
                Toast.makeText(mContext, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                // Toast.makeText(MenuActivity.this, "与服务端连接成功!", Toast.LENGTH_SHORT).show();
            } else if ("2".equals(status)) {
                Toast.makeText(mContext, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(mContext, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "未知错误！", Toast.LENGTH_LONG).show();
            }

            super.onPostExecute(stringObjectMap);
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            try {
                String jsonString1 = HttpUtils.getJsonContent(path);
                if (jsonString1 == null) {
                    return null;
                }
                return JsonTools.getMap(jsonString1);
            } catch (Exception e) {
                Log.i(e.getMessage(), "");
            }
            return null;
        }
    }

    /**
     * 获取未完成的作业信息
     */
    class findUnFinishedWorkTomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog();
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭ProgressDialog

            if (stringObjectMap == null) {
                Toast.makeText(MenuActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //获取返回的数据集合
                Map<String, Object> workInfo = (Map<String, Object>) stringObjectMap.get("workInfo");

                //查询数据为空时
                if (workInfo == null) {
                    //打开目的地选择窗口
                    final Intent intent = new Intent();
                    intent.setClass(MenuActivity.this, AddWorkPointActivity2.class);
                    intent.putExtra("title", text);
                    startActivity(intent);
                    return;
                }
                //打开作业状态修改窗口
                Intent intent = new Intent();
                intent.setClass(MenuActivity.this, WorkStatusActivity.class);
                intent.putExtra("workInfoId", workInfo.get("ID").toString());
                intent.putExtra("workType", workInfo.get("WORK_TYPE").toString());
                intent.putExtra("destinationNo", workInfo.get("GOAL_LOCATION").toString());
                intent.putExtra("destinationName", workInfo.get("JOB_SITE_N").toString());
                intent.putExtra("status", Integer.parseInt(workInfo.get("STATUS").toString()));
                //保存作业信息组标识到全局变量中
                PushUtils.workGroupId = workInfo.get("WORK_GROUP_ID").toString();

                intent.putExtra("isNewWork",  false);
                startActivity(intent);
            } else if ("2".equals(status)) {
                Toast.makeText(MenuActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(MenuActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MenuActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(getApplicationContext());
    }
}
