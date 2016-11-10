package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * 方案选择列表
 */
public class WorkPlanSelListActivity extends Activity implements RadioGroup.OnCheckedChangeListener{
    private static final String TAG = "WorkPlanSelListActivity";
    private ProgressDialog dialog = null; //等待状态提示框
    private List<HashMap<String, Object>> workPlanList;
    ListView list_work_plan; // ListView控件
    SimpleAdapter listItemAdapter; // ListView的适配器

    private WorkPointView wpv;
    private RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_plan_sel_list);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        dialog = new ProgressDialog(WorkPlanSelListActivity.this);
        //控件参数化
        list_work_plan = (ListView) findViewById(R.id.list_work_plan);
        //设置标题栏
        topbar mTopbar = (topbar) findViewById(R.id.workPlanSelListTopbar);
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

        //数据初始化
        workPlanList = new ArrayList<HashMap<String, Object>>();
        listItemAdapter = new SimpleAdapter(this, workPlanList, R.layout.activity_work_plan_sel_list_item,
                new String[]{"WORK_PLAN_NAME"},
                new int[]{R.id.lbl_work_plan_sel_list_item_title});

        list_work_plan.setAdapter(listItemAdapter);

        //控件事件绑定
        list_work_plan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = workPlanList.get(position);
                Log.v(TAG, map.toString());
                onShowDetails(map);
            }
        });

        //获取未上报的方案列表
        String PATH = "http://" + PushUtils.getServerIPText(this) + "/rgyx/appWorkInfoManage/findWorkPlanList";
        String path = PATH + "?token=" + PushUtils.token + "&status=1";
        new TomAsyncTask().execute(path);
        wpv=(WorkPointView)findViewById(R.id.wpv);
        rg=(RadioGroup)findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);
        wpv.dialogListener=new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                boolean isHas = NetworkUtils.isNetworkAvailable(WorkPlanSelListActivity.this);
                if (isHas == false) {
                    Toast.makeText(WorkPlanSelListActivity.this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent();
                i.setClass(WorkPlanSelListActivity.this, WorkInfoUploadActivity.class);
                i.putExtra("workPoint", wpv.dest);
                i.putExtra("WORK_TYPE",wpv.workType);
                startActivity(i);
                finish();
            }
        };
    }

    /**
     * 列表项单击事件
     * @param map
     */
    public void onShowDetails(HashMap<String, Object> map){
        Intent i = new Intent();
        i.setClass(WorkPlanSelListActivity.this, WorkInfoUploadActivity.class);
        i.putExtra("workPlanId", map.get("ID").toString());
        i.putExtra("workPlanName", map.get("WORK_PLAN_NAME").toString());
        startActivity(i);
        finish();
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
                Toast.makeText(WorkPlanSelListActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //获取返回的数据集合
                List<HashMap<String, Object>> tempWorkPlanList = (ArrayList<HashMap<String, Object>>) stringObjectMap.get("workPlanList");

                if(tempWorkPlanList != null){
                    for (HashMap<String, Object> map : tempWorkPlanList) {
                        workPlanList.add(map);
                    }
                    listItemAdapter.notifyDataSetChanged(); //更新显示
                }

//                for (Map<String, Object> map : tempWorkPlanList) {
//                    WorkPlan workPlan = new WorkPlan();
//                    workPlan.setFamc(map.get("WORK_PLAN_NAME").toString());
//                    workPlan.setAqsj(map.get("WORK_DATE").toString() + " " + map.get("SCHEME_MAKING_DATE").toString());
//                    workPlan.setId(map.get("ID").toString());
//                    workPlan.setStatus(map.get("STATUS") == null ? "" : map.get("STATUS").toString());
//                    workPlanList.add(workPlan);
//                    arrModel.add(map.get("WORK_PLAN_NAME").toString());
//                }
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WorkPlanSelListActivity.this, android.R.layout.simple_spinner_dropdown_item, arrModel);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                famc.setAdapter(adapter);
//
//                famc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
            } else if ("2".equals(status)) {
                Toast.makeText(WorkPlanSelListActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(WorkPlanSelListActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WorkPlanSelListActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            try {
                String jsonString1 = cn.swsk.rgyxtqapp.utils.HttpUtils.getJsonContent(path);
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


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rb01){
            wpv.setVisibility(View.VISIBLE);
            list_work_plan.setVisibility(View.GONE);
        }else{
            list_work_plan.setVisibility(View.VISIBLE);
            wpv.setVisibility(View.GONE);
        }
    }
}
