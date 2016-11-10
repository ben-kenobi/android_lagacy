package swsk.cn.rgyxtq.subs.work.C;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.work.Common.CommonAdapter;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;
import swsk.cn.rgyxtq.subs.work.Common.ViewHolder;
import swsk.cn.rgyxtq.subs.work.M.WorkPlan;
import swsk.cn.rgyxtq.subs.work.V.PlanDetailPopupWindow;
import swsk.cn.rgyxtq.util.CommonUtils;

/**
 * Created by apple on 16/3/1.
 */
public class WorkPlanReadActivity extends Activity implements AdapterView.OnItemClickListener{
    private List<WorkPlan> mDatas;
    private PlanDetailPopupWindow mPopupWindow;
    private GridView mGridView;
    private RelativeLayout mBottom;

    private GoogleApiClient mClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_read);
        Topbar top = (Topbar)findViewById(R.id.workPlanTopbar);
        mBottom=(RelativeLayout)findViewById(R.id.al_button);
        top.setRightButtonIsvisable(false);
        Intent intent = getIntent();
        top.setTitleText(intent.getStringExtra("title"));
        top.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });
        mDatas= new ArrayList<WorkPlan>();
        mGridView = (GridView)findViewById(R.id.gv_plan);

        findWorkList("rgyx/appWorkInfoManage/findWorkPlanList?token=" + PushUtils.token);
        mGridView.setOnItemClickListener(this);
        mClient=new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mClient.connect();
        Action action = Action.newAction(Action.TYPE_VIEW,
                "WorkPlanRead Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://"+ getPackageName()+"/http/host/path"));
        AppIndex.AppIndexApi.start(mClient,action);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Action action = Action.newAction(Action.TYPE_VIEW,
                "WorkPlanRead Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://"+ getPackageName()+"/http/host/path"));
        AppIndex.AppIndexApi.end(mClient, action);
        mClient.disconnect();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        findWorkInfo("rgyx/appWorkInfoManage/findWorkPlanInfo?token="+ PushUtils.token + "&id=" + ((WorkPlan) parent.getItemAtPosition(position)).getId());
    }


    private void findWorkList(final String path){
        NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map1) {
                List<Map<String, Object>> planlist = (List<Map<String, Object>>) map1.get("workPlanList");
                if (planlist != null && planlist.size() > 0) {
                    for (Map<String, Object> map : planlist) {
                        WorkPlan workPlan = new WorkPlan();
                        workPlan.setFamc(map.get("WORK_PLAN_NAME").toString());
                        workPlan.setAqsj(map.get("WORK_DATE").toString() + " " + map.get("SCHEME_MAKING_DATE").toString());
                        workPlan.setId(map.get("ID").toString());
                        workPlan.setStatus(map.get("STATUS") == null ? "" : map.get("STATUS").toString());
                        mDatas.add(workPlan);
                    }
                    mGridView.setAdapter(new CommonAdapter<WorkPlan>(WorkPlanReadActivity.this,
                            mDatas, R.layout.activity_plan_item) {
                        @Override
                        public void convert(ViewHolder holder, WorkPlan workPlan) {
                            holder.setText(R.id.tv_name, workPlan.getFamc())
                                    .setText(R.id.tv_time, workPlan.getAqsj());
                        }
                    });
                } else {
                    CommonUtils.toast("暂无数据");
                }
            }
        });



    }

    private void findWorkInfo(final String path){
        NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map1) {
                //初始化popupWindow
                List<WorkPlan> datas = new ArrayList<WorkPlan>();

                Map<String, Object> workPlanInfo = (Map<String, Object>) map1.get("workPlanInfo");

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
                        light(true);
                    }
                });

                mPopupWindow.setAnimationStyle(R.style.plan_popupwindow_anim);
                mPopupWindow.showAsDropDown(mBottom, 0, 0);
                light(false);
            }
        });
    }


    private void  light(boolean on){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha=on?1.0f:0.3f;
        getWindow().setAttributes(lp);
        if(!on)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }
}
