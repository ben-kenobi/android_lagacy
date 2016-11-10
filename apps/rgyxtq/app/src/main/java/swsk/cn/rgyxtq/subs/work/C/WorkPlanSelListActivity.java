package swsk.cn.rgyxtq.subs.work.C;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;

/**
 * Created by apple on 16/3/1.
 */
public class WorkPlanSelListActivity extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG ="WorkPlanSelListActivity";
    private List<HashMap<String,Object>> planList;
    ListView list_work_plan;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_plan_sel_list);
        list_work_plan=(ListView)findViewById(R.id.list_work_plan);
        Topbar top= (Topbar)findViewById(R.id.workPlanSelListTopbar);
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

        planList=new ArrayList<>();
        adapter=new SimpleAdapter(this,planList,R.layout.activity_work_plan_sel_list_item,
                new String[]{"WORK_PLAN_NAME"},new int[]{R.id.lbl_work_plan_sel_list_item_title});
        list_work_plan.setAdapter(adapter);
        list_work_plan.setOnItemClickListener(this);
        NetUtil.commonRequest(this, "rgyx/appWorkInfoManage/findWorkPlanList?token=" +
                PushUtils.token + "&status=1", new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map) {
                List<HashMap<String,Object>> list = (ArrayList)map.get("workPlanList");
                if(list!=null && list.size()>0){
                    planList.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });


// TODO
        Intent i = new Intent();
        i.setClass(this,WorkInfoUploadActivity.class);
        i.putExtra("workPlanId", "123123");
        i.putExtra("workPlanName","plana");
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,Object> map=planList.get(position);
        Intent i = new Intent();
        i.setClass(this,WorkInfoUploadActivity.class);
        i.putExtra("workPlanId",map.get("ID").toString());
        i.putExtra("workPlanName",map.get("WORK_PLAN_NAME").toString());
        startActivity(i);
        finish();
    }
}
