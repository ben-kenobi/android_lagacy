package cn.swsk.rgyxtqapp;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.custom.ItemStack;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

public class StockWarningActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
//    public static final String[] overTimeTitles={"厂商","预警数量","预警类型"};
    public static final String[] overTimeTitles={"厂商","编号","过期日期","预警类型"};
    public static final String[] leftTitles={"库名","实际数量","最大库存","最小库存","预警类型"};


//    private TextView fromdate,todate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat sdf01 = new SimpleDateFormat("yyyy-MM-01");
    private ListView mLv;
    private CommonAdapter<Map<String,String>> adapter;
    private CommonAdapter<TEquip> overtimeadapter;
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
//        fromdate = (TextView) findViewById(R.id.fromdate);
//        fromdate.setText(sdf01.format(new Date()));
//        todate = (TextView) findViewById(R.id.todate);
//        todate.setText(sdf.format(new Date()));
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
                if(!sary[sary.length-1].equals("正常"))
                     ((TextView)(((ItemStack) holder.getConvertView()).getChildAt(((ItemStack) holder.getConvertView()).getChildCount()-1)))
                        .setTextColor(0xffee7777);
                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
                        0xffffffff : 0xfff5f5f5);
            }
        });

        overtimeadapter=new CommonAdapter<TEquip>(this,null, ItemStack.class) {
            @Override
            public void convert(ViewHolder holder, TEquip t) {
                String sary[] = new String[]{t.getManuFacturer(),t.getOriginalNo(),t.getOutDate(),"过期预警"};
                ItemStack is=((ItemStack) holder.getConvertView());
                is.setSary(sary);
                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
                        0xffffffff : 0xfff5f5f5);
                Date outdate = null;
                try {
                    outdate = sdf.parse(sary[2]);
                }catch (Exception e){}
                Calendar out = GregorianCalendar.getInstance();
                out.setTime(outdate);
                Calendar now = GregorianCalendar.getInstance();

                int count = 0;
                while(true){
                    if(now.after(out)){
                        break;
                    }else{
                        now.add(Calendar.MONTH,3);
                        count++;
                    }
                }
                int color = count==0?0xffff0000:count==1?0xff990000:0xffeeee33;
                for (TextView tv : is.tvs) {
                    tv.setTextColor(color);
                }
            }
        };
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
            StringBuffer sb = new StringBuffer();
//            sb.append("beginDate="+fromdate.getText().toString()+"&");
//            sb.append("endDate="+todate.getText().toString()+"&");
            sb.append("loginName=" + CommonUtils.UrlEnc(PushUtils.getUserName()) + "&");
            if(curidx==1){
                path+="rocketWarning?";
                path+=sb.toString();
                HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        if (type != 0) return;
                        Map<String, Object> m = (Map<String, Object>) map.get("map");
                        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        for(String key:m.keySet()){
                            if(key.equals("MAX")||key.equals("MIN")){
                                continue;
                            }
                            Map<String,String> ma=new HashMap<String, String>();
                            ma.put("name",key);
                            ma.put("max", (m.get("MAX") + ""));
                            ma.put("min", (m.get("MIN") + ""));
                            if(m.get(key) instanceof Map){
                                ma.put("count",((Map) m.get(key)).get("COUNT")+"");
                                ma.put("max",((Map) m.get(key)).get("MAX")+"");
                                ma.put("min",((Map) m.get(key)).get("MIN")+"");
                                ma.put("loginname",((Map) m.get(key)).get("LOGINNAME")+"");
                            }else{
                                ma.put("count",m.get(key).toString());
                            }
                            int max=Integer.parseInt(ma.get("max").toString());
                            int min = Integer.parseInt(ma.get("min").toString());
                            int count = Integer.parseInt(ma.get("count").toString());
                            ma.put("type", max<count?"高于最高库存":min>count?"低于最低库存":"正常");
                            list.add(ma);
                        }
                        mLv.setAdapter(adapter);
                        adapter.setDatas(list);
                    }
                });

            }else{
//                path+="stockWarning?";
                path+="outWarning?";
                path+=sb.toString();
                HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map, String resp, int type) {
                        if (type != 0) return;
                        List<Map<String, Object>> list1 = (List<Map<String, Object>>) map.get("list");
                        List<TEquip> list = JsonTools.getTEquips(JsonTools.getJsonStr(list1));

                        mLv.setAdapter(overtimeadapter);
                        overtimeadapter.setDatas(list);
                    }
                });
            }





        }/*else if (v.getId() == R.id.fromdate) {
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
                    fromdate.setText(year + "-" + String.format("%02d",(monthOfYear + 1)) + "-" + String.format("%02d",(dayOfMonth)));
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
        }*/

    }
    private void setCuridx(int idx){
        if(idx==curidx)return;
        curidx=idx;
        mLv.setAdapter(null);
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
