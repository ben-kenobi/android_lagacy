package cn.swsk.rgyxtqapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class StatisticsActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
    private TextView fromdate,todate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy-MM-01");
    private String type="";
    private List<Map<String,String>> manufacturers;
    private TextView tvs[]=new TextView[tvids.length];
    private static int tvids[]={R.id.total,R.id.inbase,R.id.malinbase,R.id.draw,R.id.movebase,R.id.used,
    R.id.malfunc,R.id.outdate,R.id.returnback};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resid=R.layout.activity_statistics;
        super.onCreate(savedInstanceState);
        fromdate = (TextView) findViewById(R.id.fromdate);
        fromdate.setText(sdf01.format(new Date()));
        todate = (TextView) findViewById(R.id.todate);
        todate.setText(sdf.format(new Date()));
        for(int i=0;i<tvs.length;i++){
            tvs[i]=(TextView)findViewById(tvids[i]);
        }
//        mLv = (ListView) findViewById(R.id.lv);
//        mLv.setAdapter(adapter=new CommonAdapter<Map<String,String>>(this,null
//                , R.layout.item4lv_statisticsitem) {
//            @Override
//            public void convert(ViewHolder holder, Map<String,String> map) {
//                holder.setText(R.id.tv01, map.get("totalCount"));
//                holder.setText(R.id.tv02, map.get("userCount"));
//                holder.setText(R.id.tv03, map.get("destroyCount"));
//                holder.setText(R.id.tv04, map.get("outCount"));
//                holder.setText(R.id.tv05, map.get("returnCount"));
//                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
//                        0xffffffff : 0xfff5f5f5);
//            }
//        });
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.statisticstype){
            final View vg = v;
            if(manufacturers!=null){
                String title = ((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
                LVDialog.getLvDialogNShow(this,manufacturers, title.substring(0, title.length() - 1),  new SimpleAdapter(this,
                        manufacturers, R.layout.item4lv_onerow01,
                        new String[] { "manuFacturer" },
                        new int[] { android.R.id.text1 }), new
                        AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ((TextView) ((ViewGroup) vg).getChildAt(1)).setText(manufacturers.get(position).get("manuFacturer").toString());
                                type = manufacturers.get(position).get("manuFacturer").toString();
                            }
                        });
            }else{
                String path = PushUtils.getServerIP(this) +
                        "rgyx/AppManageSystem/findManuFacturer?";
                HttpUtils.commonRequest2(path, this, new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        if (type != 0) return;
                       manufacturers = (List<Map<String,String>>)(List)JsonTools.getListMap("",resp);
                        onClick(vg);
                    }
                });

            }


        }else if (v.getId()==R.id.save){

            String path = PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/queryCount?";
            StringBuffer sb = new StringBuffer();
            sb.append("beginDate="+fromdate.getText().toString()+"&");
            sb.append("endDate="+todate.getText().toString()+"&");
            sb.append("manuFacturer="+ CommonUtils.UrlEnc(type)+"&");
            sb.append("loginName="+ CommonUtils.UrlEnc(PushUtils.getUserName())+"&");
            path+=sb.toString();
            HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    Map<String,String> m = (Map<String,String>)map.get("map");
                    List<Map<String,String>> list = new ArrayList<Map<String, String>>();
                    list.add(m);
                    String sary[]={m.get("totalCount"),m.get("normalCount"),m.get("guCount"),m.get("receiveCount"),
                    m.get("moveCount"),m.get("userCount"),m.get("destroyCount"),
                    m.get("outCount"),m.get("returnCount")};
                    for(int i=0;i<sary.length;i++){
                        tvs[i].setText(sary[i]);
                    }


                }
            });

        }else if (v.getId() == R.id.fromdate) {
            Date date = new Date();
            try {
                date = sdf.parse(fromdate.getText().toString());
            }catch (Exception e){}
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    fromdate.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d",(dayOfMonth)));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
            return;
        }else if (v.getId() == R.id.todate){
            Date date = new Date();
            try {
                date = sdf.parse(todate.getText().toString());
            }catch (Exception e){}
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    todate.setText(year + "-" + String.format("%02d",(monthOfYear + 1)) + "-" + String.format("%02d",(dayOfMonth)));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
            return;
        }

    }
}
