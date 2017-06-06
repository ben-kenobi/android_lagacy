package fj.swsk.cn.eqapp.subs.setting.C;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.C.BaseTopbarActivity;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.map.adapter.ContactsAdapter;
import fj.swsk.cn.eqapp.subs.setting.M.ContactInfo;
import fj.swsk.cn.eqapp.subs.setting.M.ContactSearchArgs;
import fj.swsk.cn.eqapp.subs.setting.M.DistinctDatas;
import fj.swsk.cn.eqapp.util.DatabaseHelper;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.PinYin;

public class ContactsActivity extends BaseTopbarActivity implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout swipeRefreshLayout;
    List<ContactInfo> contacts;
    RecyclerView recyclerView;
    ContactSearchArgs mContactSearchArgs;
    SelectItemPopupWindow menuWindow;
    /**
     * 标识当前点击弹出底部菜单的项
     **/
    int menuWindowTag = -1;
    /***
     * 选择城市位置
     ***/
    int cityPos = 0;
    /**
     * 默认的联系人搜索条件
     **/
    String where = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        layoutRes = R.layout.activity_contacts;
        super.onCreate(savedInstanceState);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout.setDistanceToTriggerSync(20);
        swipeRefreshLayout.setProgressViewOffset(false, 0, 0);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.lighter_gray);
        swipeRefreshLayout.setProgressViewEndTarget(false, 120);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.pinnedlv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置为垂直布局，这也是默认的
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration();
        contacts = new ArrayList<>();
        this.mTopbar.rightListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //添加模式
                intent.putExtra("operation", "add");
                intent.setClass(ContactsActivity.this, ContactAddActivity.class);
                startActivity(intent);
            }
        };
        menuWindow = new SelectItemPopupWindow(this);
        menuWindow.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView city_name = (TextView) findViewById(R.id.city_name);
                TextView district_name = (TextView) findViewById(R.id.district_name);
                TextView department_name = (TextView) findViewById(R.id.department_name);
                TextView job_post_name = (TextView) findViewById(R.id.job_post_name);
                if (menuWindowTag == 0) {
                    cityPos = position;
                    String city = DistinctDatas.cities1[position];
                    mContactSearchArgs.SubordinatedCity = city;
                    city_name.setText(city);
                    mContactSearchArgs.SubordinatedDistrict = DistinctDatas.districts1[cityPos][0];
                    district_name.setText(mContactSearchArgs.SubordinatedDistrict);
                } else if (menuWindowTag == 1) {
                    String district = DistinctDatas.districts1[cityPos][position];
                    mContactSearchArgs.SubordinatedDistrict = district;
                    district_name.setText(district);
                } else if (menuWindowTag == 2) {
                    String department = DistinctDatas.departments1[position];
                    mContactSearchArgs.SubordinatedDepartment = department;
                    department_name.setText(department);
                } else if (menuWindowTag == 3) {
                    String jobPost = DistinctDatas.jobPosts1[position];
                    mContactSearchArgs.SubordinatedJobPost = jobPost;
                    job_post_name.setText(jobPost);
                }
                StringBuilder sb = new StringBuilder();
                boolean city_all = false;
                if (!city_name.getText().equals("全部")) {
                    sb.append("co1 ='" + city_name.getText() + "'");
                } else {
                    city_all = true;
                    sb.append("1=1");
                }
                if (!city_all && !district_name.getText().equals("全部")) {
                    sb.append(" and co2 ='" + district_name.getText() + "'");
                } else {
                    sb.append(" and 1=1");
                }
                if (!department_name.getText().equals("全部")) {
                    sb.append(" and department ='" + department_name.getText() + "'");
                } else {
                    sb.append(" and 1=1");
                }
                if (!job_post_name.getText().equals("全部")) {
                    sb.append(" and jobPost ='" + job_post_name.getText() + "'");
                } else {
                    sb.append(" and 1=1");
                }
                where = sb.toString();
                menuWindow.dismiss();
                //根据查询条件更新联系人列表
                initView();
            }
        });


        onRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 3 || msg.what == 4) {
                if (msg.what == 3) {
                    Toast.makeText(ContactsActivity.this, "同步服务器失败", Toast.LENGTH_SHORT).show();
                }
                if (msg.what == 4) {
                    Toast.makeText(ContactsActivity.this, "同步服务器成功", Toast.LENGTH_SHORT).show();
                }
//                swipeRefreshLayout.setRefreshing(false);
                return;
            }

            findViewById(R.id.progressBar).setVisibility(View.GONE);
            if (msg.what == 1) {
                if (contacts.size() > 0) {
                    findViewById(R.id.resultInfo).setVisibility(View.GONE);
                }
            } else if (msg.what == 2) {
                findViewById(R.id.resultInfo).setVisibility(View.VISIBLE);
            }
            if (mContactSearchArgs == null) {
                mContactSearchArgs = new ContactSearchArgs();
            }
            ContactsAdapter recycleAdapter = new ContactsAdapter(ContactsActivity.this, contacts, mContactSearchArgs);
            recycleAdapter.setOnContactItemClickListener(new ContactsAdapter.OnContactItemClickListener() {

                @Override
                public void onItemClick(View v, Object contactInfo) {
                    ContactInfo mContactInfo = (ContactInfo) contactInfo;
                    startContactInfoActivity(mContactInfo);
                }
            });

            recycleAdapter.setOnSearchItemClickListener(new ContactsAdapter.OnSearchItemClickListener() {

                @Override
                public void onItemClick(View v, int position) {
                    showBottomMenu(position);
                }
            });

            recyclerView.setAdapter(recycleAdapter);
            recycleAdapter.notifyDataSetChanged();
            super.handleMessage(msg);
        }
    };

    /**
     * 启动联系人信息页面
     *
     * @param mContactInfo
     */
    private void startContactInfoActivity(ContactInfo mContactInfo) {
        if (mContactInfo != null) {
            Intent intent = new Intent();
            intent.putExtra("id", mContactInfo.Id);
            intent.putExtra("name", mContactInfo.Name);
            intent.putExtra("phone", mContactInfo.Phone);
            intent.putExtra("remark", mContactInfo.remark);
            intent.putExtra("city", mContactInfo.Co1);
            intent.putExtra("district", mContactInfo.Co2);
            intent.putExtra("department", mContactInfo.Department);
            intent.putExtra("jobPost", mContactInfo.JobPost);
            intent.putExtra("email", mContactInfo.Email);
            intent.putExtra("sex", mContactInfo.Sex);
            intent.putExtra("uploaded", mContactInfo.uploaded);
            intent.setClass(ContactsActivity.this, ContactInfoActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 根据搜索条件列表点击位置弹出相应的底部菜单
     *
     * @param position
     */
    private void showBottomMenu(int position) {
        menuWindowTag = position;
        String[] data = {};
        switch (position) {
            case 0:
                data = DistinctDatas.cities1;
                break;
            case 1:
                data = DistinctDatas.districts1[cityPos];
                break;
            case 2:
                data = DistinctDatas.departments1;
                break;
            case 3:
                data = DistinctDatas.jobPosts1;
                break;
        }
        menuWindow.setListViewAdapter(
                new ArrayAdapter<>(
                        ContactsActivity.this,
                        R.layout.simple_list_item,
                        R.id.item, data));
        menuWindow.showAtLocation(findViewById(R.id.topCon),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    /**
     * 查询数据库初始化联系人列表
     */
    private void initView() {
        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                queryContactList(where);
            }
        });

    }

    private void queryContactList(final String mWhere) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper databaseHelper = new DatabaseHelper(ContactsActivity.this);
            db = databaseHelper.getReadableDatabase();
            Cursor cursor = db.query(DatabaseHelper.CONTACT, null, mWhere, null, null, null, "sortKey asc");
            Message msg = new Message();
            contacts.clear();
            if (cursor.moveToFirst()) {
                String sortKey = null;
                do {
                    ContactInfo mContactInfo = new ContactInfo();
                    mContactInfo.Id = cursor.getInt(cursor.getColumnIndex("id"));
                    mContactInfo.Phone = cursor.getString(cursor.getColumnIndex("phone"));
                    mContactInfo.Name = cursor.getString(cursor.getColumnIndex("name"));
                    mContactInfo.remark = cursor.getString(cursor.getColumnIndex("remark"));
                    mContactInfo.sork_key = cursor.getString(cursor.getColumnIndex("sortKey"));
                    mContactInfo.Co1 = cursor.getString(cursor.getColumnIndex("co1"));
                    mContactInfo.Co2 = cursor.getString(cursor.getColumnIndex("co2"));
                    mContactInfo.Department = cursor.getString(cursor.getColumnIndex("department"));
                    mContactInfo.Sex = cursor.getInt(cursor.getColumnIndex("sex"));
                    mContactInfo.JobPost = cursor.getString(cursor.getColumnIndex("jobPost"));
                    mContactInfo.Email = cursor.getString(cursor.getColumnIndex("email"));
                    mContactInfo.uploaded = cursor.getInt(cursor.getColumnIndex("uploaded"));
                    if (sortKey != null && sortKey.equals(mContactInfo.sork_key)) {
                        mContactInfo.viewType = 1;
                    } else {
                        mContactInfo.viewType = 2;
                    }
                    sortKey = mContactInfo.sork_key;
                    contacts.add(mContactInfo);
                } while (cursor.moveToNext());

                msg.what = 1;
            } else {
                msg.what = 2;
            }

            myHandler.sendMessage(msg);
        } catch (Exception e) {
            Log.d("ContactsActivity", "", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    private boolean synchronizeContacts(){
        Message message = new Message();
        try {
            Map<String, Object> map = JsonTools.getMap(HttpUtils.getJsonContent(IConstants.contactListUrl));
            if (map != null) {
                int status = (int) map.get("status");
                if (status == 400) {
                    String msg = (String) map.get("msg");
                    Toast.makeText(ContactsActivity.this, msg, Toast.LENGTH_SHORT).show();
                } else if (status == 200) {
                    ArrayList<LinkedHashMap> result = (ArrayList<LinkedHashMap>) map.get("data");
                    String sql = "insert into " + DatabaseHelper.CONTACT + "("
                            + "serverId,jobPost,sex,co1ID,co1,co2ID,"
                            + "co2,department,email,name,phone,createDate,"
                            + "remark,sortKey,uploaded) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    DatabaseHelper databaseHelper = new DatabaseHelper(ContactsActivity.this);
                    SQLiteDatabase db = databaseHelper.getReadableDatabase();
                    try {
                        SQLiteStatement stat = db.compileStatement(sql);
                        db.beginTransaction();
                        db.execSQL("DELETE FROM " + DatabaseHelper.CONTACT + " where uploaded=0");
                        for (int i = 0; i < result.size(); i++) {
                            LinkedHashMap hashMap = result.get(i);
                            stat.bindString(1, (String) hashMap.get("TID"));
                            stat.bindString(2, (String) hashMap.get("JOB_POST"));
                            stat.bindLong(3, (int) hashMap.get("SEX"));
                            stat.bindString(4,  hashMap.get("CO1_ID")==null ? "" : (String) hashMap.get("CO1_ID"));
                            stat.bindString(5, hashMap.get("CO1")==null ? "" : (String) hashMap.get("CO1"));
                            stat.bindString(6, hashMap.get("CO2_ID")==null ? "" : (String) hashMap.get("CO2_ID"));
                            stat.bindString(7, hashMap.get("CO2")==null ? "" : (String) hashMap.get("CO2"));
                            stat.bindString(8, (String) hashMap.get("DEPARTMENT"));
                            stat.bindString(9, (String) hashMap.get("EMAIL"));
                            String name = (String) hashMap.get("NAME");
                            stat.bindString(10, name);
                            stat.bindString(11, (String) hashMap.get("PHONE"));
                            stat.bindString(12, (String) hashMap.get("CREATE_DATE"));
                            stat.bindString(13, "");
                            stat.bindString(14, PinYin.getPinYin(name).substring(0, 1).toUpperCase());
                            stat.bindLong(15, 0);
                            long isSuccess = stat.executeInsert();
                            if (isSuccess < 0) {
                                return false;
                            }
                        }
                        db.setTransactionSuccessful();
                        return true;
                    } finally {
                        if (null != db) {
                            db.endTransaction();
                            db.close();
                        }
                    }
                }
            }else{
                message.what = 3;
                myHandler.sendMessage(message);
            }
            return true;
        } catch (Exception e) {
            return  false;
        }finally {
            IConstants.MAIN_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    }
    /**
     * 联系人同步刷新
     */
    @Override
    public void onRefresh() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        swipeRefreshLayout.setRefreshing(true);

        IConstants.THREAD_POOL.submit(new Runnable() {
            public void run() {
                synchronizeContacts();
                queryContactList(where);
            }
        });
    }
}
