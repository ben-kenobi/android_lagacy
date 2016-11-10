package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.custom.ImgPicker;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by Administrator on 2016/3/10.
 */
public class ProblemHandleActivity extends AnquanguanliBaseActivity implements View.OnClickListener{
    private ImgPicker picker;
    private CommonAdapter lvadapter;

    private Button save, reason,cancel;
    private EditText mEditText,mEditText2;
    private int type;
    private boolean isUploaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.anquan_handleproblem;
        super.onCreate(savedInstanceState);
        picker=new ImgPicker(this,(GridView)findViewById(R.id.gv01));

        save = (Button) findViewById(R.id.save);
        reason = (Button) findViewById(R.id.reason);
        cancel=(Button)findViewById(R.id.cancel);
        mEditText = (EditText) findViewById(R.id.et01);
        mEditText2 = (EditText) findViewById(R.id.et02);
        save.setOnClickListener(this);
        reason.setOnClickListener(this);
        cancel.setOnClickListener(this);






        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(lvadapter=new CommonAdapter<String>(this, new ArrayList<String>(Arrays.asList(new String[]{"12312","12312","12312","12312",
                "12312","12312","12312","12312","12312","12312","12312","12312"})),R.layout.item4lv_anquanitem2) {
            @Override
            public void convert(ViewHolder holder, String s) {
                Context con =ProblemHandleActivity.this;
                for (int i = 1; i <= 5; i++) {
                   int id= con.getResources().getIdentifier("tv0"+i,"id",con.getPackageName());
                    holder.setText(id, s);
                    holder.setTextColor(id,0xff666666);
                }


                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0?0xffffffff:0xfff5f5f5);
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CommonAdapter<String> ada = (CommonAdapter<String>) parent.getAdapter();
                ada.getDatas().remove(position);
                ada.notifyDataSetChanged();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if (v == save) {
            try {
                if (isUploaded) return;
                isUploaded = true;
                boolean isHas = NetworkUtils.isNetworkAvailable(this);
                if (isHas == false) {
                    Toast.makeText(this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT)
                            .show();
                    isUploaded = false;
                    return;
                }

                upload();
            } catch (Exception e) {
            }
        } else if (v == reason) {
            final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "目标库");
            list.add(map);
            list.add(map);
            list.add(map);

            LVDialog.getLvDialogNShow(this, list, "请选择目标库",null, new
                    AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int
                                position, long
                                                        id) {
                            reason.setText(list.get(position).get("name"));
                        }
                    });
        }else if(v==cancel){
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picker.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upload(){
        String reasont = reason.getText().toString();
        String info = mEditText.getText().toString();
        String desc=mEditText2.getText().toString();
        String json = JsonTools.getJsonStr(lvadapter.getDatas());
        String path = PushUtils.getServerIP(this) + "rgyx/AppManageSystem/StatusChange?loginName=" +
                CommonUtils.UrlEnc
                        (PushUtils.getUserName()) + "&type=" + type + "&categroy=" + CommonUtils.UrlEnc
                (reasont) + "&problemDesc="
                + CommonUtils.UrlEnc(info)+"&desc="+CommonUtils.UrlEnc(desc);
        Map<String, String> map1 = new HashMap<>();
        map1.put("list", json);
        Map<String, File> files = new HashMap<>();
        for (int i = 0; i < picker.tempFiles.size(); i++) {
            files.put(picker.tempFiles.get(i).getPath().replace("/", ""), picker.tempFiles.get(i));
        }


        cn.swsk.rgyxtqapp.utils.HttpUtils.commonRequest5(path, this, map1, files, new cn.swsk
                .rgyxtqapp.utils.HttpUtils.RequestCB() {


            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if (type != 0) {
                    isUploaded = false;
                } else {
                    new AlertDialog.Builder(ProblemHandleActivity.this).setTitle("提交成功！")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    finish();
                                }
                            }).show();
                }
            }
        });
    }
}
