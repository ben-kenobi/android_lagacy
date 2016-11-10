package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.adapter.Pop1Action;
import cn.swsk.rgyxtqapp.bean.TCodeCount;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.bean.TInventory;
import cn.swsk.rgyxtqapp.bean.TInventoryDetail;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class AddInventoryActivity extends AnquanQRBaseActivity implements View.OnClickListener {
    private Button save;
    private TextView datetv;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Pop1Action pop1;
    private TextView wareHouse, area;
    private int ary[] = new int[5];
    private EditText reckonertv, logsubjtv, input;

    {
        dataObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {
                List<TEquip> list = (List<TEquip>) data;

//                for (TEquip eq : list) {
//                    boolean b ="1".equals(eq.getEstatus())&&(eq.getStatus()==0||eq.getStatus()==12)
//                            &&PushUtils.getUserName().equals(eq.getWareHouse());
//                    eq.setCheckstate(b ? 1 : 2);
//
//                }
                int count = dapter.getCheckedInfos().size()+dapter
                        .getCount()-ary[0];
                input.setText(count + "");

                ary[1] = count;
                cal();

            }
        };
    }


    protected void afterAddTequip(List<TEquip> list){

        for(int i=0;i<list.size();i++){
            TEquip eq=list.get(i);
            if(eq.getCheckstate()==1){
//                CommonUtils.toast("发现不在库装备",this);
                break;
            }

        }
       dataObs.update(null,null);

    }


    @Override
    protected void onResume() {
        super.onResume();
       findViewById(R.id.ll04).findViewById(R.id.tv05).setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.addinventoryactivity;
        super.onCreate(savedInstanceState);
        dapter.operatable=false;

        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        datetv = (TextView) findViewById(R.id.datetv);
        datetv.setText(sdf.format(new Date()));
        input = (EditText) findViewById(R.id.et01);
        logsubjtv = (EditText) findViewById(R.id.logsubjtv);
        logsubjtv.setText(PushUtils.getUserName() + "_" + datetv.getText());
        logsubjtv.setEnabled(false);
        reckonertv = (EditText) findViewById(R.id.reckonertv);
        input.setEnabled(false);

//        input.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (TextUtils.isEmpty(s.toString())) {
//                    ary[1] = 0;
//                    return;
//                }
//                ary[1] = Integer.parseInt(s.toString());
//                cal();
//            }
//        });
        input.getEditableText().append("0");


        wareHouse = (TextView) findViewById(R.id.warehousetv);
        area = (TextView) findViewById(R.id.areatv);
        pop1 = new Pop1Action(this, (View) findViewById(R.id.warehouse));
        pop1.cb = new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                setWareHouse(resp, pop1.getSelectedRegion());

            }
        };
        findViewById(R.id.warehouse).setOnClickListener(this);

        String area = "";
        if (PushUtils.sWareHouse != null)
            area = PushUtils.sWareHouse.getBelongRegion();
        setWareHouse(PushUtils.getUserName(), area);
        wareHouse.setEnabled(false);


    }


    private void setWareHouse(String wareHousename, String areanam) {
        wareHouse.setText(wareHousename);
        area.setText(areanam);
        final String path = PushUtils.getServerIP(AddInventoryActivity.this) +
                "rgyx/AppManageSystem/getCount?wareHouse=" + CommonUtils.UrlEnc(wareHousename);
        HttpUtils.commonRequest(path, AddInventoryActivity.this, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                Map<String, Object> resul = (Map<String, Object>) map.get("map");

                resp = JsonTools.getJsonStr(resul.get("normal"));
                CommonUtils.log("resp=" + resp);
                List<TEquip> list = null;
                if (resp != null && resp.startsWith("["))
                    list = JsonTools.getTEquips(resp);

                if (list != null) {
                    CommonUtils.log("=====================" + list.size());
                    ary[2] = Integer.valueOf(resul.get("returnCount").toString());
                    ary[0] = list.size();
                    cal();
                    dapter.setInfos(list, 1);

                } else {
                    CommonUtils.log("=====================");
                    CommonUtils.toast("获取数据失败", AddInventoryActivity.this);
                }



            }
        });

    }

    private void cal() {
        ary[3]=dapter.getCount()-ary[0];
        ary[4]=ary[0]-dapter.getCheckedInfos().size();

        for (int i = 0; i < ary.length; i++) {
            if (i == 1) continue;
            ((EditText) findViewById(CommonUtils.getId(this, "et0" + i,
                    "id"))).setText(ary[i] + "");
        }
    }


    private Date getCreateDate() {
        Date date = new Date();
        try {
            date = sdf.parse(datetv.getText().toString());
        } catch (Exception e) {
        }
        return date;
    }

    private int getStatus() {
        if(ary[ary.length-1]>0 && ary[ary.length-2]>0)
            return -2;

        if (ary[ary.length - 1] > 0) {
            return -1;
        }
        if (ary[ary.length - 2] > 0)
            return 1;
        return 0;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v == this.save) {
            final String path = PushUtils.getServerIP(AddInventoryActivity.this) +
                    "rgyx/AppManageSystem/addInventory?";
            TInventory in = new TInventory();
            in.setRegDate(datetv.getText().toString());
            in.setTitle(logsubjtv.getText().toString());
            in.setRegion(area.getText().toString());
            in.setWareHouse(wareHouse.getText().toString());
            in.setWarehouseId(PushUtils.sWareHouse.getWareHouseId());
            in.setCreatorId(PushUtils.getUserName());
            in.setOperator(reckonertv.getText().toString());
            in.setStatus(getStatus());
            in.setRemark(in.getStatus() == 1 ? "盘盈" : in.getStatus() == -1 ? "盘亏" : in.getStatus() == -2 ? "盘盈又盘亏" : "正常");
            TInventoryDetail detail = new TInventoryDetail();
            in.gettInventoryDetail().add(detail);
            detail.setPaperQty(ary[0] + "");
            detail.setRealQty(ary[1]);
            detail.setReturnQty(ary[2]);
            detail.setWinNum(ary[3]);
            detail.setLessNum(ary[4]);

            if (in.incomplete()) {
                CommonUtils.toast("请完善盘点信息", this);
                return;
            }
            List<TInventory> list = new ArrayList();
            list.add(in);
            List<TCodeCount> countlist = dapter.getCheckedCodeCount("新增盘点");
            Map<String,List> map = new HashMap<>();
            map.put("list",list);
            map.put("countList",countlist);
//            String json = JsonTools.getJsonStr(list);
            String json = JsonTools.getJsonStr(map);
            HttpUtils.commonRequest3(path, AddInventoryActivity.this, json, new HttpUtils.RequestCB
                    () {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    new AlertDialog.Builder(AddInventoryActivity.this).setTitle("提交成功！")
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 点击“确认”后的操作
                                    finish();
                                }
                            }).show();
                }
            });


        } else if (id == R.id.date) {
            Calendar cal = Calendar.getInstance();

            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    datetv.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", (dayOfMonth)));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();

        } else if (v.getId() == R.id.warehouse) {
            CommonUtils.hideSoftInput(v, this);
            pop1.showAsDropDown();
        } else {
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&(resultCode==1||resultCode==-1)){
            ScanQRCode.startScan(this,1);
        }
    }
}




