package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.AnquanItemsAdapter;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by Administrator on 2016/3/4.
 */
public abstract  class AnquanQRBaseActivity extends AnquanguanliBaseActivity {
    public ListView lv;
    public AnquanItemsAdapter dapter;
    public Button scan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = (ListView)findViewById(R.id.lv);
        if(dapter==null)
            lv.setAdapter(dapter=new AnquanItemsAdapter(this));
        else lv.setAdapter(dapter);
        scan = (Button)findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AnquanQRBaseActivity.this, ScanQRCode.class);
                intent.putExtra("title", "扫描二维码");
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==1&&resultCode==1){
            final String result = data.getStringExtra("result");
            HttpUtils.commonRequest2(PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/parseCode?code=" + result, this, new HttpUtils
                    .RequestCB() {

                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    List<TEquip> list = null;
                    if (resp != null && resp.startsWith("["))
                        list = JsonTools.getTEquips(resp);

                    if (list != null && list.size() > 0) {
                        CommonUtils.log("=====================" + list.size());
                        dapter.addInfos(list);
                    } else {
                        CommonUtils.log("=====================");
                        CommonUtils.toast("获取数据失败", AnquanQRBaseActivity.this);
                    }
                }
            });
        }
    }

    protected  void save(String path){
        String  data= JsonTools.getJsonStr(dapter.getCheckedInfos());

        if(data==null||data.length()<8){
            CommonUtils.toast("无提交的数据",this);
            return ;
        }

        HttpUtils.commonRequest3(path, this, data, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp,int type) {
                if(type==0){
                    dapter.removeCheckedInfos();
                    CommonUtils.toast("保存成功",AnquanQRBaseActivity.this);
                }else{
                    CommonUtils.log(map.toString());
                    if("-2".equals(map.get("status")+"")){
                        if((map.get("code")+"").length()>1)
                             dapter.changeState (((String)map.get("code")).split("\\|"));
                    }

                }

            }
        });


    }

}
