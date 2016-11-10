package cn.swsk.rgyxtqapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.Pop1Action;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.Region;
import cn.swsk.rgyxtqapp.bean.TInventory;
import cn.swsk.rgyxtqapp.bean.TInventoryDetail;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class AddInventoryActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
    private Button save;
    private TextView datetv;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Pop1Action pop1;
    private TextView wareHouse, area;
    private int ary[] = new int[5];
    private EditText reckonertv,logsubjtv,input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.addinventoryactivity;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        datetv = (TextView) findViewById(R.id.datetv);
        datetv.setText(sdf.format(new Date()));
        input = (EditText) findViewById(R.id.et01);
        logsubjtv = (EditText) findViewById(R.id.logsubjtv);
        reckonertv = (EditText) findViewById(R.id.reckonertv);

        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())) {
                    ary[1]=0;return;
                }
                ary[1] = Integer.parseInt(s.toString());
                cal();
            }
        });
        input.getEditableText().append("0");


        wareHouse = (TextView) findViewById(R.id.warehousetv);
        area = (TextView) findViewById(R.id.areatv);
        pop1 = new Pop1Action(this, (View) findViewById(R.id.warehouse));
        pop1.cb = new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                wareHouse.setText(resp);
                Region r = null;
                area.setText(pop1.getSelectedRegion().getRegionName());
                final String path = PushUtils.getServerIP(AddInventoryActivity.this) +
                        "rgyx/AppManageSystem/getCount?wareHouse=" + CommonUtils.UrlEnc(resp);
                HttpUtils.commonRequest(path, AddInventoryActivity.this, new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        Map<String, Object> resul = (Map<String, Object>) map.get("map");

                        ary[2] = Integer.valueOf(resul.get("returnCount").toString()) ;
                        ary[0] = Integer.valueOf(resul.get("normalCount").toString());
                        cal();

                    }
                });
            }
        };
        findViewById(R.id.warehouse).setOnClickListener(this);
    }

    private void cal() {
        int total = ary[0] + ary[2];
        ary[3] = ary[1] - total;
        ary[4] = total - ary[1];
        if(ary[3]<0)
            ary[3]=0;
        if(ary[4]<0)
            ary[4]=0;
        for (int i = 0; i < 5; i++) {
            if(i==1)continue;
            ((EditText) findViewById(CommonUtils.getId(this, "et0" + i,
                    "id"))).setText(ary[i] + "");
        }
    }



    private Date getCreateDate(){
        Date date = new Date();
        try {
            date = sdf.parse(datetv.getText().toString());
        }catch(Exception e){}
        return date;
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
            in.setCreatorId(PushUtils.getUserName());
            in.setOperator(reckonertv.getText().toString());
            TInventoryDetail detail= new TInventoryDetail();

            in.gettInventoryDetail().add(detail);
            detail.setPaperQty(ary[0] + "");
            detail.setRealQty(ary[1]);
            detail.setReturnQty(ary[2]);
            if(in.incomplete()){
                CommonUtils.log("请完善盘点信息");
                return;
            }
            List<TInventory> list = new ArrayList();
            list.add(in);
            HttpUtils.commonRequest3(path, AddInventoryActivity.this, JsonTools.getJsonStr(list), new HttpUtils.RequestCB
                    () {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if(type!=0)return;
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
                    datetv.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();

        } else if (v.getId() == R.id.warehouse) {
            CommonUtils.hideSoftInput(v, this);
            pop1.showAsDropDown();
        } else {
        }


    }

}




