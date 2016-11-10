package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import cn.swsk.rgyxtqapp.bean.GroupMemberBean;
import cn.swsk.rgyxtqapp.bean.WorkPlan;
import cn.swsk.rgyxtqapp.custom.LVDialog;
import cn.swsk.rgyxtqapp.custom.topbar;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.DialogUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;

/**
 * 作业信息上报
 * Created by tom on 2015/10/17.
 */
public class WorkInfoUploadActivity extends Activity {
    private String specPointId = "350625043";

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
    private GroupMemberBean wp;
    private boolean timeSelected = false;
    private boolean dateSelected = false;
    private cn.swsk.rgyxtqapp.custom.ProgressDialog dialog = null; //等待状态提示框

    private TextView tv_plan_name,tv_famc; //方案名称

    private EditText etHJDYL, etYTYL, etZYRQ, etZYKSSJ, etZYJSSJ,
            etZYMJ, etZYZDBH, etGPSJD, etGPSWD, etZYQJLX, etGSPDYL, etQTYL,
            etYJ, etFW, etKY, etTBR, etTXRQ, etBZ;

    private Spinner snrZYLX, zyqtqqk, zyhtqqk, zyxg, dmfx,etTBDW;

    private Button chooseTBR;

    private boolean isUploaded = false;
    // 创建一个以当前系统时间为名称的文件，防止重复
    private List<File> tempFiles = new ArrayList<>();
    private Dialog tbrlv;

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

        init();

