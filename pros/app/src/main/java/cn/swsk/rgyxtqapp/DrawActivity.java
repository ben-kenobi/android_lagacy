package cn.swsk.rgyxtqapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.DrawPersonLvAdapter;
import cn.swsk.rgyxtqapp.bean.DrawPerson;
import cn.swsk.rgyxtqapp.custom.LVDialog;

/**
 * Created by apple on 16/2/23.
 */
public class DrawActivity extends AnquanguanliBaseActivity implements View.OnClickListener {
    private ListView lv;
    private Button confirm;
    private DrawPersonLvAdapter dapter;



    public  void preCreate(){
        this.resid=R.layout.draw_activity;
    }
    public   void anaCreate(){
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(dapter = new DrawPersonLvAdapter(this));
        confirm= (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        dapter.addInfo(DrawPerson.getDefault());


    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        final View vg=v;
        final List<Map<String,String>> list=new ArrayList<Map<String,String>>();
        if(v==confirm){
            dapter.addInfo(DrawPerson.getDefault());
            return ;
        }else if(id==R.id.identity){
            Map<String,String> map=new HashMap<String, String>();
            map.put("name", "identity");
            list.add(map);
            list.add(map);
            list.add(map);

        }
        String title=((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
        LVDialog.getLvDialogNShow(this, list, title.substring(0, title.length() - 1), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) ((ViewGroup) vg).getChildAt(1)).setText(list.get(position).get("name"));
            }
        });
    }



}




