package cn.swsk.rgyxtqapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.codehaus.jackson.type.TypeReference;

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
import cn.swsk.rgyxtqapp.custom.SlideupCover;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class InventoryManageActivity extends AnquanguanliBaseActivity implements View
        .OnClickListener {
    private Button save, newinventory;
    private TextView datetv;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private ListView mLv;
    private SlideupCover cover;
    private EditText reckonertv,logsubjtv;
    private Pop1Action pop1;
    private TextView wareHouse, area;
    private CommonAdapter<TInventory> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.inventorymanage_activity;
        super.onCreate(savedInstanceState);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        newinventory = (Button) findViewById(R.id.newinventory);
        newinventory.setOnClickListener(this);
        datetv = (TextView) findViewById(R.id.datetv);
        datetv.setText(sdf.format(new Date()));
        logsubjtv = (EditText) findViewById(R.id.logsubjtv);
        reckonertv = (EditText) findViewById(R.id.reckonertv);
        mLv = (ListView) findViewById(R.id.lv);
        mLv.setAdapter(adapter=new CommonAdapter<TInventory>(this, new ArrayList<TInventory>()
                , R.layout.item4lv_anquaninventory) {
            @Override
            public void convert(ViewHolder holder, TInventory in) {
                holder.setText(R.id.tv01, in.getRegDate());
                holder.setText(R.id.tv02, in.getTitle());
                holder.setText(R.id.tv03, in.getOperator());
                holder.setTag(R.id.tv04, holder.getPosition());
                holder.setOnClickListener(R.id.tv04, InventoryManageActivity.this);
                holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0 ?
                        0xffffffff : 0xfff5f5f5);
            }
        });
        wareHouse = (TextView) findViewById(R.id.warehousetv);
        area = (TextView) findViewById(R.id.areatv);
        pop1 = new Pop1Action(this, (View) findViewById(R.id.warehouse));
        pop1.cb = new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                wareHouse.setText(resp);
                area.setText(pop1.getSelectedRegion().getRegionName());
            }
        };
        findViewById(R.id.warehouse).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (v == this.save) {
             String path = PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/inventoryManager?";
            StringBuffer sb = new StringBuffer();
            sb.append("regDate="+datetv.getText().toString()+"&");
            sb.append("title="+CommonUtils.UrlEnc(logsubjtv.getText().toString())+"&");
            sb.append("wareHouse="+CommonUtils.UrlEnc(wareHouse.getText().toString())+"&");
            sb.append("operator="+CommonUtils.UrlEnc(reckonertv.getText().toString())+"&");
            path+=sb.toString();
            HttpUtils.commonRequest(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    List<TInventory> list = JsonTools.getInstance(JsonTools.getJsonStr(map.get
                            ("inventorys")), new
                            TypeReference<List<TInventory>>() {
                            });
                    adapter.setDatas(list);


                }
            });


        } else if (v == this.newinventory) {
            Intent intent = new Intent();
            intent.setClass(this, AddInventoryActivity.class);
            intent.putExtra("title", "盘点单新增");
            startActivity(intent);
        }  else if (id == R.id.tv04) {
            int tag = (Integer) v.getTag();
            List<TInventoryDetail> ary = adapter.getItem(tag).gettInventoryDetail();
            toggleCover(ary);

        }else if (id == R.id.warehouse) {
            CommonUtils.hideSoftInput(v, this);
            pop1.showAsDropDown();
        }  else if (id == R.id.date) {
            Calendar cal = Calendar.getInstance();

            DatePickerDialog dd = new DatePickerDialog(this, android.R.style
                    .Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    datetv.setText(year + "-" + (monthOfYear + 1) + "-" + (dayOfMonth));
                }
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
            dd.show();

        }

    }


    public View toggleCover(List<TInventoryDetail> ary) {
        if (cover == null) {
            cover = new SlideupCover(this,R.layout.inventory_showpop);
        }
        cover.toggle(ary,new CommonAdapter<TInventoryDetail>(this, ary, R.layout.item4lv_inventoryitem) {
            @Override
            public void convert(ViewHolder holder, TInventoryDetail d) {
                int iary[]={Integer.valueOf(d.getPaperQty()),d.getRealQty(),d.getReturnQty(),0,0};
                int total = iary[0] + iary[2];
                iary[3] = iary[1] - total;
                iary[4] = total - iary[1];
                if(iary[3]<0)
                    iary[3]=0;
                if(iary[4]<0)
                    iary[4]=0;
                for (int i = 1; i <= 5; i++) {
                    holder.setText(getResources().getIdentifier("tv0" + i, "id",
                            getPackageName()), iary[i-1]+"");
                    holder.getConvertView().setBackgroundColor(holder.getPosition() % 2 == 0
                            ? 0xffffffff : 0xfff5f5f5);
                }
            }
        });

        return cover;
    }
}