        topbar mTopbar = (topbar) findViewById(R.id.workInfoTopbar);
        //mTopbar.setRightButtonIsvisable(false);
        mTopbar.setOnTopbarClickListener(new topbar.topbarClickListener() {
            @Override
            public void leftClick() {
                finish();
            }

            @Override
            public void rightClick() {
                if (snrZYLX.getSelectedItem().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请选择作业类型。", Toast.LENGTH_SHORT).show();
                    snrZYLX.requestFocus();
                    return;
                }
                if (etHJDYL.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入火箭弹用量。", Toast.LENGTH_SHORT).show();
                    etHJDYL.requestFocus();
                    return;
                }
                if (etYTYL.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入烟条用量。", Toast.LENGTH_SHORT).show();
                    etYTYL.requestFocus();
                    return;
                }
                if (etZYRQ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业日期。", Toast.LENGTH_SHORT).show();
                    etZYRQ.requestFocus();
                    return;
                }
                if (etZYKSSJ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业开始时间。", Toast.LENGTH_SHORT).show();
                    etZYKSSJ.requestFocus();
                    return;
                }
                if (etZYJSSJ.getText().toString().equals("")) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请输入作业结束时间。", Toast.LENGTH_SHORT).show();
                    etZYJSSJ.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etTBDW.getSelectedItem().toString())) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请选择填报单位。", Toast.LENGTH_SHORT).show();
                    etTBDW.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(etTBR.getText().toString())) {
                    Toast.makeText(WorkInfoUploadActivity.this, "请选择填报人。", Toast.LENGTH_SHORT).show();
                    etTBR.requestFocus();
                    return;
                }
                if(empty(etHJDYL)||empty(etYTYL)||empty(etZYRQ)||
                        empty(etZYKSSJ)||empty(etZYJSSJ)||empty(etZYMJ)||
                        empty(etZYZDBH)||empty(etGPSJD)||empty(etGPSWD)||empty(etZYQJLX)||
                        empty(etGSPDYL)||empty(etQTYL)||empty(etYJ)||empty(etFW)
                        ||empty(etKY)||empty(etTBR)||empty(etTXRQ)
                        ){
                    return ;
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
                // TODO Auto-generated method stub
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

        if(!TextUtils.isEmpty(intent.getStringExtra("workPlanId"))){
            workPlanId = intent.getStringExtra("workPlanId");
            tv_plan_name.setText(intent.getStringExtra("workPlanName"));
            String PATH = "http://" + PushUtils.getServerIPText(WorkInfoUploadActivity.this) + "/rgyx/appWorkInfoManage/findWorkPlanInfo";
            String path = PATH + "?token=" + PushUtils.token + "&id=" + workPlanId;
            new Tom1AsyncTask().execute(path);
        }else {
            wp =(GroupMemberBean)intent.getSerializableExtra("workPoint");
            tv_plan_name.setText(wp.getName());
            tv_famc.setText("作业点名称:");
            if (intent.getStringExtra("WORK_TYPE") != null){
                snrZYLX.setSelection("增雨".equals(intent.getStringExtra("WORK_TYPE").toString())?0:1 );
            }
            etZYZDBH.setText(wp.getWorkTypeId());
            etGPSJD.setText(wp.lon);
            etGPSWD.setText(wp.lat);
            etZYQJLX.setText("火箭弹");
            etKY.setText(wp.notificati);
//            etTBR.setText(PushUtils.userName);
            if(isspec()){
                final  ArrayAdapter adapter2 = new ArrayAdapter(this,R.layout.tom_spinner_item,new String[]{PushUtils.userUnit,"厦门市人影办"});
                etTBDW.setEnabled(true);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                etTBDW.setAdapter(null);
                etTBDW.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(etTBDW.getAdapter()==null)
                            etTBDW.setAdapter(adapter2);
                        return false;
                    }
                });



            }


        }


    }

    /**
     * 初始化
     */
    private void init() {
        dialog = new cn.swsk.rgyxtqapp.custom.ProgressDialog(WorkInfoUploadActivity.this);
    }

    /**
     * 异步加载作业方案信息
     */
    class Tom1AsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.showDialog(); //显示加载等待提示框
        }

        @Override
        protected void onPostExecute(Map<String, Object> stringObjectMap) {
            dialog.cancelDialog();//关闭加载等待提示框

            if (stringObjectMap == null) {
                Toast.makeText(WorkInfoUploadActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                return;
            }
            //获取状态值
            String status = stringObjectMap.containsKey("status") ? stringObjectMap.get("status").toString() : "";

            if ("1".equals(status)) {
                //初始化popupWindow
                List<WorkPlan> datas = new ArrayList<WorkPlan>();

                Map<String, Object> workPlanInfo = (Map<String, Object>) stringObjectMap.get("workPlanInfo");

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
                        default:etZYQJLX.setText("火箭弹");
                            break;
                    }
                }
                //etZYQJLX.setText(workPlanInfo.get("WORK_WARE_TYPE") == null ? "" : workPlanInfo.get("WORK_WARE_TYPE").toString());
                etYJ.setText(workPlanInfo.get("BEST_ELEVATION") == null ? "" : workPlanInfo.get("BEST_ELEVATION").toString());
                etFW.setText(workPlanInfo.get("BEST_AZIMUTH") == null ? "" : workPlanInfo.get("BEST_AZIMUTH").toString());
                etKY.setText(workPlanInfo.get("NOTIFICATI") == null ? "" : workPlanInfo.get("NOTIFICATI").toString());
//                etTBR.setText(PushUtils.userName);
                etTBDW.setSelection(0);
            } else if ("2".equals(status)) {
                Toast.makeText(WorkInfoUploadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else if ("9999".equals(status)) {
                Toast.makeText(WorkInfoUploadActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(WorkInfoUploadActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(stringObjectMap);
        }

        @Override
        protected Map<String, Object> doInBackground(String... params) {
            String path = params[0];
            String jsonString1 = cn.swsk.rgyxtqapp.utils.HttpUtils.getJsonContent(path);
            if (jsonString1 == null) {
                return null;
            }
            Map<String, Object> map = JsonTools.getMap(jsonString1);
            return map;
        }
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
        chooseTBR = (Button) findViewById(R.id.chooseTBR);
        etTBDW = (Spinner) findViewById(R.id.et_tbdw);
        etTXRQ = (EditText) findViewById(R.id.et_tbrq);
        etBZ = (EditText) findViewById(R.id.et_bz);
        tv_famc =(TextView)findViewById(R.id.tv_famc);

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

        adapter2 = new ArrayAdapter(this, R.layout.tom_spinner_item, new String[]{PushUtils.userUnit});


        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        etTBDW.setAdapter(adapter2);
        etTBDW.setEnabled(false);

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
        chooseTBR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tbrlv!=null){
                    tbrlv.show();
                }else{
                    cn.swsk.rgyxtqapp.utils.HttpUtils.commonRequest2
                            (PushUtils.getServerIP(WorkInfoUploadActivity.this) + "rgyx/appWorkInfoManage/getName?token=" + PushUtils.token,
                                    WorkInfoUploadActivity.this, new cn.swsk.rgyxtqapp.utils.HttpUtils.RequestCB() {
                                        @Override
                                        public void cb(Map<String, Object> map, String resp, int type) {
                                            List<Map<String,Object>> listmap = (List<Map<String,Object>>)JsonTools.getListMap("",resp);
                                            tbrlv= LVDialog.getLvDialogNShow(WorkInfoUploadActivity.this,null,"选择填报人", new SimpleAdapter(WorkInfoUploadActivity.this,
                                                    listmap, R.layout.item4lv_onerow01,
                                                    new String[] { "stuffName" },
                                                    new int[] { android.R.id.text1 }),  new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    etTBR.setText(((TextView)view).getText());
                                                }
                                            }) ;
                                        }
                                    });


                }
            }
        });



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
                            editText.setText(hourOfDay + "点" + minute + "分");
                        }
                    }, mHour, mMinute, true);
            dialog.setTitle(editText.getTag().toString());
