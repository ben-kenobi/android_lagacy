package cn.swsk.rgyxtqapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.utils.CommonUtils;

/**
 * Created by apple on 16/4/22.
 */
public class AnquanSystemconfActivity extends AnquanguanliBaseActivity  implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private TextView datetv;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ListView mLv;
    private EditText batchno,period;
    private RadioGroup rg;
    private View vg01,vg02;
    private CommonAdapter<Map<String,Object>> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.anquan_systemconfig;
        super.onCreate(savedInstanceState);
        initUI();
    }


    private void initUI(){
        datetv=(TextView)findViewById(R.id.proddate);
        datetv.setText(sdf.format(new Date()));
        batchno=(EditText)findViewById(R.id.batchno);
        period=(EditText)findViewById(R.id.period);
        vg01=findViewById(R.id.vg01);
        vg02=findViewById(R.id.vg02);
        rg=(RadioGroup)findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);
        mLv=(ListView)findViewById(R.id.lv);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.proddaterl){
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(datetv.getText().toString()));
            }catch (Exception e){}

            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    datetv.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
        }else if (v.getId()==R.id.save){
            CommonUtils.log("save========================");
        }


    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rb01){
            vg01.setVisibility(View.VISIBLE);
            vg02.setVisibility(View.GONE);
        }else if(checkedId == R.id.rb02){
            vg01.setVisibility(View.GONE);
            vg02.setVisibility(View.VISIBLE);
            if(mAdapter==null){
                List<Map<String,Object>> list = new ArrayList<>();
                mAdapter=new CommonAdapter<Map<String, Object>>(AnquanSystemconfActivity.this,list,R.layout.item4lv_anquanbatchhis) {
                    @Override
                    public void convert(ViewHolder holder, Map<String, Object> stringObjectMap) {
                    }
                };
                mLv.setAdapter(mAdapter);
            }
        }

    }
}
