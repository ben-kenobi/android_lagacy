package cn.swsk.rgyxtqapp;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.custom.ItemStack;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class StockWarningActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
    public static final String[] overTimeTitles={"厂商","预警数量","预警类型"};
    public static final String[] leftTitles={"库名","预警数量","最大库存","最小库存","预警类型"};


    private TextView fromdate,todate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ListView mLv;
    private CommonAdapter<Map<String,String>> adapter;
    private List<Map<String,String>> typelist;
    private int selidx=0,curidx=-1;
    private ItemStack stack;
    private TextView typetv;
    {
         typelist = new ArrayList<Map<String, String>>();
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "过期预警");
        typelist.add(map);
        map = new HashMap<String, String>();
        map.put("name", "库存预警");
        typelist.add(map);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        resid=R.layout.activity_stockwarning;
        super.onCreate(savedInstanceState);
        fromdate = (TextView) findViewById(R.id.fromdate);
        fromdate.setText(sdf.format(new Date()));
        todate = (TextView) findViewById(R.id.todate);
        todate.setText(sdf.format(new Date()));
        mLv = (ListView) findViewById(R.id.lv);
        typetv=(TextView)findViewById(R.id.reckonertv);
        typetv.setText(typelist.get(selidx).get("name"));
        stack=(ItemStack)findViewById(R.id.warningTitle);
        mLv.setAdapter(adapter = new CommonAdapter<Map<String, String>>(this, null
                , ItemStack.class) {
            @Override
            public void convert(ViewHolder holder, Map<String, String> map) {
                String sary[]=null;
                if (curidx == 1) {
                     sary = new String[]{map.get("name"), map.get("count"), CommonUtils.null2zero(map.get
                            ("max")),
                            CommonUtils.null2zero(map.get("min")), map.get("type")};
                }else{
                    sary = new String[]{map.get("name"), map.get("count"), map.get("type")};
                }

                ((ItemStack) holder.getConvertView()).setSary(sary);
                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
                        0xffffffff : 0xfff5f5f5);
            }
        });


        setCuridx(selidx);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.statisticstype){
            final View vg = v;
            String title = ((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
            LVDialog.getLvDialogNShow(this, typelist, title.substring(0, title.length() - 1), null, new
                    AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ((TextView) ((ViewGroup) vg).getChildAt(1)).setText(typelist.get(position).get("name"));
                            selidx=position;
                        }
                    });
        }else if (v.getId()==R.id.save){
            setCuridx(selidx);
            String path = PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/";
            if(curidx==0){
                path+="rocketWarning?";
            }else{
                path+="stockWarning?";
            }
            StringBuffer sb = new StringBuffer();
            sb.append("beginDate="+fromdate.getText().toString()+"&");
            sb.append("endDate="+todate.getText().toString()+"&");
            path+=sb.toString();
            HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    Map<String, Object> m = (Map<String, Object>) map.get("map");
                    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                    for(String key:m.keySet()){
                        Map<String,String> ma=new HashMap<String, String>();
                        ma.put("name",key);
                        if(m.get(key) instanceof Map){
                            ma.put("count",((Map) m.get(key)).get("COUNT")+"");
                            ma.put("max",((Map) m.get(key)).get("MAX")+"");
                            ma.put("min",((Map) m.get(key)).get("MIN")+"");
                            ma.put("loginname",((Map) m.get(key)).get("LOGINNAME")+"");
                        }else{
                            ma.put("count",m.get(key).toString());
                        }
                        ma.put("type", typelist.get(curidx).get("name"));
                        list.add(ma);
                    }
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
    private void setCuridx(int idx){
        if(idx==curidx)return;
        curidx=idx;
        if(curidx==0){
            stack.setSary(overTimeTitles);
        }else if(curidx==1){

//            Map<String, Object> m = new HashMap<>();
//            Map<String,String> m2=new HashMap<>();
//            m2.put("MAX","21");
//            m2.put("MIN","21");
//            m2.put("COUNT","21");
//            m2.put("LOGINNAME","qweqw");
//            m.put("wewef",m2);
//            m.put("wewef2",m2);
//            m.put("wewef3",m2);
//            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
//            for(String key:m.keySet()){
//                Map<String,String> ma=new HashMap<String, String>();
//                ma.put("name",key);
//                if(m.get(key) instanceof Map){
//                    ma.put("count",((Map) m.get(key)).get("COUNT")+"");
//                    ma.put("max",((Map) m.get(key)).get("MAX")+"");
//                    ma.put("min",((Map) m.get(key)).get("MIN")+"");
//                    ma.put("loginname",((Map) m.get(key)).get("LOGINNAME")+"");
//                }else{
//                    ma.put("count",m.get(key).toString());
//                }
//                ma.put("type", typelist.get(curidx).get("name"));
//                list.add(ma);
//            }

            stack.setSary(leftTitles);
//            mLv.setAdapter(adapter = new CommonAdapter<Map<String, String>>(this, list
//                    , R.layout.item4lv_stockwarning2) {
//                @Override
//                public void convert(ViewHolder holder, Map<String, String> map) {
//                    holder.setText(R.id.tv01, map.get("name"));
//                    holder.setText(R.id.tv02, map.get("count"));
//                    holder.setText(R.id.tv03, CommonUtils.null2zero(map.get("max")));
//                    holder.setText(R.id.tv04, CommonUtils.null2zero(map.get("min")));
//                    holder.setText(R.id.tv05, map.get("type"));
//                    holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
//                            0xffffffff : 0xfff5f5f5);
//                }
//            });

        }

    }

}
