package cn.swsk.rgyxtqapp;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.Pop1Action;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class ExportManageActivity extends AnquanQRBaseActivity implements View.OnClickListener {

    private Button save;
    private String name;
    private String iden;
    private String phone;
    private RadioGroup rg;
    private String subpath;
    private Pop1Action pop1;
    private Dialog lvDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.exportmanagementa_ctivity;
        super.onCreate(savedInstanceState);

        rg = (RadioGroup) findViewById(R.id.rg01);
        rg.findViewById(R.id.rb01).setOnClickListener(this);
        rg.findViewById(R.id.rb03).setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        pop1 = new Pop1Action(this, (TextView) rg.findViewById(R.id.rb02));
        pop1.cb = new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                subpath = "loginName=" +
                        CommonUtils.UrlEnc(PushUtils.getUserName())
                        + "&type=2&targetWareHouse=" + CommonUtils.UrlEnc(resp);
//                            saveDraw(subpath);
            }
        };
    }


    @Override
    public void onClick(View v) {
        if (v == save) {
            saveDraw(subpath);
        } else if (v.getId() == R.id.rb01) {
            Intent intent = new Intent();
            intent.setClass(this, DrawActivity.class);
            intent.putExtra("title", "领取");
            intent.putExtra("name", name);
            intent.putExtra("iden", iden);
            intent.putExtra("phone", phone);
            startActivityForResult(intent, 2);

        } else if (v.getId() == R.id.rb03) {
            toggleLvDialog();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == 1) {
            name = data.getStringExtra("name");
            iden = data.getStringExtra("iden");
            phone = data.getStringExtra("phone");
            subpath = "loginName=" + CommonUtils.UrlEnc(PushUtils.getUserName())
                    + "&type=1&applicantName=" + CommonUtils.UrlEnc(name) + "&applicantTel="
                    + phone + "&applicantRole=" + CommonUtils.UrlEnc(iden);
//            saveDraw(subpath);

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveDraw(String subpath) {
        if (subpath == null) {
            CommonUtils.toast("请完善提交信息", this);
            return;
        }

        String path1 = "rgyx/AppManageSystem/delivery?";

        save(PushUtils.getServerIP(this) + path1 + subpath);

    }


    private int lvselidx=-1;
    private void toggleLvDialog() {
        if (lvDialog == null) {
            final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Resources res= this.getResources();
            String ary[] = this.getResources().getStringArray(R.array.equipproducer);
            for(int i=0;i<ary.length;i++){
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",ary[i]);
                list.add(map);
            }

            lvDialog = LVDialog.getLvDialogNShow(this, list, "请选择目标厂家",
                    new CommonAdapter<Map<String,String>>(this,list,R.layout.item4lv_onerow01) {
                @Override
                public void convert(ViewHolder holder, Map<String, String> stringStringMap) {
                    holder.setText(android.R.id.text1,stringStringMap.get("name"));
                    if(holder.getPosition()==lvselidx){
                        holder.setBackgroundColor(android.R.id.text1,0xeeeeeeee);
                    }else{
                        holder.setBackgroundColor(android.R.id.text1,0xffffffff);

                    }
                }
            }, new
                    AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long
                                id) {
                            lvselidx=position;
                            subpath = "loginName=" +
                                    CommonUtils.UrlEnc(PushUtils.getUserName())
                                    + "&type=3&targetmanufactor=" + CommonUtils.UrlEnc(list.get
                                    (position).get("name"));
//                            saveDraw(subpath);
                        }
                    });
            lvDialog.dismiss();
        }
        if (lvDialog.isShowing()){
            lvDialog.dismiss();
        }else{
            lvDialog.show();
        }
    }
}
