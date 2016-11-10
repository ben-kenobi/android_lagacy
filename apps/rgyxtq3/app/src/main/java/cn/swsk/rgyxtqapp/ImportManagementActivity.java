package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import java.net.URLEncoder;
import java.util.ArrayList;

import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/18.
 */
public class ImportManagementActivity extends AnquanQRBaseActivity implements View
        .OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Button input, save, cancel;
    private RadioGroup rg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.importmanagementa_ctivity;
        super.onCreate(savedInstanceState);
        input = (Button) findViewById(R.id.input);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        input.setOnClickListener(this);
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
                save(path);
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        final int state = checkedId == R.id.rb02 ? 1 : checkedId == R.id.rb03 ?2 :0;
        dapter.setInfos(new ArrayList(), state);
        /*if (checkedId == R.id.rb01) {

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
                public void cb(Map<String, Object> map, String resp, int type) {
                    resp = JsonTools.getJsonStr(map.get("tequips"));
                    CommonUtils.log("resp=" + resp);
                    List<WareHouseT> list = null;
                    if (resp != null && resp.startsWith("["))
                        list = JsonTools.getWareHouseT(resp);

                    if (list != null) {
                        CommonUtils.log("=====================" + list.size());
                        dapter.setInfos(list, state);
                    } else {
                        CommonUtils.log("=====================");
                        CommonUtils.toast("获取数据失败", ImportManagementActivity.this);

                    }
                }
            });

        }*/
    }
}
