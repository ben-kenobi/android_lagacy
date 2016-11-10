package fj.swsk.cn.eqapp.subs.collect.C;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.main.V.LVDialog;
import fj.swsk.cn.eqapp.subs.collect.Common.ImgPicker2;
import fj.swsk.cn.eqapp.subs.collect.M.Tscene;
import fj.swsk.cn.eqapp.subs.collect.M.TsceneProg;
import fj.swsk.cn.eqapp.subs.collect.V.DensityDescDialog;
import fj.swsk.cn.eqapp.subs.more.M.EQInfo;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.ResUtil;
import fj.swsk.cn.eqapp.util.SharePrefUtil;

/**
 * Created by apple on 16/7/8.
 */
public class EditSceneActivity extends BaseTopbarActivity implements View.OnClickListener, DensityDescDialog.ItemCB {
    private boolean isUploaded = false;
    private ImgPicker2 picker;
    TextView location;
    private Spinner level, summary;
    TextView eventid;
    private EditText detail;
    private CheckBox usesrc;
    private Tscene ts = CommonUtils.context.curscene;
    Dialog densityDescription;
    Button chooseeventid;
    Dialog lvDialog;
    int lvselidx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.editscene_activity;
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        detail = (EditText) findViewById(R.id.detail);
        detail.setText(ts.detail);
        location = (TextView) findViewById(R.id.location);
        level = (Spinner) findViewById(R.id.level);
        summary = (Spinner) findViewById(R.id.summary);
        eventid = (TextView) findViewById(R.id.eventid);
        chooseeventid = (Button) findViewById(R.id.chooseevenid);
        picker = new ImgPicker2(this, (GridView) findViewById(R.id.gv01));
        usesrc = (CheckBox) findViewById(R.id.usesrc);

        Drawable ld = getResources().getDrawable(R.mipmap.location);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        location.setCompoundDrawables(ld, null, null, null);
        location.setText("地点:" + String.format("%.2f", ts.loc_lon) +
                " , " + String.format("%.2f", ts.loc_lat) + "   " + IConstants.TIMESDF.format(ts.addtime));

