package cn.swsk.rgyxtqapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.Pop1Action;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.IConstants;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * Created by apple on 16/2/23.
 */
public class ExportManageActivity extends AnquanQRBaseActivity implements View.OnClickListener {

    private Button save;
    private String name;
    private String iden;
    private String phone;
    private LinearLayout rg;
    private String subpath;
    private String saveType;
    private Pop1Action pop1;
    private Dialog lvDialog;
    private RadioButton rbs[] = new RadioButton[3];

    {
        dataObs = new Observer() {
            @Override
            public void update(Observable observable, Object data) {


            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.resid = R.layout.exportmanagementa_ctivity;
        super.onCreate(savedInstanceState);

        rg = (LinearLayout) findViewById(R.id.rg01);
        rg.findViewById(R.id.rl01).setOnClickListener(this);
        rg.findViewById(R.id.rl03).setOnClickListener(this);
        rbs[0] = (RadioButton) rg.findViewById(R.id.rb01);
        rbs[1] = (RadioButton) rg.findViewById(R.id.rb02);
        rbs[2] = (RadioButton) rg.findViewById(R.id.rb03);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        pop1 = new Pop1Action(this, rg.findViewById(R.id.rl02));
        pop1.cb = new HttpUtils.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                subpath = "loginName=" +
                        CommonUtils.UrlEnc(PushUtils.getUserName())
                        + "&type=2&targetWareHouse=" + CommonUtils.UrlEnc(resp);
//                            saveDraw(subpath);
                saveType="移库";

                setCheckedText(R.id.rb02tv1, resp);
            }
        };
        pop1.trigClickListener = this;
        if (PushUtils.getUserName().endsWith("省库")) {
            checkRB(1);
            findViewById(R.id.rl01).setEnabled(false);
        }
    }


    @Override
    public void onClick(View v) {
        if (v == save) {
            saveDraw(subpath,saveType);
        } else if (v.getId() == R.id.rl01) {
            checkRB(0);
            Intent intent = new Intent();
            intent.setClass(this, DrawActivity.class);
            intent.putExtra("title", "领取");
            intent.putExtra("name", name);
            intent.putExtra("iden", iden);
            intent.putExtra("phone", phone);
            startActivityForResult(intent, 2);


        } else if (v.getId() == R.id.rl03) {
            toggleLvDialog();
            checkRB(2);
        } else if (v.getId() == R.id.rl02) {
            checkRB(1);
        }
    }

    private void setCheckedText(int id, String text) {
        TextView tv = (TextView) rg.findViewById(id);
        tv.setVisibility(View.VISIBLE);
        tv.setText(text);
    }


    private void checkRB(int idx) {
        for (int i = 0; i < rbs.length; i++) {
            if (i == idx)
                rbs[i].setChecked(true);
            else
                rbs[i].setChecked(false);
        }
        updateState();
    }

    @Override
    protected void afterAddTequip(List<TEquip> list) {
        boolean hastoast = false;
        for (TEquip eq : list) {
            boolean b = "1".equals(eq.getEstatus()) && (eq.getStatus() == 0);
            b &= PushUtils.getUserName().equals(eq.getWareHouse());
            eq.setCheckstate(b ? 1 : 2);
            if (!b && !hastoast) {
//                CommonUtils.toast("装备不是在库状态", this);
                hastoast = true;
            }
        }
        updateState();
    }

    private void updateState() {
        if (rbs[0].isChecked()) {
            boolean toast=false;
            for (TEquip equip : dapter.infolist) {
                if ("1".equals(equip.getEstatus()) && equip.getStatus() == 7&&equip.getCheckstate()==1) {
                    equip.setCheckstate(2);
                    if(!toast){
                        toast=true;
                        CommonUtils.toast("无法分发故障归还的装备",this);
                    }

                }
            }

        }else if(rbs[1].isChecked()){
            for (TEquip eq : dapter.infolist) {
                boolean b = "1".equals(eq.getEstatus()) && (eq.getStatus() == 0);
                b &= PushUtils.getUserName().equals(eq.getWareHouse());
                eq.setCheckstate(b ? 1 : 2);
            }
        }else{
            for (TEquip eq : dapter.infolist) {
                boolean b = "1".equals(eq.getEstatus()) ;
                b &= PushUtils.getUserName().equals(eq.getWareHouse());
                eq.setCheckstate(b ? 1 : 2);
            }
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
            saveType="分发";

//            saveDraw(subpath);
            setCheckedText(R.id.rb01tv1, name);

        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveDraw(String subpath,String saveType) {
        if (subpath == null) {
            CommonUtils.toast("请完善提交信息", this);
            return;
        }

        String path1 = "rgyx/AppManageSystem/delivery?";

        save(PushUtils.getServerIP(this) + path1 + subpath,saveType);

    }


    private int lvselidx = -1;

    private void toggleLvDialog() {
        if (lvDialog == null) {
            String path = PushUtils.getServerIP(this) +
                    "rgyx/AppManageSystem/findManuFacturer?";
            HttpUtils.commonRequest2(path, this, new HttpUtils.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    final List<Map<String, String>> list = (List<Map<String, String>>) (List) JsonTools.getListMap("", resp);
                    IConstants.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            lvDialog = LVDialog.getLvDialogNShow(ExportManageActivity.this, list, "请选择目标厂家",
                                    new CommonAdapter<Map<String, String>>(ExportManageActivity.this, list, R.layout.item4lv_onerow01) {
                                        @Override
                                        public void convert(ViewHolder holder, Map<String, String> stringStringMap) {
                                            holder.setText(android.R.id.text1, stringStringMap.get("manuFacturer"));
                                            if (holder.getPosition() == lvselidx) {
                                                holder.setBackgroundColor(android.R.id.text1, 0xeeeeeeee);
                                            } else {
                                                holder.setBackgroundColor(android.R.id.text1, 0xffffffff);

                                            }
                                        }
                                    }, new
                                            AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long
                                                        id) {
                                                    lvselidx = position;
                                                    subpath = "loginName=" +
                                                            CommonUtils.UrlEnc(PushUtils.getUserName())
                                                            + "&type=3&targetmanufactor=" + CommonUtils.UrlEnc(list.get
                                                            (position).get("manuFacturer"));
                                                    saveType="退回厂家";
                                                    setCheckedText(R.id.rb03tv1, list.get(position).get("manuFacturer"));
//                            saveDraw(subpath);
                                                }
                                            });
                        }
                    });
                }
            });


        } else if (lvDialog.isShowing()) {
            lvDialog.dismiss();
        } else {
            lvDialog.show();
        }
    }
}
