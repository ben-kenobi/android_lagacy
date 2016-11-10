package cn.swsk.rgyxtqapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.custom.LVDialog;


/**
 * Created by apple on 16/2/19.
 */
public class ManualInputActivity extends AnquanguanliBaseActivity implements
        View.OnClickListener{

    private Button save;
    private TextView datetv;
    private SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid=R.layout.manualinputactivity;
        super.onCreate(savedInstanceState);
        save= (Button)findViewById(R.id.save);
        save.setOnClickListener(this);
//        ViewGroup vg = ((ViewGroup) findViewById(R.id.ll01));
//        for(int i=0;i<vg.getChildCount();i++){
//            vg.getChildAt(i).setOnClickListener(this);
//        }
        datetv= (TextView)findViewById(R.id.datetv);
        datetv.setText(sdf.format(new Date()));
    }




    @Override
    public void onClick(View v) {
        int id=v.getId();
        final View vg=v;
        final List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        if(v==this.save){
            return ;
        }else if(id==R.id.producer){
            Map<String,String> map=new HashMap<String, String>();
            map.put("name", "producer");
            list.add(map);
            list.add(map);
            list.add(map);
        }else if(id==R.id.type){
            Map<String,String> map=new HashMap<String, String>();
            map.put("name","type");
            list.add(map);
            list.add(map);
            list.add(map);

        }else if(id==R.id.batch){
            Map<String,String> map=new HashMap<String, String>();
            map.put("name","batch");
            list.add(map);
            list.add(map);
            list.add(map);
        }else if (id==R.id.date){
            Calendar cal= Calendar.getInstance() ;

            DatePickerDialog dd=new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    datetv.setText(year+"-"+(monthOfYear+1)+"-"+(dayOfMonth));
                }
            }, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            dd.show();
            return;

        }else if (id==R.id.no){
            return;
        }else if(id==R.id.boxno){
            return;
        }else if (id==R.id.code){
            Map<String,String> map=new HashMap<String, String>();
            map.put("name","code");
            list.add(map);
            list.add(map);
            list.add(map);
        }

        String title=((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
        LVDialog.getLvDialogNShow(this, list, title.substring(0,title.length()-1), null,new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)((ViewGroup)vg).getChildAt(1)).setText(list.get(position).get("name"));
            }
        });
    }


}




