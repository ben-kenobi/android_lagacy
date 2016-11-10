package cn.swsk.rgyxtqapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;

import cn.swsk.rgyxtqapp.adapter.AnquanItemsAdapter;
import cn.swsk.rgyxtqapp.bean.TEquip;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;

/**
 * Created by apple on 16/3/3.
 */
public class AnquanChangeStatus extends AnquanguanliBaseActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener{


    private ListView lv;
    private AnquanItemsAdapter dapter;
    private Button save,scan;
    private RadioGroup rg;

    @Override
    public void preCreate() {
        resid=R.layout.anquan_changestatus;
    }

    @Override
    public void anaCreate() {
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(dapter=new AnquanItemsAdapter(this,lv));
        save= (Button)findViewById(R.id.save);
        save.setOnClickListener(this);
        scan=(Button)findViewById(R.id.scan);
        scan.setOnClickListener(this);
        rg=(RadioGroup)findViewById(R.id.rg01);
        rg.setOnCheckedChangeListener(this);

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(checkedId==R.id.rb01){
            dapter.setInfos(new ArrayList<TEquip>(),0);
        }else if (checkedId == R.id.rb02||checkedId == R.id.rb03){
            final int type = checkedId==R.id.rb02?1:2;
            final ProgressDialog pd= DialogUtils.createProgressDialogNshow("", "", true, this);
            CommonUtils.THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {

                    String str = HttpUtils.getJsonContent("https://www.baidu.com/?type=" + type);
                    Log.e("itag", str);
                    CommonUtils.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {

                            pd.dismiss();

                            dapter.setInfos(Arrays.asList(new TEquip[]{TEquip.defaul("1"),
                                    TEquip.defaul("2"), TEquip.defaul("3"), TEquip.defaul("4"),
                                    TEquip.defaul("5"), TEquip.defaul("6")}), type);


                        }
                    });


                }
            });




        }
    }



    @Override
    public void onClick(View v) {
        if(v==this.save){
            final ProgressDialog pd= DialogUtils.createProgressDialogNshow("", "", true, this);
            CommonUtils.THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes= JsonTools.getJsonBytes(dapter.getCheckedInfos());
                    if(bytes==null||bytes.length==0){
                        CommonUtils.MAIN_HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Dialog dialog=DialogUtils.getMessageDialogBuilder(AnquanChangeStatus.this,"error",
                                        "",new  DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();

                            }
                        });
                        return ;
                    }
                    String str= HttpUtils.postJsonContent("https://www.baidu.com/", bytes);
                    Log.e("itag", str);
                    CommonUtils.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.toast("保存成功",AnquanChangeStatus.this);
                            dapter.removeCheckedInfos();
                            pd.dismiss();
                        }
                    });


                }
            });




        }else if (v==scan){
            Intent intent = new Intent();
            intent.setClass(this, ScanQRCode.class);
            intent.putExtra("title", "扫描二维码");
            startActivityForResult(intent, 1);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ProgressDialog pd= DialogUtils.createProgressDialogNshow("", "", true, this);
        CommonUtils.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {

                String str = HttpUtils.getJsonContent("https://www.baidu.com/?type=" + 1);
                Log.e("itag", str);
                CommonUtils.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {

                        pd.dismiss();


                        dapter.addInfos(Arrays.asList(new TEquip[]{TEquip.defaul("1"),
                                TEquip.defaul("3"), TEquip.defaul("6")}));


                    }
                });


            }
        });

        if(requestCode==1&&resultCode==1){


        }
    }
}
