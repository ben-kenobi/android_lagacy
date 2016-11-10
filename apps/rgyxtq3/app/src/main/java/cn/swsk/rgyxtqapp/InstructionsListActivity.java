package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;


/**
 * 指令列表
 */
public class InstructionsListActivity extends Activity {

    private static final String TAG = "InstructionsListActivity";

    private ProgressDialog dialog = null; //等待状态提示框
    private List<HashMap<String, Object>> instructionsList;
    ListView list_instructions; // ListView控件
    SimpleAdapter listItemAdapter; // ListView的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_list);

        init();
    }

    /**
     * 初始化
     */
    private void init() {
        dialog = new ProgressDialog(InstructionsListActivity.this);
        //控件参数化
        list_instructions = (ListView) findViewById(R.id.list_instructions);
        //设置标题栏
        topbar mTopbar = (topbar) findViewById(R.id.instructionsListTopbar);
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
        instructionsList = new ArrayList<HashMap<String, Object>>();
        listItemAdapter = new SimpleAdapter(this, instructionsList, R.layout.activity_instructions_list_item,
                new String[]{"content", "sendDate"},
                new int[]{R.id.lbl_instructions_list_title, R.id.lbl_instructions_list_info});

        list_instructions.setAdapter(listItemAdapter);

        //控件事件绑定
        list_instructions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = instructionsList.get(position);
                Log.v(TAG, map.toString());
                onShowDetails(map);
            }
        });

        //获取方案列表
        String path = "http://" + PushUtils.getServerIPText(this) + "/rgyx/appUser/findInstructionsList";
        path += "?token=" + PushUtils.token;
        new TomAsyncTask().execute(path);
    }

    /**
     * 列表项单击事件
     * @param map
     */
    public void onShowDetails(HashMap<String, Object> map){
//        Intent i = new Intent();
//        i.setClass(InstructionsListActivity.this, InstructionsListItemActivity.class);
//        i.putExtra("instructionsId", map.get("id").toString());
//        i.putExtra("sendDate", map.get("sendDate").toString());
//        i.putExtra("content", map.get("content").toString());
//        startActivity(i);
//        finish();

        StringBuffer sb = new StringBuffer();
        sb.append("时间：").append(map.get("sendDate").toString()).append("\n");
        sb.append("内容：").append(map.get("content").toString());

        AlertDialog  myDialog = new AlertDialog.Builder(InstructionsListActivity.this).setTitle("指令")
                .setMessage(sb.toString())
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("关闭", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                    }
                }).show();
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
                Toast.makeText(InstructionsListActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //获取返回的数据集合
                List<HashMap<String, Object>> tempInstructionsList = (ArrayList<HashMap<String, Object>>) stringObjectMap.get("instructionsList");

                if(tempInstructionsList != null && tempInstructionsList.size() > 0){
                    for (HashMap<String, Object> map : tempInstructionsList) {
                        instructionsList.add(map);
                    }
                    listItemAdapter.notifyDataSetChanged(); //更新显示
                }else{
                    Toast.makeText(InstructionsListActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
                }
            } else if ("2".equals(status)) {
                Toast.makeText(InstructionsListActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(InstructionsListActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(InstructionsListActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
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
}
