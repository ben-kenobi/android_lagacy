package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/18.
 */
public class ImportManagementActivity extends AnquanQRBaseActivity implements View
        .OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button input, save, cancel;
    private RadioGroup rg;

    {
        dataObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {


            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.importmanagementa_ctivity;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        rg = (RadioGroup) findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == this.save) {
            try{
                String  path=PushUtils.getServerIP(this)  +
                        "rgyx/AppManageSystem/Storage?loginName="+ URLEncoder.encode(PushUtils.getUserName(),"UTF-8")+"&type="+(dapter.state==0?1:(dapter.state+3));
                String savetype = dapter.state==0?"新增入库":dapter.state==1?"归还入库":"调拨入库";
                save(path,savetype);
            } catch(Exception e){}

        } else if (v == cancel) {
            finish();
        } else if (v == input) {
            Intent intent = new Intent();
            intent.setClass(this, ManualInputActivity.class);
            intent.putExtra("title", "手动输入");
            startActivity(intent);

        }
    }

    @Override
    protected void afterAddTequip(List<TEquip> list) {
        boolean hastoast=false;

        if(dapter.state == 2){
           for(TEquip eq:list){
               int idx=dapter.infolist.indexOf(eq);
               if(idx<0){
                   CommonUtils.toast("装备不在调拨范围",this);
                   return ;
               }

           }
        }


        if(dapter.state!=0) return;
        for (TEquip eq : list) {
            eq.setCheckstate(((eq.getStatus() == null) &&eq.getEstatus()==null) ? 1 : 2);
            if(!(eq.getStatus()==null && eq.getEstatus()==null)&&!hastoast){
                CommonUtils.toast("装备不符合新增条件", this);
                hastoast=true;
            }
        }


    }

    @Override
    protected void beforeCommit() {
        for(TEquip eq: dapter.getCheckedInfos()){
            if("2".equals(eq.getEstatus())&&5==eq.getStatus()){
                eq.setEstatus("1");
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb01) {
            dapter.setInfos(new ArrayList<TEquip>(), 0);
        } else if (checkedId == R.id.rb02 || checkedId == R.id.rb03) {
            final int state = checkedId == R.id.rb02 ? 1 : 2;
            String path="";
            try{
             path=PushUtils.getServerIP(this)  +
                    "rgyx/AppManageSystem/Storage?loginName="+ URLEncoder.encode(PushUtils.getUserName(),"UTF-8")+"&type="+(state+1);

            } catch(Exception e){

            }
            HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp,int type) {
                    resp=JsonTools.getJsonStr(map.get("tequips"));
                    CommonUtils.log("resp="+resp);
                    List<TEquip> list = null;
                    if (resp != null && resp.startsWith("["))
                        list = JsonTools.getTEquips(resp);

                    if (list != null ) {
                        CommonUtils.log("=====================" + list.size());
                        dapter.setInfos(list,state);
                    } else {
                        CommonUtils.log("=====================");
                        CommonUtils.toast("获取数据失败", ImportManagementActivity.this);
                    }
                }
            });

        }
    }
}
