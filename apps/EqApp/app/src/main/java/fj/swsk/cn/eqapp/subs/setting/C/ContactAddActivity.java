package fj.swsk.cn.eqapp.subs.setting.C;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.subs.setting.M.DistinctDatas;
import fj.swsk.cn.eqapp.util.BaseUtils;
import fj.swsk.cn.eqapp.util.DatabaseHelper;
import fj.swsk.cn.eqapp.util.PinYin;

public class ContactAddActivity extends BaseTopbarActivity implements View.OnClickListener {
    /**
     * 指定操作是编辑还是添加
     */
    String operation;
    /**
     * 联系人ID
     */
    int id;
    EditText name;
    EditText phone;
    EditText email;
    EditText remark;
    TextView sex_name;
    TextView department_name;
    TextView city_name;
    TextView job_post_name;
    TextView district_name;
    SelectItemPopupWindow menuWindow;
    /**
     * 标识当前点击弹出底部菜单的项
     **/
    Object menuWindowTag;
    /***
     * 选择城市位置
     ***/
    int cityPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_contact_add_edit;
        super.onCreate(savedInstanceState);
        initView();
        /**
         * 根据用户操作，更新或添加联系人数据
         */
        this.mTopbar.rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation.equals("edit") && id != -1) {
                    uploadContactByUpdate();
                } else if (operation.equals("add")) {
                    uploadContactByInsert();
                }
            }
        };
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            long rows = msg.getData().getLong("rows");
            if (msg.what == 1) {
                if (rows > 0) {
                    Toast.makeText(getApplicationContext(), "添加成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (rows == -1) {
                    Toast.makeText(getApplicationContext(), "添加失败", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 2) {
                if (rows > 0) {
                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                    finishEdit();
                } else if (rows == -1) {
                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                }
            }

            super.handleMessage(msg);
        }
    };

    /**
     * 初始化界面
     */
    private void initView() {
        Intent intent = getIntent();
        operation = intent.getStringExtra("operation");

        id = intent.getIntExtra("id", -1);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        remark = (EditText) findViewById(R.id.remark);
        email = (EditText) findViewById(R.id.email);
        department_name = (TextView) findViewById(R.id.department_name);
        city_name = (TextView) findViewById(R.id.city_name);
        job_post_name = (TextView) findViewById(R.id.job_post_name);
        district_name = (TextView) findViewById(R.id.district_name);
        sex_name = (TextView) findViewById(R.id.sex_name);

        findViewById(R.id.department_item).setOnClickListener(this);
        findViewById(R.id.city_item).setOnClickListener(this);
        findViewById(R.id.job_post_item).setOnClickListener(this);
        findViewById(R.id.district_item).setOnClickListener(this);
        findViewById(R.id.sex_item).setOnClickListener(this);

        if (operation.equals("edit") && id != -1) {
            name.setText(intent.getStringExtra("name"));
            remark.setText(intent.getStringExtra("remark"));
            phone.setText(intent.getStringExtra("phone"));
            email.setText(intent.getStringExtra("email"));
            city_name.setText(intent.getStringExtra("city"));
            sex_name.setText(intent.getStringExtra("sex"));
            job_post_name.setText(intent.getStringExtra("jobPost"));
            department_name.setText(intent.getStringExtra("department"));
            district_name.setText(intent.getStringExtra("district"));
        }

        menuWindow = new SelectItemPopupWindow(this);
        /**
         * 底部列表菜单选择
         */
        menuWindow.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (menuWindowTag.equals("1")) {
                    sex_name.setText(DistinctDatas.sexs[position]);
                } else if (menuWindowTag.equals("2")) {
                    cityPos = position;
                    city_name.setText(DistinctDatas.cities[position]);
                } else if (menuWindowTag.equals("3")) {
                    district_name.setText(DistinctDatas.districts[cityPos][position]);
                } else if (menuWindowTag.equals("4")) {
                    department_name.setText(DistinctDatas.departments[position]);
                } else if (menuWindowTag.equals("5")) {
                    job_post_name.setText(DistinctDatas.jobPosts[position]);
                }

                menuWindow.dismiss();
            }
        });
    }

    /**
     * 弹出底部菜单
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        menuWindowTag = v.getTag().toString();
        String[] data;
        if (menuWindowTag.equals("1")) {
            data = DistinctDatas.sexs;
        } else if (menuWindowTag.equals("2")) {
            data = DistinctDatas.cities;
        } else if (menuWindowTag.equals("3")) {
            data = DistinctDatas.districts[cityPos];
        } else if (menuWindowTag.equals("4")) {
            data = DistinctDatas.departments;
        } else {
            data = DistinctDatas.jobPosts;
        }

        menuWindow.setListViewAdapter(
                new ArrayAdapter<>(
                        this,
                        R.layout.simple_list_item,
                        R.id.item, data));
        menuWindow.showAtLocation(findViewById(R.id.settingsMain),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 完成编辑后返回数据到上一页面
     */
    private void finishEdit() {
        Intent mIntent = new Intent();
        mIntent.putExtra("name", name.getText());
        mIntent.putExtra("phone", phone.getText());
        mIntent.putExtra("remark", remark.getText());
        mIntent.putExtra("city", city_name.getText());
        mIntent.putExtra("district", district_name.getText());
        mIntent.putExtra("department", department_name.getText());
        mIntent.putExtra("jobPost", job_post_name.getText());
        mIntent.putExtra("sex", sex_name.getText());
        mIntent.putExtra("email", email.getText());

        setResult(Activity.RESULT_OK, mIntent);
        finish();
    }

    /**
     * 以contentvalues形式返回当前联系人信息
     *
     * @return
     */
    private ContentValues getContactInfo() {
        Object mName = name.getText();
        Object telNo = phone.getText();
        if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "请添加姓名", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (telNo == null || !BaseUtils.isMobileNo(telNo.toString())) {
            Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
            return null;
        }
        String mEmail = email.getText().toString();
        if (!TextUtils.isEmpty(mEmail)) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
                Toast.makeText(getApplicationContext(), "请输入正确的邮箱地址", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        ContentValues cv = new ContentValues();
        cv.put("phone", telNo.toString());
        cv.put("name", mName.toString());
        cv.put("remark", remark.getText().toString());
        cv.put("sortKey", PinYin.getPinYin(mName.toString()).substring(0, 1).toUpperCase());
        cv.put("co1", city_name.getText().toString());
        cv.put("co2", district_name.getText().toString());
        cv.put("department", department_name.getText().toString());
        cv.put("jobPost", job_post_name.getText().toString());
        cv.put("email", email.getText().toString());
        cv.put("sex", sex_name.getText().toString().equals("男") ? 1 : 2);
        //手机系统时间，有可能和服务器系统时间不一致
        cv.put("createDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        return cv;
    }

    /**
     * 上传插入联系人到服务器
     */
    public void uploadContactByInsert() {
        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                SQLiteDatabase db = null;
                try {
                    ContentValues cv = getContactInfo();
                    cv.put("uploaded",1);
                    DatabaseHelper databaseHelper = new DatabaseHelper(ContactAddActivity.this);
                    db = databaseHelper.getReadableDatabase();
                    long rows = db.insert(DatabaseHelper.CONTACT, null, cv);
                    //插入服务器
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putLong("rows", rows);
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                }finally {
                    if(db!=null){
                        db.close();
                    }
                }
            }
        });
    }

    /**
     * 上传更新联系人到服务器
     */
    public void uploadContactByUpdate() {
        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                SQLiteDatabase db = null;
                try {
                    ContentValues cv = getContactInfo();
                    cv.put("uploaded",2);
                    DatabaseHelper databaseHelper = new DatabaseHelper(ContactAddActivity.this);
                    db = databaseHelper.getReadableDatabase();
                    db.beginTransaction();
                    long rows = db.update(DatabaseHelper.CONTACT, cv, "id=" + id, null);
                    //更新服务器
                    db.setTransactionSuccessful();
                    Message msg = new Message();
                    msg.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putLong("rows", rows);
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                } finally {
                    if(db!=null){
                        db.endTransaction();
                        db.close();
                    }
                }
            }
        });
    }
}
