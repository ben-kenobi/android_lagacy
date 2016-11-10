package swsk.cn.rgyxtq.subs.work.C;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.main.Common.JsonTools;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.main.Common.Topbar;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;
import swsk.cn.rgyxtq.subs.work.M.WorkPlan;
import swsk.cn.rgyxtq.util.DialogUtil;
import swsk.cn.rgyxtq.util.NetworkUtils;
import swsk.cn.rgyxtq.util.SystemUtil;

/**
 * Created by apple on 16/3/1.
 */
public class WorkInfoUploadActivity extends Activity implements DialogInterface.OnClickListener{
    private GridView gvImage; //导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter;//适配器
    private String pathImage = null;//图片路径

    // private Spinner famc;
    private List<WorkPlan> mDatas;

    private static final int PHOTO_CARMERA = 1;
    private static final int PHOTO_PICK = 2;

    private HttpUtils httpUtils;
    private ProgressDialog myDialog;

    private int mPosition;

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private String workPlanId;
    private boolean timeSelected = false;
    private boolean dateSelected = false;

    private TextView tv_plan_name; //方案名称

    private EditText etHJDYL, etYTYL, etZYRQ, etZYKSSJ, etZYJSSJ,
            etZYMJ, etZYZDBH, etGPSJD, etGPSWD, etZYQJLX, etGSPDYL, etQTYL,
            etYJ, etFW, etKY, etTBR, etTBDW, etTXRQ, etBZ;

    private Spinner snrZYLX, zyqtqqk, zyhtqqk, zyxg, dmfx;

