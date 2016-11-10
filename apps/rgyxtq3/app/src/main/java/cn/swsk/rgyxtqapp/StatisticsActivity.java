package cn.swsk.rgyxtqapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import org.codehaus.jackson.type.TypeReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.TInventory;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class StatisticsActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
    private TextView fromdate,todate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ListView mLv;
    private CommonAdapter<Map<String,String>> adapter;
    private String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.activity_statistics;
        super.onCreate(savedInstanceState);
        fromdate = (TextView) findViewById(R.id.fromdate);
        fromdate.setText(sdf.format(new Date()));
        todate = (TextView) findViewById(R.id.todate);
        todate.setText(sdf.format(new Date()));
        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(adapter=new CommonAdapter<Map<String,String>>(this,null
                , R.layout.item4lv_statisticsitem) {
            @Override
            public void convert(ViewHolder holder, Map<String,String> map) {
                int total=Integer.parseInt(map.get("destroyCount"))+ Integer.parseInt(map.get("normalCount"))+ Integer.parseInt(map.get("outCount"));
                holder.setText(R.id.tv01, total+"");
                holder.setText(R.id.tv02, map.get("normalCount"));
                holder.setText(R.id.tv03, map.get("destroyCount"));
                holder.setText(R.id.tv04, map.get("outCount"));
                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
                        0xffffffff : 0xfff5f5f5);
            }
        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.statisticstype){
            final View vg = v;
            final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            String ary[]=getResources().getStringArray(R.array.equipproducer);
            for(String str:ary){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", str);
                list.add(map);
            }

            String title = ((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
            LVDialog.getLvDialogNShow(this, list, title.substring(0, title.length() - 1), null, new
                    AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) ((ViewGroup) vg).getChildAt(1)).setText(list.get(position).get("name"));
                            type = list.get(position).get("name");
                        }
                    });
        }else if (v.getId()==R.id.save){

            String path = PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/queryCount?";
            StringBuffer sb = new StringBuffer();
            sb.append("beginDate="+fromdate.getText().toString()+"&");
            sb.append("endDate="+todate.getText().toString()+"&");
            sb.append("manuFacturer="+ CommonUtils.UrlEnc(type)+"&");
            path+=sb.toString();
            HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    Map<String,String> m = (Map<String,String>)map.get("map");
                    List<Map<String,String>> list = new ArrayList<Map<String, String>>();
                    list.add(m);
                    adapter.setDatas(list);


                }
            });

        }else if (v.getId() == R.id.fromdate) {
            Calendar cal = Calendar.getInstance();

            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fromdate.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
            return;
        }else if (v.getId() == R.id.todate){
            Calendar cal = Calendar.getInstance();

            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    todate.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
            return;
        }

    }
}
