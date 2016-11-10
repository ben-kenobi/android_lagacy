package cn.swsk.rgyxtqapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.DrawPersonLvAdapter;
import cn.swsk.rgyxtqapp.bean.WorkUnit;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class DrawActivity extends AnquanguanliBaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private ListView lv;
    private Button confirm;
    private DrawPersonLvAdapter dapter;
    private EditText phone,name;
    private TextView iden;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid=R.layout.draw_activity;
        super.onCreate(savedInstanceState);
        lv = (ListView)findViewById(R.id.lv);
        phone = (EditText)findViewById(R.id.phonetv);
        name = (EditText)findViewById(R.id.nametv);
        iden = (TextView)findViewById(R.id.identitytv);
        lv.setAdapter(dapter = new DrawPersonLvAdapter(this));
        confirm= (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        lv.setOnItemClickListener(this);
        loadPersons();
        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        iden.setText(intent.getStringExtra("iden"));
        phone.setText(intent.getStringExtra("phone"));
//        this.getWindow().getDecorView().setBackgroundColor(0x66000000);
//        int pad=20*CommonUtils.getDensity(this);
//        this.getWindow().getDecorView().setPadding(pad,pad,pad,pad);

    }
    private void loadPersons(){
        if(PushUtils.sWareHouse==null) return ;
        String path = PushUtils.getServerIP(this)  + "rgyx/AppManageSystem/getWorkUnit?loginName="+
                CommonUtils.UrlEnc(PushUtils.sWareHouse.getBelongRegion());

        HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                resp = JsonTools.getJsonStr(map.get("units"));
                List<WorkUnit> list = null;
                if (resp != null && resp.startsWith("["))
                    list = JsonTools.getWorkUnits(resp);

                if (list != null) {
                    CommonUtils.log("=====================" + list.size());
                    dapter.addInfos(list);
                } else {
                    CommonUtils.log("=====================");
                    CommonUtils.toast("获取数据失败", DrawActivity.this);
                }
            }
        });
    }



    @Override
    public void onClick(View v) {
        int id=v.getId();
        final View vg=v;

        if(v==confirm){
           String nameText= name.getText().toString();
            String phoneT=phone.getText().toString();
            String ident=iden.getText().toString();
            if(nameText.length()==0||phoneT.length()==0||ident.length()==0){
                CommonUtils.toast("请完整填写信息",this);
                return ;
            }
            Intent i=getIntent();
            i.putExtra("name",nameText);
            i.putExtra("phone",phoneT);
            i.putExtra("iden",ident);
            setResult(1, i);
            finish();
            return ;
        }else if(id==R.id.identity){
            final List<Map<String,String>> list=dapter.idenList();
            String title=((TextView) ((ViewGroup) v).getChildAt(0)).getText().toString();
            LVDialog.getLvDialogNShow(this, list, title.substring(0, title.length() - 1),null, new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) ((ViewGroup) vg).getChildAt(1)).setText(list.get(position).get("name"));
                }
            });

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WorkUnit person = dapter.infolist.get(position);
        this.name.setText(person.getWuName());
        this.iden.setText(person.getIden());
        this.phone.setText(person.getTel());
    }
}