        ld = getResources().getDrawable(android.R.drawable.ic_menu_camera);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button) findViewById(R.id.shoot)).setCompoundDrawables(ld, null, null, null);

        ld = getResources().getDrawable(android.R.drawable.presence_video_online);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button) findViewById(R.id.record)).setCompoundDrawables(ld, null, null, null);

        ld = getResources().getDrawable(android.R.drawable.ic_menu_crop);
        ld.setBounds(0, 0, ResUtil.dp2Intp(25), ResUtil.dp2Intp(25));
        ((Button) findViewById(R.id.pick)).setCompoundDrawables(ld, null, null, null);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.eqlevel, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapter);
        level.setSelection(ts.eqlevelidx);
        adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, android.R.id.text1,
                getResources().getStringArray(R.array.eqsummary));
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        summary.setAdapter(adapter);
        summary.setSelection(ts.summaryidx);
        String js = SharePrefUtil.getEQInfo();
        JsonTools.getInstance(js, new TypeReference<EQInfo>() {}) ;
        if (TextUtils.isEmpty(js)) {
            EQInfo e=JsonTools.getInstance(js, new TypeReference<EQInfo>() {}) ;
            if(e!=null)
                eventid.setText(EQInfo.getIns().description());
            else
                eventid.setText("");
        } else {
            eventid.setText("");
        }


        mTopbar.rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditSceneActivity.this);
                builder.setMessage("直接提交或保存");
                builder.setTitle("提示");
                builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if ("-1".equals(ts.event_id)) {
                            CommonUtils.toast("无对应地震信息，请先保存");
                            return;
                        }
                        save();
                        upload2();

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
        } else if (id == R.id.shoot) {
            picker.startCamera();
        } else if (id == R.id.record) {
            picker.startRecorder();
        } else if (id == R.id.densitydescription) {
            createNshowDensityDescription();
        } else if (id == R.id.chooseevenid) {
            toggleLvDialog();
        }
    }


    void createNshowDensityDescription() {
        if (densityDescription == null) {
            densityDescription = new DensityDescDialog(this);
        }
        densityDescription.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        picker.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void save() {
        ts.eqlevelidx = level.getSelectedItemPosition();
        ts.summaryidx = summary.getSelectedItemPosition();
        ts.detail = detail.getText().toString();
        ts.insertOrUpdate();

    }


    public File convertFile(File file) {
        if (usesrc.isChecked() || file.getName().endsWith("mp4")) {
            return file;
        }
        File newfile = new File(CommonUtils.context.getFilesDir(), IConstants.SMALLFILEPREFIX + file.getName());
//        BitmapFactory.Options opt = new BitmapFactory.Options();
//        opt.inSampleSize=1;

        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
        bm = CommonUtils.resizeBitmap(bm, 1000);
        CommonUtils.log(bm.getWidth() + "====" + bm.getHeight());
        try {
            bm.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(newfile));
            if (!bm.isRecycled())
                bm.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newfile;

    }


    public void upload2() {

        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final TsceneProg tp = new TsceneProg();
                tp._id = ts._id;
                tp.compressing = true;
                tp.describe = "正在压缩。。。";
                CommonUtils.context.uploadingMap.put(tp._id, tp);
                final Map<String, File> files = new HashMap<>();
                for (File file : ts.list) {
                    files.put(file.getName(), convertFile(file));
                }

                IConstants.MAIN_HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        doupload2(files, tp);
                    }
                });
            }
        });
        finish();
        Intent intent = new Intent(this, PendingSubmissionActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }


    public void doupload2(Map<String, File> files, final TsceneProg tp) {


        if (!HttpUtils.isNetworkAvailable(this)) {
            tp.state = 2;
            tp.describe = "网络不可用，请重新上传。。。";
            return;
        }

        String name = PushUtils.getToken();

        Map<String, String> map1 = new HashMap<>();
        map1.put("intensityLevel", level.getSelectedItemPosition()+1+"");
        map1.put("shortDescription", summary.getSelectedItem().toString());
        map1.put("detailsDescription", ts.detail);
        map1.put("lon", ts.loc_lon + "");
        map1.put("lat", ts.loc_lat + "");
        map1.put("createtime", IConstants.TIMESDF.format(ts.addtime));
        map1.put("seismNo", ts.event_id);

        String path = IConstants.uploadUrl + "?token=" + CommonUtils.UrlEnc(name);

        NetUtil.commonRequest6(tp, path, this, map1, files, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int type) {
                if (type != 0) {
                    tp.state = 2;
//                    tp.describe="上传失败，请重新上传。。。";
                } else {
                    tp.state = 1;
                    tp.describe = "上传成功!";
                    afterUpload();


                }
            }
        });


    }


    private void toggleLvDialog() {
        if (lvDialog == null) {
            String path = IConstants.eqHisUrl + "";

            NetUtil.commonRequest(this, path, new NetUtil.RequestCB() {
                @Override
                public void cb(Map<String, Object> map, String resp, int type) {
                    if (type != 0) return;
                    List<Map<String, Object>> li = (List<Map<String, Object>>) map.get("data");
                    final List<EQInfo> list = new ArrayList<>();
                    for (Map<String, Object> m : li) {
                        EQInfo ei = JsonTools.getInstance(JsonTools.getJsonString(m),
                                new TypeReference<EQInfo>() {
                                });
                        ei.hasLayer = true;
                        list.add(ei);
                    }
                    IConstants.MAIN_HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            lvDialog = LVDialog.getLvDialogNShow(EditSceneActivity.this, null, "手动选择地震",
                                    new CommonAdapter<EQInfo>(EditSceneActivity.this, list, R.layout.item4lv_onerow01) {
                                        @Override
                                        public void convert(ViewHolder holder, EQInfo info) {
                                            holder.setText(android.R.id.text1, info.description());
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
                                                    eventid.setText(list.get(position).description());
                                                    ts.event_id=list.get(position).obsTime+"";
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


    public void upload() {
        final ProgressDialog pd = DialogUtil.createProgressDialogNshow("", "", false, this);
        IConstants.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<String, File> files = new HashMap<>();
                for (File file : ts.list) {
//                   CommonUtils.log(file.getName()+"================");
                    files.put(file.getName(), convertFile(file));
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


    public void doupload(Map<String, File> files) {


        if (!HttpUtils.isNetworkAvailable(this)) {
            CommonUtils.toast("当前网络不可用，请先保存");
            return;
        }

        String name = PushUtils.getToken();

        Map<String, String> map1 = new HashMap<>();
        map1.put("intensityLevel", level.getSelectedItem().toString());
        map1.put("shortDescription", summary.getSelectedItem().toString());
        map1.put("detailsDescription", ts.detail);
        map1.put("lon", ts.loc_lon + "");
        map1.put("lat", ts.loc_lat + "");
        map1.put("createtime", IConstants.TIMESDF.format(ts.addtime));
        map1.put("seismNo", ts.event_id);

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


    private void afterUpload() {
        ts.uploaded(usesrc.isChecked());
        finish();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    private void updateUI() {
        boolean editable = ts.flag == 0;
        mTopbar.setRightButtonIsvisable(editable);
        level.setEnabled(editable);
        summary.setEnabled(editable);
        summary.setEnabled(editable);
        detail.setEnabled(editable);
        findViewById(R.id.operationbar).setVisibility(editable ? View.VISIBLE : View.GONE);
        findViewById(R.id.densitydescription).setVisibility(editable ? View.VISIBLE : View.GONE);
        picker.gv.setEnabled(editable);
        usesrc.setEnabled(editable);
        usesrc.setText(editable ? "使用原图" : "原图");
        usesrc.setChecked("1".equals(ts.remark));
    }


    @Override
    public void onItemClick(int pos) {
        level.setSelection(pos);
    }
}