//            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    timeSelected = true;
//                    dialog.dismiss();
//                }
//            });
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

    public void showImagePickDialog() {
        String title = "获取图片方式";
        String[] choices = new String[]{"拍照", "从手机中选择"};
        AlertDialog.Builder dialog = DialogUtils.getItemDialogBuilder(this, choices, title, dialogListener);
        dialog.show();
    }

    private android.content.DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    // 调用拍照
                    File tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    tempFiles.add(tempFile);
                    startCamera(dialog);
                    break;
                case 1:
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            getPhotoFileName());
                    tempFiles.add(tempFile);
                    // 调用相册
                    startPick(dialog);
                    break;
                default:
                    break;
            }
        }
    };

    // 调用系统相机
    protected void startCamera(DialogInterface dialog) {
        dialog.dismiss();
        // 调用系统的拍照功能
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("camerasensortype", 2); // 调用摄像头
        intent.putExtra("autofocus", true); // 自动对焦
        intent.putExtra("fullScreen", false); // 全屏
        intent.putExtra("showActionIcons", false);
        // 指定调用相机拍照后照片的存储路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFiles.get(tempFiles.size() - 1)));
        startActivityForResult(intent, PHOTO_CARMERA);
    }

    // 调用系统相册
    protected void startPick(DialogInterface dialog) {
        dialog.dismiss();
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_PICK);
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

    private boolean empty(EditText et){
        if(TextUtils.isEmpty(et.getText())){
            CommonUtils.toast("请完善信息再提交",this);
            et.requestFocus();
            return true;
        }
        return false;
    }
    // 上传文件到服务器
    protected void upload() {




        RequestParams params = new RequestParams();
        params.addBodyParameter("snrZYLX", snrZYLX.getSelectedItem().toString() .equals( "增雨") ? "1" : "2");
        params.addBodyParameter("etHJDYL", etHJDYL.getText().toString());
        params.addBodyParameter("etYTYL", etYTYL.getText().toString());
        params.addBodyParameter("etZYRQ", etZYRQ.getText().toString());
        params.addBodyParameter("etZYKSSJ", etZYKSSJ.getText().toString());
        params.addBodyParameter("etZYJSSJ", etZYJSSJ.getText().toString());
        params.addBodyParameter("etZYMJ", etZYMJ.getText().toString());
        params.addBodyParameter("etZYZDBH", etZYZDBH.getText().toString());
        params.addBodyParameter("etGPSJD", etGPSJD.getText().toString());
        params.addBodyParameter("etGPSWD", etGPSWD.getText().toString());
        params.addBodyParameter("etZYQJLX", etZYQJLX.getText().toString() .equals( "火箭弹" )? "1" : "2");
        params.addBodyParameter("etGSPDYL", etGSPDYL.getText().toString());
        params.addBodyParameter("etQTYL", etQTYL.getText().toString());
        params.addBodyParameter("etYJ", etYJ.getText().toString());
        params.addBodyParameter("etFW", etFW.getText().toString());
        params.addBodyParameter("etKY", etKY.getText().toString());
        params.addBodyParameter("etTBR", etTBR.getText().toString());
        params.addBodyParameter("etTBDW", etTBDW.getSelectedItem().toString());
        params.addBodyParameter("etTXRQ", etTXRQ.getText().toString());
        params.addBodyParameter("etBZ", etBZ.getText().toString());
//        params.addBodyParameter("zyqtqqk", zyqtqqk.getSelectedItem().toString());
//        params.addBodyParameter("zyhtqqk",                zyhtqqk.getSelectedItem().toString());
//        params.addBodyParameter("zyxg", zyxg.getSelectedItem().toString());
//        params.addBodyParameter("dmfx", dmfx.getSelectedItem().toString());
        params.addBodyParameter("zyqtqqk", zyqtqqk.getSelectedItemPosition()+1+"");
        params.addBodyParameter("zyhtqqk", zyhtqqk.getSelectedItemPosition()+1+"");
        params.addBodyParameter("zyxg", zyxg.getSelectedItemPosition()+1+"");
        params.addBodyParameter("dmfx", dmfx.getSelectedItemPosition()+1+"");
        params.addBodyParameter("etZYFABH", workPlanId==null?"":workPlanId);





        if( tempFiles.size()==0)
            params.addBodyParameter("picIsNull", "true");
        for (int i = 0; i < tempFiles.size(); i++) {
            params.addBodyParameter("picIsNull", "false");
            params.addBodyParameter(tempFiles.get(i).getPath().replace("/", ""), tempFiles.get(i));

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
                    // TODO Auto-generated method stub
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

    private boolean isspec(){
        if(wp==null) return false;


        return specPointId.equals(wp.getWorkTypeId());
    }
}


