package fj.swsk.cn.eqapp.subs.setting.C;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.util.DatabaseHelper;

public class ContactInfoActivity extends BaseTopbarActivity {
    int id;
    TextView name;
    TextView phone;
    TextView remark;
    TextView sendMsgBtn;
    TextView deleteContactorBnt;
    TextView department_name;
    TextView city_name;
    TextView district_name;
    TextView job_post_name;
    TextView email;
    TextView upload;
    TextView callPhone;
    ImageView sex_name;
    int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.contract_info_view;
        super.onCreate(savedInstanceState);

        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        remark = (TextView) findViewById(R.id.remark);
        sendMsgBtn = (TextView) findViewById(R.id.sendMsgBtn);
        deleteContactorBnt = (TextView) findViewById(R.id.deleteContactorBnt);
        district_name = (TextView) findViewById(R.id.district_name);
        city_name = (TextView) findViewById(R.id.city_name);
        department_name = (TextView) findViewById(R.id.department_name);
        job_post_name = (TextView) findViewById(R.id.job_post_name);
        email = (TextView) findViewById(R.id.email_name);
        sex_name = (ImageView) findViewById(R.id.sexImg);
        upload = (TextView) findViewById(R.id.upload);
        callPhone = (TextView) findViewById(R.id.callPhone);
        initView();
        this.mTopbar.rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ContactInfoActivity.this, ContactAddActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", id);
                bundle.putString("operation", "edit");
                bundle.putString("name", name.getText().toString());
                bundle.putString("phone", phone.getText().toString());
                bundle.putString("remark", remark.getText().toString());
                bundle.putString("department", department_name.getText().toString());
                bundle.putString("jobPost", job_post_name.getText().toString());
                bundle.putString("email", email.getText().toString());
                bundle.putString("city", city_name.getText().toString());
                bundle.putString("district", district_name.getText().toString());
                bundle.putString("sex", sex == 1 ? "男" : "女");
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        };
        deleteContactorBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactInfoActivity.this);
                builder.setTitle("你确定删除该联系人吗？").setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (id != -1) {
                                    int rows = -1;
                                    try {
                                        DatabaseHelper databaseHelper = new DatabaseHelper(ContactInfoActivity.this);
                                        SQLiteDatabase db = databaseHelper.getReadableDatabase();
                                        rows = db.delete(DatabaseHelper.CONTACT, "id=" + id, null);
                                    } catch (Exception e) {
                                        Log.d("ContactInfoActivity", "", e);
                                    }
                                    if (rows > 0) {
                                        Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).create().show();
            }
        });
        sendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phone.getText()));
                startActivity(intent);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadContact();
            }
        });
        callPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile = phone.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                startActivity(intent);
            }
        });
    }

    public void initView() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        name.setText(intent.getStringExtra("name"));
        phone.setText(intent.getStringExtra("phone"));
        remark.setText(intent.getStringExtra("remark"));
        district_name.setText(intent.getStringExtra("district"));
        city_name.setText(intent.getStringExtra("city"));
        department_name.setText(intent.getStringExtra("department"));
        job_post_name.setText(intent.getStringExtra("jobPost"));
        email.setText(intent.getStringExtra("email"));
        sex = intent.getIntExtra("sex", -1);
        if (sex == 1) {
            sex_name.setImageResource(R.mipmap.men);
        } else if (sex == 2) {
            sex_name.setImageResource(R.mipmap.women);
        }
        int uploaded = intent.getIntExtra("uploaded",-1);
        if(uploaded == 0){
            upload.setVisibility(View.GONE);
            findViewById(R.id.upload_line).setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            Bundle bundle = data.getExtras();
            name.setText(bundle.get("name").toString());
            remark.setText(bundle.get("remark").toString());
            phone.setText(bundle.get("phone").toString());
            district_name.setText(bundle.get("district").toString());
            city_name.setText(bundle.get("city").toString());
            department_name.setText(bundle.get("department").toString());
            job_post_name.setText(bundle.get("jobPost").toString());
            email.setText(bundle.get("email").toString());

            String sex = bundle.get("sex").toString();
            if(sex.equals("男")){
                sex_name.setImageResource(R.mipmap.men);
            }else if(sex.equals("女")){
                sex_name.setImageResource(R.mipmap.women);
            }
        }
    }

    /**
     * 上传到服务器
     * 上传成功后修改uploaded字段
     */
    private void uploadContact(){

    }
}
