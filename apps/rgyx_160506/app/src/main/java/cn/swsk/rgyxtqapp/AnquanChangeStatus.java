package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.custom.InnerLV;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/3/3.
 */
public class AnquanChangeStatus extends AnquanQRBaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {


    private Button save;
    private RadioGroup rg;
    private RadioButton rb02, rb03;

    {
        dataObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                List<TEquip> list = (List<TEquip>) data;
//            if (list.size() == 0) {
//                rb02.setChecked(false);
//                rb03.setChecked(false);
//
//                return;
//            }
//            if (rb02.isChecked() || rb03.isChecked()) return;
//            String estatus;
//
//
//            if(list.get(0).getStatus()==null || list.get(0).getStatus()==0){
//                estatus=list.get(0).getEstatus();
//            }else
//             estatus = list.get(0).getStatus()+"";
//            if ("2".equals(estatus)) {
//                rb02.setChecked(true);
//            }else if("3".equals(estatus)){
//                rb03.setChecked(true);
//            }else{
//                CommonUtils.toast("数据不符合变更条件 ！",AnquanChangeStatus.this);
//            }


            }
        };
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid = R.layout.anquan_changestatus;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);
        rb02 = (RadioButton) findViewById(R.id.rb02);
//        rb03 = (RadioButton) findViewById(R.id.rb03);
        ((InnerLV) lv).setParentScrollView((ScrollView) findViewById(R.id.sc01));
        loadData();


    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb04) {
        } else if (checkedId == R.id.rb06 || checkedId == R.id.rb07) {


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==2 && resultCode==2){
            dapter.clear();
            loadData();
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == this.save) {

            final View rb = rg.findViewById(rg.getCheckedRadioButtonId());
            int id = rg.getCheckedRadioButtonId();
            if (rb == null||id == R.id.rb06 || id == R.id.rb07) {
                CommonUtils.toast("请选择变更的状态", this);
                return;
            }
//            if (!dapter.isStatusConsistence(dapter.getCheckedInfos())) {
//                CommonUtils.toast("提交的数据状态不一致，请核对", this);
//                return;
//            }
            if (id == R.id.rb04&&false) {

            } else if (id == R.id.rb06 || id == R.id.rb07) {

            } else {
                saveWithState("loginName=" + CommonUtils.UrlEnc
                        (PushUtils.getUserName()) + "&type=" + rb.getTag());
            }

        }else if(v.getId() == R.id.rb06 || v.getId() == R.id.rb07){
            Intent intent = new Intent();
            intent.setClass(this, MalfunctionActivity.class);
            intent.putExtra("title", v.getId() == R.id.rb06 ? "故障损毁" : "故障归还");
            intent.putExtra("list", (Serializable) dapter.infolist);
            startActivityForResult(intent, 2);
        }
    }

    private void saveWithState(String path) {
        try {
            List list = dapter.getCheckedInfos();
            if(list==null||list.size()==0){
                if(dapter.infolist.size()>0){
                    CommonUtils.toast("数据无效无法提交保存",this);
                }else{
                    CommonUtils.toast("无提交数据",this);
                }
                return ;
            }


            path = PushUtils.getServerIP(this) + "rgyx/AppManageSystem/StatusChange?" + path;

            String data = JsonTools.getJsonStr(list);



            HashMap<String, String> map = new HashMap<>();
            map.put("list", data);
            HashMap<String, File> files = new HashMap<>();
            HttpUtils.commonRequest4(path, this, map, files, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type == 0) {
                        dapter.removeCheckedInfos();
                        CommonUtils.toast("保存成功", AnquanChangeStatus.this);
                    } else {
                        CommonUtils.log(map.toString());
                        if ("-2".equals(map.get("status") + "")) {
                            if ((map.get("originalNo") + "").length() > 1)
                                dapter.changeState(((String) map.get("originalNo")).split(","));
                        }

                    }

                }
            });
//            save(path);
        } catch (Exception e) {
        }
    }

    private void loadData(){

        String path = PushUtils.getServerIP(this)+"rgyx/AppManageSystem/StatusList?loginName="+ CommonUtils.UrlEnc
                (PushUtils.getUserName());
        HttpUtils.commonRequest2(path, this, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if(type==0){
                    List<TEquip> list = JsonTools.getTEquips(resp);
                    dapter.setInfos(list,1);
                }
            }
        });


    }


}
