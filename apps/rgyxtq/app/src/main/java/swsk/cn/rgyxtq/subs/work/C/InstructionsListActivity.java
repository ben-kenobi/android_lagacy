package swsk.cn.rgyxtq.subs.work.C;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
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
import swsk.cn.rgyxtq.util.CommonUtils;
import swsk.cn.rgyxtq.util.DialogUtil;

/**
 * Created by apple on 16/3/1.
 */
public class InstructionsListActivity  extends Activity implements AdapterView.OnItemClickListener{
    private static final String TAG="InstructionsListActivity";
    private List<HashMap<String,Object>> instructionsList;
    ListView list_instructions;
    SimpleAdapter listItemAdapter;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_list);
        init();
    }

    private void init(){
        dialog= DialogUtil.progressD(this);
        list_instructions = (ListView)findViewById(R.id.list_instructions);
        Topbar mTopbar = (Topbar)findViewById(R.id.instructionsListTopbar);
        mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {

            }
        });


        instructionsList = new ArrayList<HashMap<String, Object>>();
        listItemAdapter= new SimpleAdapter(this,instructionsList,R.layout.activity_instructions_list_item,
                new String[]{"content","sendDate"},new int[]{R.id.lbl_instructions_list_title,R.id.lbl_instructions_list_info});
        list_instructions.setAdapter(listItemAdapter);
        list_instructions.setOnItemClickListener(this);
        findIns();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String,Object>map = instructionsList.get(position);

        StringBuffer sb = new StringBuffer();
        sb.append("时间：").append(map.get("sendDate").toString()).append("\n");
        sb.append("内容：").append(map.get("content").toString());

        DialogUtil.getDefault(this,"指令",sb.toString()).setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    private void findIns(){
        NetUtil.commonRequest(this, "rgyx/appUser/findInstructionsList?token=" + PushUtils.token,
                new NetUtil.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map) {
                        List<HashMap<String,Object>> list=(List<HashMap<String,Object>>)map.get("instructionsList");
                        if(list!=null&&list.size()>0){
                            instructionsList.addAll(list);
                            listItemAdapter.notifyDataSetChanged();
                        }else{
                            CommonUtils.toast("暂无数据");
                        }
                    }
                });

    }
}