    private boolean isUploaded = false;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private List<File> tempFiles = new ArrayList<>();

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("'PNG'_yyyyMMdd_HHmmss");
        return sdf.format(date) + ".png";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workinfo_upload);
        initView();

        Topbar mTopbar = (Topbar) findViewById(R.id.workInfoTopbar);
        //mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new Topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                if (snrZYLX.getSelectedItem().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请选择作业类型。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etHJDYL.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入火箭弹用量。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etYTYL.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入烟条用量。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etZYRQ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业日期。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etZYKSSJ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业开始时间。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etZYJSSJ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业结束时间。", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isUploaded) {
                    isUploaded = true;
                    boolean isHas = NetworkUtils.isNetworkAvailable(WorkInfoUploadActivity.this);
                    if (isHas == false) {
                        Toast.makeText(WorkInfoUploadActivity.this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
                        isUploaded = false;
                        return;
                    }
                    myDialog = ProgressDialog.show(WorkInfoUploadActivity.this, "提示",
                            "正在提交中，请稍等...", true);
                    myDialog.show();
                    upload();
                }

            }
        });
        Intent intent = getIntent();
        mTopbar.setTitleText(getString(R.string.lbl_work_info_submit));
        workPlanId = intent.getStringExtra("workPlanId");
        tv_plan_name.setText(intent.getStringExtra("workPlanName"));

        /*
         * 防止键盘挡住输入框
         * 不希望遮挡设置activity属性 android:windowSoftInputMode="adjustPan"
         * 希望动态调整高度 android:windowSoftInputMode="adjustResize"
         */
        getWindow().setSoftInputMode(WindowManager.LayoutParams.
                SOFT_INPUT_ADJUST_PAN);
        //锁定屏幕
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gvImage = (GridView) findViewById(R.id.gvImage);

        Bitmap addImage = BitmapFactory.decodeResource(getResources(), R.mipmap.add_image); //加号;

        imageItem = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("itemImage", addImage);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this,
                imageItem, R.layout.add_picture_gridview_item,
                new String[]{"itemImage"}, new int[]{R.id.imageView1});

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gvImage.setAdapter(simpleAdapter);

        gvImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    showImagePickDialog();
                } else {
                    mPosition = position;
                    deleteIMG(position);
                }
            }
        });

        httpUtils = new HttpUtils(100000);


        NetUtil.commonRequest(this, "rgyx/appWorkInfoManage/findWorkPlanInfo?token=" + PushUtils.token + "&id=" + workPlanId,
                new NetUtil.RequestCB() {
                    @Override
                    public void cb(Map<String, Object> map) {
                        List<WorkPlan> datas = new ArrayList<WorkPlan>();

                        Map<String, Object> workPlanInfo = (Map<String, Object>) map.get("workPlanInfo");

//                String workType = "";
//                if (workPlanInfo.get("WORK_TYPE") != null && workPlanInfo.get("WORK_TYPE") == 1) {
//                    workType = "增雨";
//                } else if (workPlanInfo.get("WORK_TYPE") != null && workPlanInfo.get("WORK_TYPE") == 2) {
//                    workType = "防雹";
//                }
                        if (workPlanInfo.get("WORK_TYPE") != null){
                            snrZYLX.setSelection(Integer.parseInt(workPlanInfo.get("WORK_TYPE").toString()) - 1);
                        }
                        etHJDYL.setText(workPlanInfo.get("ROCKET_NUM") == null ? "" : workPlanInfo.get("ROCKET_NUM").toString());
                        etYTYL.setText(workPlanInfo.get("SMOKE_NUM") == null ? "" : workPlanInfo.get("SMOKE_NUM").toString());
                        etZYZDBH.setText(workPlanInfo.get("WORK_POINT_NO") == null ? "" : workPlanInfo.get("WORK_POINT_NO").toString());
                        etGPSJD.setText(workPlanInfo.get("LON") == null ? "" : workPlanInfo.get("LON").toString());
                        etGPSWD.setText(workPlanInfo.get("LAT") == null ? "" : workPlanInfo.get("LAT").toString());
                        //作业器具类型
                        if(workPlanInfo.get("WORK_WARE_TYPE") != null){
                            switch (workPlanInfo.get("WORK_WARE_TYPE").toString()){
                                case "1": etZYQJLX.setText("火箭弹"); break;
                                case "2": etZYQJLX.setText("烟条"); break;
                                default:
                                    break;
                            }
                        }
                        //etZYQJLX.setText(workPlanInfo.get("WORK_WARE_TYPE") == null ? "" : workPlanInfo.get("WORK_WARE_TYPE").toString());
                        etYJ.setText(workPlanInfo.get("BEST_ELEVATION") == null ? "" : workPlanInfo.get("BEST_ELEVATION").toString());
                        etFW.setText(workPlanInfo.get("BEST_AZIMUTH") == null ? "" : workPlanInfo.get("BEST_AZIMUTH").toString());
                        etKY.setText(workPlanInfo.get("NOTIFICATI") == null ? "" : workPlanInfo.get("NOTIFICATI").toString());
                        etTBR.setText(PushUtils.userName);
                        etTBDW.setText(PushUtils.userUnit);
                    }
                });

    }

    private void initView() {
        //初始化下拉框
        // famc = (Spinner) findViewById(R.id.spinner_famc);
        //初始化控件
        tv_plan_name = (TextView)findViewById(R.id.tv_plan_name);

        zyqtqqk = (Spinner) findViewById(R.id.spinner_zyqtqqk);
        zyhtqqk = (Spinner) findViewById(R.id.spinner_zyhtqqk);
        zyxg = (Spinner) findViewById(R.id.spinner_zyxg);
        dmfx = (Spinner) findViewById(R.id.spinner_dmfx);

        snrZYLX = (Spinner) findViewById(R.id.spinner_zylx);
        etHJDYL = (EditText) findViewById(R.id.et_hjdyl);
        etYTYL = (EditText) findViewById(R.id.et_ytly);
        etZYRQ = (EditText) findViewById(R.id.et_zyrq);
        etZYKSSJ = (EditText) findViewById(R.id.et_zykssj);
        etZYJSSJ = (EditText) findViewById(R.id.et_zyjssj);
        etZYMJ = (EditText) findViewById(R.id.et_zymj);
        etZYZDBH = (EditText) findViewById(R.id.et_zybh);
        etGPSJD = (EditText) findViewById(R.id.et_gpsjd);
        etGPSWD = (EditText) findViewById(R.id.et_gpswd);
        etZYQJLX = (EditText) findViewById(R.id.et_zyqjlx);
        etGSPDYL = (EditText) findViewById(R.id.et_gsppdyl);
        etQTYL = (EditText) findViewById(R.id.et_qtyl);
        etYJ = (EditText) findViewById(R.id.et_yj);
        etFW = (EditText) findViewById(R.id.et_fw);
        etKY = (EditText) findViewById(R.id.et_ky);
        etTBR = (EditText) findViewById(R.id.et_tbr);
        etTBDW = (EditText) findViewById(R.id.et_tbdw);
        etTXRQ = (EditText) findViewById(R.id.et_tbrq);
        etBZ = (EditText) findViewById(R.id.et_bz);

       /* ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.workToolsType, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        famc.setAdapter(adapter2);*/


        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.workType, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        snrZYLX.setAdapter(adapter2);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.weatherAfterWork, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zyhtqqk.setAdapter(adapter2);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.weatherBeforeWork, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zyqtqqk.setAdapter(adapter2);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.effectWork, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zyxg.setAdapter(adapter2);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.surfaceWind, R.layout.tom_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dmfx.setAdapter(adapter2);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String date = simpleDateFormat.format(new java.util.Date());
//        EditText editText = (EditText) findViewById(R.id.et_tbrq);
//        editText.setText(date);

        //添加事件Spinner事件监听
        //workToolType.setOnItemSelectedListener(new SpinnerXMLSelectedListener());

        //设置默认值
        //workToolType.setVisibility(View.VISIBLE);

        EditText etWorkDate = (EditText) findViewById(R.id.et_zyrq);
        etWorkDate.setOnClickListener(new showDatePickerDialog());
        etWorkDate.setText(date);

        EditText etWriteDate = (EditText) findViewById(R.id.et_tbrq);
        etWriteDate.setOnClickListener(new showDatePickerDialog());
        etWriteDate.setText(date);

        EditText etBeginWork = (EditText) findViewById(R.id.et_zykssj);
        etBeginWork.setOnClickListener(new showTimePickerDialog());

        EditText etEndWork = (EditText) findViewById(R.id.et_zyjssj);
        etEndWork.setOnClickListener(new showTimePickerDialog());
    }



    private class showDatePickerDialog implements View.OnClickListener {
        public void onClick(View v) {
            if (dateSelected == true) {
                return;
            }
            dateSelected = true;
            final EditText editText = (EditText) v;
            String text = editText.getText().toString();
            if (text.equals("") || (mYear == 0 && mMonth == 0 && mDay == 0)) {
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
            }
            DatePickerDialog dialog =
                    new DatePickerDialog(WorkInfoUploadActivity.this, null, mYear, mMonth, mDay);
            dialog.setTitle(editText.getTag().toString());
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatePicker datePicker = ((DatePickerDialog) dialog).getDatePicker();
                    mYear = datePicker.getYear();
                    mMonth = datePicker.getMonth();
                    mDay = datePicker.getDayOfMonth();
                    editText.setText(mYear + "年" + (mMonth + 1) + "月" + mDay + "日");
                    dateSelected = false;
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dateSelected = false;
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private class showTimePickerDialog implements View.OnClickListener {
        public void onClick(View v) {
            final EditText editText = (EditText) v;
            String text = editText.getText().toString();
            if (text.equals("") || (mHour == 0 && mMinute == 0)) {
                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
            }
            TimePickerDialog dialog =
                    new TimePickerDialog(WorkInfoUploadActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            if (timeSelected == true) {
                                editText.setText(hourOfDay + "点" + minute + "分");
                            }
                        }
                    }, mHour, mMinute, true);
            dialog.setTitle(editText.getTag().toString());
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timeSelected = true;
                    dialog.dismiss();
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timeSelected = false;
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    // 调用拍照
                    File tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    tempFiles.add(tempFile);
                    SystemUtil.intent2Cam(this,PHOTO_CARMERA,Uri.fromFile(tempFile));
                    dialog.dismiss();
                    break;
                case 1:
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    tempFiles.add(tempFile);
                    // 调用相册
                    SystemUtil.intent2pick(this,PHOTO_PICK);
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }






    private void showImagePickDialog(){
        String title = "获取图片方式";
        String[] choices = new String[]{"拍照", "从手机中选择"};
        AlertDialog.Builder dialog = DialogUtil.getItemDialogBuilder(this, choices, title, this);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {
            return;
        }
        switch (requestCode) {
            case PHOTO_CARMERA:
                Uri uri = Uri.fromFile(tempFiles.get(tempFiles.size() - 1));
                pathImage = uri.getPath();
                break;
            case PHOTO_PICK:
                uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = getContentResolver().query(
                            uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    //光标移动至开头 获取图片路径
                    cursor.moveToFirst();
                    pathImage = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //按比例压缩图片
    private Bitmap getImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //保存到本地
        saveCropPic(bitmap);
        return bitmap;
    }


    // 把裁剪后的图片保存到sdcard上
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        try {
            fis = new FileOutputStream(tempFiles.get(tempFiles.size() - 1));
            fis.write(byteArrayOutputStream.toByteArray());
            fis.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != byteArrayOutputStream) {
                    byteArrayOutputStream.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addbmp = getImage(pathImage);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", addbmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this,
                    imageItem, R.layout.add_picture_gridview_item,
                    new String[]{"itemImage"}, new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data,
                                            String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gvImage.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            //刷新后释放防止手机休眠后自动添加
            pathImage = null;
        }
    }

    /*
     * deleteIMG对话框提示用户删除操作
     * position为删除图片位置
     */
    protected void deleteIMG(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WorkInfoUploadActivity.this);
        builder.setMessage("确认移除已添加图片吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
                simpleAdapter.notifyDataSetChanged();
                tempFiles.remove(mPosition - 1);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private void upload(){
        RequestParams params = new RequestParams();
        params.addBodyParameter("snrZYLX", snrZYLX.getSelectedItem().toString() == "增雨" ? "1" : "2");
        params.addBodyParameter("etHJDYL", etHJDYL.getText().toString());
        params.addBodyParameter("etYTYL", etYTYL.getText().toString());
        params.addBodyParameter("etZYRQ", etZYRQ.getText().toString());
        params.addBodyParameter("etZYKSSJ", etZYKSSJ.getText().toString());
        params.addBodyParameter("etZYJSSJ", etZYJSSJ.getText().toString());
        params.addBodyParameter("etZYMJ", etZYMJ.getText().toString());
        params.addBodyParameter("etZYZDBH", etZYZDBH.getText().toString());
        params.addBodyParameter("etGPSJD", etGPSJD.getText().toString());
        params.addBodyParameter("etGPSWD", etGPSWD.getText().toString());
        params.addBodyParameter("etZYQJLX", etZYQJLX.getText().toString() == "火箭弹" ? "1" : "2");
        params.addBodyParameter("etGSPDYL", etGSPDYL.getText().toString());
        params.addBodyParameter("etQTYL", etQTYL.getText().toString());
        params.addBodyParameter("etYJ", etYJ.getText().toString());
        params.addBodyParameter("etFW", etFW.getText().toString());
        params.addBodyParameter("etKY", etKY.getText().toString());
        params.addBodyParameter("etTBR", etTBR.getText().toString());
        params.addBodyParameter("etTBDW", etTBDW.getText().toString());
        params.addBodyParameter("etTXRQ", etTXRQ.getText().toString());
        params.addBodyParameter("etBZ", etBZ.getText().toString());
        params.addBodyParameter("zyqtqqk", zyqtqqk.getSelectedItem().toString());
        params.addBodyParameter("zyhtqqk", zyhtqqk.getSelectedItem().toString());
        params.addBodyParameter("zyxg", zyxg.getSelectedItem().toString());
        params.addBodyParameter("dmfx", dmfx.getSelectedItem().toString());
        params.addBodyParameter("etZYFABH", workPlanId);

        params.addBodyParameter("picIsNull", "true");
        for (int i = 0; i < tempFiles.size(); i++) {
            params.addBodyParameter(tempFiles.get(i).getPath().replace("/", ""), tempFiles.get(i));
            params.addBodyParameter("picIsNull", "false");
        }
        String path = "http://" + PushUtils.getServerIPText(this) + "/rgyx/appWorkInfoSub/addWorkInfoSub?token=" + PushUtils.token;
        try {
            httpUtils.send(HttpRequest.HttpMethod.POST, path, params, new RequestCallBack<String>() {
                @Override
                public void onFailure(HttpException e, String msg) {
                    Toast.makeText(WorkInfoUploadActivity.this, "提交失败，请检查网络是否正确连接！", Toast.LENGTH_SHORT).show();
                    isUploaded = false;
                    myDialog.cancel();
                }

                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    myDialog.cancel();
                    isUploaded = false;

                    //Toast.makeText(WorkInfoUploadActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                    Map<String, Object> resultMap = JsonTools.getMap(responseInfo.result);

                    if(resultMap.containsKey("status")){
                        String status = resultMap.get("status").toString(); //服务端反馈状态码

                        if ("1".equals(status)) {
                            new AlertDialog.Builder(WorkInfoUploadActivity.this).setTitle("提交成功！")
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 点击“确认”后的操作
                                            finish();
                                        }
                                    }).show();
                        }
                        else if ("2".equals(status)) {
                            Toast.makeText(WorkInfoUploadActivity.this, resultMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
                        } else if ("9999".equals(status)) {
                            Toast.makeText(WorkInfoUploadActivity.this, resultMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(WorkInfoUploadActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            isUploaded = false;
        }
    }

}
