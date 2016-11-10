package fj.swsk.cn.eqapp.subs.collect.C;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.subs.collect.Common.ImgPicker2;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.ResUtil;

/**
 * Created by apple on 16/7/8.
 */
public class EditSceneActivity extends BaseTopbarActivity implements View.OnClickListener{
    private boolean isUploaded = false;
    private ImgPicker2 picker;
    TextView location;
    private Spinner level,summary;
    private EditText detail;
    private CheckBox usesrc;
    private Tscene ts = CommonUtils.context.curscene;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.editscene_activity;
        super.onCreate(savedInstanceState);
        detail = (EditText) findViewById(R.id.detail);
        detail.setText(ts.detail);
        location = (TextView) findViewById(R.id.location);
        level = (Spinner) findViewById(R.id.level);
        summary = (Spinner) findViewById(R.id.summary);
        picker = new ImgPicker2(this, (GridView) findViewById(R.id.gv01));
        usesrc=(CheckBox)findViewById(R.id.usesrc);

        Drawable ld=getResources().getDrawable(R.mipmap.location);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        location.setCompoundDrawables(ld, null, null, null);
        location.setText("地点:" + String.format("%.2f", ts.loc_lon) +
                " , " + String.format("%.2f", ts.loc_lat) + "   " + IConstants.TIMESDF.format(ts.addtime));

        ld=getResources().getDrawable(android.R.drawable.ic_menu_camera);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button)findViewById(R.id.shoot)).setCompoundDrawables(ld, null, null, null);

        ld=getResources().getDrawable(android.R.drawable.presence_video_online);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button)findViewById(R.id.record)).setCompoundDrawables(ld,null,null,null);

        ld=getResources().getDrawable(android.R.drawable.ic_menu_crop);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button) findViewById(R.id.pick)).setCompoundDrawables(ld, null, null, null);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.eqlevel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapter);
        level.setSelection(ts.eqlevelidx);
        adapter = ArrayAdapter.createFromResource(this, R.array.eqsummary, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        summary.setAdapter(adapter);
        summary.setSelection(ts.summaryidx);

        mTopbar.rightListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSceneActivity.this);
                builder.setMessage("直接提交或保存");
                builder.setTitle("提示");
                builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        save();
                        upload();

                    }
                });
                builder.setNegativeButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        save();
                        finish();
                    }
                });
                builder.create().show();
            }
        };

        updateUI();

    }




    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.pick) {
            picker.startPick();
        } else if (id==R.id.shoot) {
            picker.startCamera();
        }else if(id==R.id.record){
            picker.startRecorder();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picker.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void save(){
        ts.eqlevelidx=level.getSelectedItemPosition();
        ts.summaryidx=summary.getSelectedItemPosition();
        ts.detail=detail.getText().toString();
        ts.insertOrUpdate();

    }







    public File convertFile(File file){
        if(usesrc.isChecked()||file.getName().endsWith("mp4")){
            return file;
        }
        File  newfile = new File(CommonUtils.context.getFilesDir(),IConstants.SMALLFILEPREFIX+file.getName());
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inSampleSize=1;

        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        bm=CommonUtils.resizeBitmap(bm, 2000);
        CommonUtils.log(bm.getWidth()+"===="+bm.getHeight());
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(newfile));
            if(!bm.isRecycled())
                bm.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return newfile;

    }
    public void upload(){
        final ProgressDialog pd = DialogUtil.createProgressDialogNshow("", "", false, this);
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<String,File> files = new HashMap<>();
                for(File file:ts.list){
//            CommonUtils.log(file.getName()+"================");

                    files.put(file.getName(),convertFile(file));
                }

                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        doupload(files);
                    }
                });
            }
        });


    }


    public void doupload( Map<String,File> files)  {
        String name = PushUtils.getToken();

        Map<String, String> map1 = new HashMap<>();
        map1.put("intensityLevel", level.getSelectedItem().toString());
        map1.put("shortDescription", summary.getSelectedItem().toString());
        map1.put("detailsDescription",ts.detail);
        map1.put("lon", ts.loc_lon+"");
        map1.put("lat", ts.loc_lat+"");
        map1.put("createtime", IConstants.TIMESDF.format(ts.addtime));
        map1.put("eventid", ts.event_id);

        String path = IConstants.uploadUrl + "?token=" + CommonUtils.UrlEnc(name);


        NetUtil.commonRequest5(path, this, map1, files, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if (type != 0) {
                    CommonUtils.toast("上传失败，请重新上传");
                } else {
                    new AlertDialog.Builder(EditSceneActivity.this).setTitle("上传成功！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    afterUpload();
                                }
                            }).show();
                }
            }
        });

    }


    private void afterUpload(){
        ts.uploaded(usesrc.isChecked());
        finish();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void  updateUI(){
       boolean editable = ts.flag==0;
        mTopbar.setRightButtonIsvisable(editable);
       level.setEnabled(editable);
       summary.setEnabled(editable);
       detail.setEnabled(editable);
       findViewById(R.id.operationbar).setVisibility(editable ? View.VISIBLE : View.GONE);
       picker.gv.setEnabled(editable);
        usesrc.setEnabled(editable);
        usesrc.setText(editable ? "使用原图":"原图");
        usesrc.setChecked("1".equals(ts.remark));



    }

}
