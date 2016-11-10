package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.custom.ImgPicker;
import cn.swsk.rgyxtqapp.custom.InnerLV;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by Administrator on 2016/3/4.
 */
public class MalfunctionActivity extends AnquanQRBaseActivity implements View.OnClickListener {


    public static final String[] malfuncRet={
            "火箭弹尾翼","弹尖变形或者松动","喷孔封胶带撕毁",
            "警示标志撕毁","火箭弹破损","留架",
            "电阻值偏高","电阻值偏低","其他不正常故障情况"
    };
    public static final String[] malfuncDestroy={
            "留架燃烧","炸架","跳架",
            "近处掉落","飞行路径异常","未听到自毁声",
            "其他不正常的销毁状况"};

    private Button save, reason;
    private EditText mEditText;
    private int type;
    private boolean isUploaded = false;
    private ImgPicker picker;


    {
        dataObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
//            List<TEquip> list = (List<TEquip>) data;
//            for (TEquip eq : list) {
//                boolean b = ((eq.getStatus() != null && 0 == eq.getStatus()) && "2".equals(eq.getEstatus()));
//                b &= PushUtils.getUserName().equals(eq.getUserTeam());
//                eq.setCheckstate(b ? 1 : 2);
//            }

            }
        };
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid = R.layout.anquan_malfunctionback;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        reason = (Button) findViewById(R.id.reason);
        mEditText = (EditText) findViewById(R.id.et01);
        picker = new ImgPicker(this, (GridView) findViewById(R.id.gv01));
        save.setOnClickListener(this);
        reason.setOnClickListener(this);

        String title = getIntent().getStringExtra("title");
        if ("故障归还".equals(title)) {
            reason.setText(malfuncRet[0]);
            type = 7;
        } else if ("故障损毁".equals(title)) {
            reason.setText(malfuncDestroy[0]);
            type = 6;
        }
        ((InnerLV) lv).setParentScrollView((ScrollView) findViewById(R.id.sv01));

        scan.setVisibility(View.VISIBLE);


        List list = (List)getIntent().getSerializableExtra("list");
        dapter.setInfos(list, 1);
//        afterAddTequip(list);
    }


    @Override
    public void onClick(View v) {
        if (v == save) {
            try {
                if (isUploaded) return;
                isUploaded = true;
                boolean isHas = NetworkUtils.isNetworkAvailable(MalfunctionActivity.this);
                if (isHas == false) {
                    Toast.makeText(MalfunctionActivity.this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT)
                            .show();
                    isUploaded = false;
                    return;
                }

                upload();
            } catch (Exception e) {
            }
        } else if (v == reason) {
            final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            String reasons[] = type==7 ? malfuncRet:malfuncDestroy;
            for(String str:reasons){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", str);
                list.add(map);
            }

            LVDialog.getLvDialogNShow(this, list, "请选择目标库", null, new
                    AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int
                                position, long
                                                        id) {
                            reason.setText(list.get(position).get("name"));
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picker.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }








    @Override
    protected void afterAddTequip(List<TEquip> list) {
        boolean hastoast=false;
        for (TEquip eq : list) {
            boolean b = ((eq.getStatus() != null && 0 == eq.getStatus()) && "2".equals(eq.getEstatus()));
            b&=PushUtils.getUserName().equals(eq.getUserTeam());
            eq.setCheckstate(b ? 1 : 2);
            if(!b&&!hastoast){
                CommonUtils.toast("装备不是被当前分队领用状态", this);
                hastoast=true;
            }
        }

    }




    public void upload() throws Exception {
        List list = dapter.getCheckedInfos();
        if(list==null||list.size()==0){
            if(dapter.infolist.size()>0){
                CommonUtils.toast("数据无效无法提交保存",this);
            }else{
                CommonUtils.toast("无提交数据",this);
            }
            isUploaded = false;
            return ;
        }




        String reasont = reason.getText().toString();
        String info = mEditText.getText().toString();
        if(TextUtils.isEmpty(reasont)){
            CommonUtils.toast("请完善信息", this);
//            mEditText.requestFocus();
            return ;
        }
        String json = JsonTools.getJsonStr(list);
        String path = PushUtils.getServerIP(this) + "rgyx/AppManageSystem/StatusChange?loginName=" +
                CommonUtils.UrlEnc
                        (PushUtils.getUserName()) + "&type=" + type + "&categroy=" + CommonUtils.UrlEnc
                (reasont) + "&problemDesc="
                + CommonUtils.UrlEnc(info);
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
                    new AlertDialog.Builder(MalfunctionActivity.this).setTitle("提交成功！")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    setResult(2);
                                    finish();
                                }
                            }).show();
                }
            }
        });
    }


    protected void upload1() throws Exception {

        String reasont = reason.getText().toString();
        String info = mEditText.getText().toString();
        String json = JsonTools.getJsonStr(dapter.getCheckedInfos());
        String path = PushUtils.getServerIP(this) + "rgyx/AppManageSystem/StatusChange?loginName=" +
                CommonUtils.UrlEnc
                        (PushUtils.getUserName()) + "&type=" + type + "&categroy=" + CommonUtils.UrlEnc
                (reasont) + "&problemDesc="
                + CommonUtils.UrlEnc(info);

        final ProgressDialog pd = DialogUtils.createProgressDialogNshow("提示", "正在提交中，请稍等...",
                true, this);

        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();

        params.addBodyParameter("list", json);

        for (int i = 0; i < picker.tempFiles.size(); i++) {
            params.addBodyParameter(picker.tempFiles.get(i).getPath().replace("/", ""), picker.tempFiles.get(i));
        }

        try {
            httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>
                    () {
                @Override
                public void onFailure(HttpException e, String msg) {
                    Toast.makeText(MalfunctionActivity.this, "提交失败，请检查网络是否正确连接！", Toast
                            .LENGTH_SHORT).show();
                    isUploaded = false;
                    pd.dismiss();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    pd.dismiss();
                    isUploaded = false;

                    //Toast.makeText(WorkInfoUploadActivity.this, "提交成功！", Toast.LENGTH_SHORT)
                    // .show();
                    Map<String, Object> resultMap = JsonTools.getMap(responseInfo.result);

                    if (resultMap.containsKey("status")) {
                        String status = resultMap.get("status").toString(); //服务端反馈状态码

                        if ("0".equals(status)) {
                            new AlertDialog.Builder(MalfunctionActivity.this).setTitle("提交成功！")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 点击“确认”后的操作
                                            finish();
                                        }
                                    }).show();
                        } else if ("2".equals(status)) {
                            Toast.makeText(MalfunctionActivity.this, resultMap.get("errmsg")
                                    .toString(), Toast.LENGTH_LONG).show();
                        } else if ("9999".equals(status)) {
                            Toast.makeText(MalfunctionActivity.this, resultMap.get("errmsg")
                                    .toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MalfunctionActivity.this, "未知错误！", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isUploaded = false;
        }
    }
}
