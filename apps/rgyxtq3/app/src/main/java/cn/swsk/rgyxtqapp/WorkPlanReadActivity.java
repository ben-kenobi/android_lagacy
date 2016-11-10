package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.WorkPlan;
import cn.swsk.rgyxtqapp.custom.PlanDetailPopupWindow;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * 作业方案查看
 * Created by tom on 2015/10/17.
 */
public class WorkPlanReadActivity extends Activity {

    private List<WorkPlan> mDatas;
    private PlanDetailPopupWindow mPopupWindow;
    private GridView mGridView;
    private RelativeLayout mBotton;
    private ProgressDialog dialog = null; //等待状态提示框
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WorkPlanRead Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cn.swsk.rgyxtqapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "WorkPlanRead Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://cn.swsk.rgyxtqapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /**
     * 异步加载方案列表
     */
    class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog(); //显示加载等待提示框
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭加载等待提示框

            if (stringObjectMap == null) {
                Toast.makeText(WorkPlanReadActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //获取返回的数据集合
                List<Map<String, Object>> workPlanList = (List<Map<String, Object>>) stringObjectMap.get("workPlanList");

                if(workPlanList != null) {
                    for (Map<String, Object> map : workPlanList) {
                        WorkPlan workPlan = new WorkPlan();
                        workPlan.setFamc(map.get("WORK_PLAN_NAME").toString());
                        workPlan.setAqsj(map.get("WORK_DATE").toString() + " " + map.get("SCHEME_MAKING_DATE").toString());
                        workPlan.setId(map.get("ID").toString());
                        workPlan.setStatus(map.get("STATUS") == null ? "" : map.get("STATUS").toString());
                        mDatas.add(workPlan);
                    }
                    mGridView.setAdapter(new CommonAdapter<WorkPlan>(WorkPlanReadActivity.this, mDatas, R.layout.activity_plan_item) {
                        @Override
                        public void convert(ViewHolder holder, WorkPlan workPlan) {
                            holder.setText(R.id.tv_name, workPlan.getFamc())
                                    .setText(R.id.tv_time, workPlan.getAqsj());
                        }
                    });
                }else{
                    Toast.makeText(WorkPlanReadActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                }
            } else if ("2".equals(status)) {
                Toast.makeText(WorkPlanReadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(WorkPlanReadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WorkPlanReadActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
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
     * 异步加载作业方案信息
     */
    class Tom1AsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog(); //显示加载等待提示框
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭加载等待提示框

            if (stringObjectMap == null) {
                Toast.makeText(WorkPlanReadActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //初始化popupWindow
                List<WorkPlan> datas = new ArrayList<WorkPlan>();

                Map<String, Object> workPlanInfo = (Map<String, Object>) stringObjectMap.get("workPlanInfo");

                WorkPlan workPlan = new WorkPlan();
                workPlan.setFamc(workPlanInfo.get("WORK_PLAN_NAME").toString());
                workPlan.setFazzsj(workPlanInfo.get("WORK_DATE").toString() + " " + workPlanInfo.get("SCHEME_MAKING_DATE").toString());
                workPlan.setId(workPlanInfo.get("ID") == null ? "" : workPlanInfo.get("ID").toString());
                workPlan.setStatus(workPlanInfo.get("STATUS") == null ? "" : workPlanInfo.get("STATUS").toString());
                workPlan.setAqsj(workPlanInfo.get("SAFE_RANGE") == null ? "" : workPlanInfo.get("SAFE_RANGE").toString());
                workPlan.setZjyj(workPlanInfo.get("BEST_ELEVATION") == null ? "" : workPlanInfo.get("BEST_ELEVATION").toString());
                workPlan.setZjfwj(workPlanInfo.get("BEST_AZIMUTH") == null ? "" : workPlanInfo.get("BEST_AZIMUTH").toString());
                workPlan.setZyqjlx(workPlanInfo.get("WORK_WARE_TYPE") == null ? "" : workPlanInfo.get("WORK_WARE_TYPE").toString());
                workPlan.setZylx(workPlanInfo.get("WORK_TYPE") == null ? "" : workPlanInfo.get("WORK_TYPE").toString());
                workPlan.setTqxs(workPlanInfo.get("WEATHER_SITUATION") == null ? "" : workPlanInfo.get("WEATHER_SITUATION").toString());

                datas.add(workPlan);

                mPopupWindow = new PlanDetailPopupWindow(WorkPlanReadActivity.this, datas);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        lightOn();
                    }
                });

                mPopupWindow.setAnimationStyle(R.style.plan_popupwindow_anim);
                mPopupWindow.showAsDropDown(mBotton, 0, 0);
                lightOff();
            } else if ("2".equals(status)) {
                Toast.makeText(WorkPlanReadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(WorkPlanReadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WorkPlanReadActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(stringObjectMap);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_read);
        init();

        topbar mTopbar = (topbar) findViewById(R.id.workPlanTopbar);
        mBotton = (RelativeLayout) findViewById(R.id.al_button);
        mTopbar.setRightButtonIsvisable(false);
        Intent intent = getIntent();
        mTopbar.setTitleText(intent.getStringExtra("title"));
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });

        mDatas = new ArrayList<WorkPlan>();
        mGridView = (GridView) findViewById(R.id.gv_plan);
        //获取方案列表
        String PATH = "http://" + PushUtils.getServerIPText(this) + "/rgyx/appWorkInfoManage/findWorkPlanList";
        String path = PATH + "?token=" + PushUtils.token;
        new TomAsyncTask().execute(path);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //查询作业方案信息
                String PATH = "http://" + PushUtils.getServerIPText(WorkPlanReadActivity.this) + "/rgyx/appWorkInfoManage/findWorkPlanInfo";
                String path = PATH + "?token=" + PushUtils.token + "&id=" + ((WorkPlan) parent.getItemAtPosition(position)).getId();
                new Tom1AsyncTask().execute(path);
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * 初始化
     */
    private void init() {
        dialog = new ProgressDialog(WorkPlanReadActivity.this);
    }

    private void lightOn() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1.0f;
        getWindow().setAttributes(lp);
    }

    private void lightOff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}
