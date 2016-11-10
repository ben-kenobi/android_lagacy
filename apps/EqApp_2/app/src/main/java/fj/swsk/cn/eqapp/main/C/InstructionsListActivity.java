package fj.swsk.cn.eqapp.main.C;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;


/**
 * 指令列表
 */
public class InstructionsListActivity extends BaseTopbarActivity  implements AdapterView.OnItemClickListener{

    ListView lv ;
    CommonAdapter<Map<String, Object>> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes=R.layout.instructionlist_activity;
        super.onCreate(savedInstanceState);
        mTopbar.setRightButtonIsvisable(false);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        lv = (ListView) findViewById(R.id.pinnedlv);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter=new CommonAdapter<Map<String, Object>>(this, null,  R.layout.item4lv_eqhislv) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> m) {
                holder.setText(R.id.time, "来自："+m.get("sendUserId") + "    " + m.get("sendDate") );
                holder.setText(R.id.title,"内容："+m.get("content")+"");
            }
        });

        String path = IConstants.instructionList+"?token="+ PushUtils.getToken();
        NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                List<Map<String,Object>> li = (List<Map<String, Object>>)((List) map.get("data"));
                adapter.setDatas(li);
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onShowDetails(adapter.getItem(position));
    }

    public void onShowDetails(Map<String, Object> map){

        StringBuffer sb = new StringBuffer();
        sb.append("时间：").append(map.get("sendDate").toString()).append("\n");
        sb.append("内容：").append(map.get("content").toString());

        AlertDialog  myDialog = new AlertDialog.Builder(this)
                .setTitle("指令").setMessage(sb.toString())
                .setPositiveButton("确定", null).show();

    }


}
