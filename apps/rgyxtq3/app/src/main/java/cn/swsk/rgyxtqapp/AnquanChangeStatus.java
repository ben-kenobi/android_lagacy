package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.adapter.AnquanItemsAdapter2;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.bean.WareHouseT;
import cn.swsk.rgyxtqapp.custom.InnerLV;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/3/3.
 */
public class AnquanChangeStatus extends AnquanguanliBaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    public ListView lv;
    public AnquanItemsAdapter2 dapter;
    private Button save;
    private RadioGroup rg;
    private RadioButton rb02, rb03;

    private Observer dataObs = new Observer() {
        @Override
        public void update(Observable observable, Object data) {
            List<TEquip> list = (List<TEquip>) data;
            if (list.size() == 0) {
                rb02.setChecked(false);
                rb03.setChecked(false);

                return;
            }
            if (rb02.isChecked() || rb03.isChecked()) return;
            String estatus;
            if(list.get(0).getStatus()==0){
                estatus=list.get(0).getEstatus();
            }else
             estatus = list.get(0).getStatus()+"";
            if ("2".equals(estatus)) {
                rb02.setChecked(true);
            }else if("3".equals(estatus)){
                rb03.setChecked(true);
            }else{
                CommonUtils.toast("数据不符合变更条件 ！",AnquanChangeStatus.this);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.anquan_changestatus;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);
        rb02 = (RadioButton) findViewById(R.id.rb02);
        rb03 = (RadioButton) findViewById(R.id.rb03);
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(dapter=new AnquanItemsAdapter2(this));
        ((InnerLV) lv).setParentScrollView((ScrollView) findViewById(R.id.sc01));

    lv.requestDisallowInterceptTouchEvent(true);
        chooseType();
        loadData();
    }
    public void chooseType(){
        if(PushUtils.sWareHouse==null){
            rb02.setChecked(true);
            rb03.setVisibility(View.GONE);
        }else{
            rb03.setChecked(true);
            rg.getChildAt(1).setVisibility(View.GONE);
            rg.getChildAt(3).setVisibility(View.GONE);
            rb02.setVisibility(View.GONE);

        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb04) {
        } else if (checkedId == R.id.rb06 || checkedId == R.id.rb07) {
            Intent intent = new Intent();
            intent.setClass(this, MalfunctionActivity.class);
            intent.putExtra("title", checkedId == R.id.rb06 ? "故障销毁" : "故障归还");
            startActivityForResult(intent, 2);

        }
    }


    @Override
    public void onClick(View v) {
        if (v == this.save) {

            final View rb = rg.findViewById(rg.getCheckedRadioButtonId());
            int id = rg.getCheckedRadioButtonId();
            if (rb == null) {
                CommonUtils.toast("请选择变更的状态", this);
                return;
            }

            if (id == R.id.rb04&&false) {

            } else if (id == R.id.rb06 || id == R.id.rb07) {
            } else {
                saveWithState("loginName=" + CommonUtils.UrlEnc
                        (PushUtils.getUserName()) + "&type=" + rb.getTag());
            }

        }
    }

    private void saveWithState(String path) {
        try {
            path = PushUtils.getServerIP(this) + "rgyx/AppManageSystem/StatusChange?" + path;

            String data = JsonTools.getJsonStr(dapter.infolist);

            if (data == null || data.length() < 8) {
                CommonUtils.toast("无提交的数据", this);
                return;
            }

            HashMap<String, String> map = new HashMap<>();
            map.put("list", data);
            HashMap<String, File> files = new HashMap<>();
            HttpUtils.commonRequest4(path, this, map, files, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type == 0) {
                        loadData();
                        CommonUtils.toast("保存成功", AnquanChangeStatus.this);
                    }

                }
            });
        } catch (Exception e) {
        }
    }

    private void loadData(){
        int type = 2;
        if(PushUtils.sWareHouse!=null){
            type = 1;
        }
        String path = PushUtils.getServerIP(this)+"rgyx/AppManageSystem/StatusList?loginName="+ CommonUtils.UrlEnc
                (PushUtils.getUserName()) + "&type=" + type;
        HttpUtils.commonRequest2(path, this, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
               if(type==0){
                   List<WareHouseT> list = JsonTools.getWareHouseT(resp);
                   dapter.setInfos(list);
               }
            }
        });


    }











    @Override
    protected void onResume() {
        super.onResume();
//        dapter.obsable.addObserver(dataObs);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        dapter.obsable.deleteObserver(dataObs);
    }
}
