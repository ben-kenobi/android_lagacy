package swsk.cn.rgyxtq.main.C;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.JsonTools;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.security.C.SafeManagerActivity;
import swsk.cn.rgyxtq.subs.user.C.UserManageActivity;
import swsk.cn.rgyxtq.subs.work.C.AddWorkPointActivity;
import swsk.cn.rgyxtq.subs.work.C.WorkPlanReadActivity;
import swsk.cn.rgyxtq.subs.work.C.WorkPlanSelListActivity;
import swsk.cn.rgyxtq.subs.work.C.WorkStatusActivity;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;
import swsk.cn.rgyxtq.util.CommonUtils;
import swsk.cn.rgyxtq.util.DialogUtil;
import swsk.cn.rgyxtq.util.HttpUtils;
import swsk.cn.rgyxtq.util.NetworkUtils;

/**
 * Created by apple on 16/2/25.
 */
public class MenuActivity extends FragmentActivity implements AdapterView.OnItemClickListener{

    private final static int REQUEST_CODE = 1;
    private String text;
    private Dialog dialog = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        dialog = DialogUtil.progressD(this);
        if(!"".equals(PushUtils.token)){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Topbar mTopbar = (Topbar)findViewById(R.id.menuTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setLeftButtonIsvisable(false);
        mTopbar.setTitleText(PushUtils.sysTitle);


        GridView gv = (GridView)findViewById(R.id.gview)
;
        gv.setAdapter(new MenuGVAdapter(this));
        gv.setOnItemClickListener(this);


        if(JPushInterface.isPushStopped(getApplicationContext())){
            JPushInterface.init(getApplicationContext());
            JPushInterface.resumePush(getApplicationContext());
        }
        PushUtils.channelId=JPushInterface.getRegistrationID(getApplicationContext());
        upPushCID();


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        boolean isHas = NetworkUtils.isNetworkAvailable(this);
        // TODO
//        if (isHas == false) {
//            NetUtil.noNetWorkToast();
//            return;
//        }
        TextView tv = (TextView) view.findViewById(R.id.menu_gridview_item_name);
        text = tv.getText().toString();
        final Intent intent = new Intent();
        if (text.equals(getString(R.string.lbl_work_status_submit))) {
            // TODO
            intent.setClass(MenuActivity.this, AddWorkPointActivity.class);
            intent.putExtra("title", "add");
            startActivity(intent);

            //请求服务端
            String path = "rgyx/appWorkSub/findUnFinishedWork?token="+PushUtils.token;
            new FindUnFinishedWorkAsyncTask().execute(path);
        } else if (text.equals(getString(R.string.lbl_work_info_submit))) {
            intent.setClass(MenuActivity.this, WorkPlanSelListActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("作业方案查看")) {
            intent.setClass(MenuActivity.this, WorkPlanReadActivity.class);
            intent.putExtra("title", text);
            startActivity(intent);
        } else if (text.equals("设置")) {
            intent.setClass(MenuActivity.this, SettingActivity.class);
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

    public void upPushCID(){
        new AsyncTask<String,Void,Map<String,Object>>(){
            @Override
            protected Map<String, Object> doInBackground(String... params) {
                String resp=HttpUtils.getJsonContent(params[0]);
                if(resp==null) return null;
                return JsonTools.getMap(resp);
            }



            @Override
            protected void onPostExecute(Map<String, Object> stringObjectMap) {
                if(stringObjectMap==null){
                    NetUtil.connectFailToast();
                    return ;
                }
                String status = stringObjectMap.containsKey("status")?stringObjectMap.get("status")
                        .toString():"";
                if("1".equals(status)){
//                    CommonUtils.toast("连接成功");
                }else if("2".equals(status)||"9999".equals(status)){
                    CommonUtils.toast(stringObjectMap.get("errmsg").toString());
                }else {
                    CommonUtils.toast("未知错误");
                }
                super.onPostExecute(stringObjectMap);

            }
        }.execute("rgyx/appUser/upPushChannelId?pushChannelId=" +
                PushUtils.channelId + "&token=" + PushUtils.token);
    }








    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }








    class FindUnFinishedWorkAsyncTask extends AsyncTask<String,Void,Map<String,Object>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String resp = HttpUtils.getJsonContent(params[0]);
            if(resp==null) return null;
            return JsonTools.getMap(resp);
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.dismiss();
            if(stringObjectMap==null){
                NetUtil.connectFailToast();
                return ;
            }

            String status = stringObjectMap.containsKey("status")?stringObjectMap.get("status").toString():"";
            if("1".equals(status)){
                Map<String,Object> workInfo = (Map<String,Object>)stringObjectMap.get("workInfo");
                if(workInfo==null){
                    Intent intent = new Intent();
                    intent.setClass(MenuActivity.this,AddWorkPointActivity.class);
                    intent.putExtra("title",text);
                    startActivity(intent);
                    return ;
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

                intent.putExtra("isNewWork", false);
                startActivity(intent);
            }else if("2".equals(status)||"9999".equals(status)){
                CommonUtils.toast(stringObjectMap.get("errmsg").toString());
            }else{
                CommonUtils.toast("未知错误");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}


