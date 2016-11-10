package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;
import java.util.Map;
import java.util.Observer;

import cn.swsk.rgyxtqapp.adapter.AnquanItemsAdapter;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by Administrator on 2016/3/4.
 */
public abstract class AnquanQRBaseActivity extends AnquanguanliBaseActivity {
    public ListView lv;
    public AnquanItemsAdapter dapter;
    public Button scan;
    protected Observer dataObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = (ListView) findViewById(R.id.lv);
        dapter = new AnquanItemsAdapter(this, lv);
        if (lv != null)
            lv.setAdapter(dapter);
        scan = (Button) findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScanQRCode.startScan(AnquanQRBaseActivity.this, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && (resultCode == 1 || resultCode == -1)) {
            final String result = ScanQRCode.getResult(data);
//            HttpUtils.commonRequest2(PushUtils.getServerIP(this) +
//                    "rgyx/AppManageSystem/parseCode?code=" + result, this, new HttpUtils
//                    .RequestCB() {
//
//                @Override
//                public void cb(Map<String, Object> map, String resp, int type) {
//                    List<TEquip> list = null;
//                    if (resp != null && resp.startsWith("["))
//                        list = JsonTools.getTEquips(resp);
//
//                    if (list != null && list.size() > 0) {
//                        CommonUtils.log("=====================" + list.size());
//                        dapter.addInfos(list);
//                    } else {
//                        CommonUtils.log("=====================");
//                        CommonUtils.toast("获取数据失败", AnquanQRBaseActivity.this);
//                    }
//                }
//            });

            final int getype = data.getIntExtra("type",0);
            CommonUtils.log(result + "--------------------");
            HttpUtils.commonRequest(PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/parseCode?code=" + result, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    resp = JsonTools.getJsonStr(map.get("list"));
                    CommonUtils.log("resp=" + resp);
                    List<TEquip> list = null;
                    if (resp != null && resp.startsWith("["))
                        list = JsonTools.getTEquips(resp);

                    if (list != null) {
                        CommonUtils.log("=====================" + list.size());
                        dapter.addInfos(list);
                        afterAddTequip(list);
                        if (getype == 0) {
                            dapter.scanList.add(list);
                        }else{
                            dapter.manualList.add(list);
                        }
                    } else {
                        CommonUtils.log("=====================");
                        CommonUtils.toast("获取数据失败", AnquanQRBaseActivity.this);
                    }

                }
            });
        }
    }

    protected void afterAddTequip(List<TEquip> list) {
    }

    protected void beforeCommit() {
    }

    protected void save(String path,String savetype) {
        beforeCommit();
        List list = dapter.getCheckedInfos();
        if (list == null || list.size() == 0) {
            if (dapter.infolist.size() > 0) {
                CommonUtils.toast("数据无效无法提交保存", this);
            } else {
                CommonUtils.toast("无提交数据", this);
            }
            return;
        }
//        List countlist = dapter.getCheckedCodeCount(savetype);
//        Map<String,List> map = new HashMap<>();
//        map.put("equipList",list);
//        map.put("countList",countlist);

        String data = JsonTools.getJsonStr(list);
//        String data2 = JsonTools.getJsonStr(countlist);
//        String data = JsonTools.getJsonStr(map);
//        String data = "equipList="+data1+"&countList="+data2;

        HttpUtils.commonRequest3(path, this, data, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if (type == 0) {
                    dapter.removeCheckedInfos();
                    CommonUtils.toast("保存成功", AnquanQRBaseActivity.this);
                } else {
                    CommonUtils.log(map.toString());
                    if ("-2".equals(map.get("status") + "")) {
                        if ((map.get("originalNo") + "").length() > 1)
                            dapter.changeState(((String) map.get("originalNo")).split(","));
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataObs != null)
            dapter.obsable.addObserver(dataObs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataObs != null)
            dapter.obsable.deleteObserver(dataObs);
    }

}
