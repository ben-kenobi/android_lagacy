package cn.swsk.rgyxtqapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.swsk.rgyxtqapp.adapter.CommonAdapter;
import cn.swsk.rgyxtqapp.adapter.ViewHolder;
import cn.swsk.rgyxtqapp.custom.ProgressDialog;
import cn.swsk.rgyxtqapp.utils.CommonUtils;
import cn.swsk.rgyxtqapp.utils.HttpUtils;
import cn.swsk.rgyxtqapp.utils.JsonTools;
import cn.swsk.rgyxtqapp.utils.NetworkUtils;
import cn.swsk.rgyxtqapp.utils.PushUtils;
import cn.swsk.rgyxtqapp.utils.UpdateManager;

public class LoginActivity extends Activity {

    private EditText txt_userName, txt_password;
    private CheckBox remenber_pwd, auto_login;
    private Button btn_login, btn_cancle;
    private String userNameValue, passwordValue;
    private SharedPreferences sharedPreferences;
    private int setCheckNum = 0; //设置按钮点击次数
    private ProgressDialog dialog = null; //等待状态提示框
    private ListView promptlv;
    private SharedPreferences users;
    private List<String[]> usernames;
    private CommonAdapter<String[]> usernamesAdapter;


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtils.log(UpdateManager.updating+"");
        // 检查软件更新
        if(NetworkUtils.isNetworkAvailable(this)) {
            UpdateManager manager = new UpdateManager(LoginActivity.this);

            manager.checkUpdate(false, this);
        }else{
            Toast.makeText(this,"网络不可用",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        promptlv.setVisibility(View.GONE);
        return  super.onTouchEvent(event);
    }

    private void showPromptLv(boolean show){
        if(promptlv==null){
            promptlv=new ListView(this);
            FrameLayout fl = (FrameLayout)findViewById(android.R.id.content);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(txt_userName.getWidth(),-2);
            int pos[]=new int[2];
            txt_userName.getLocationOnScreen(pos);
            lp.leftMargin=pos[0];
            lp.topMargin=pos[1]+txt_userName.getHeight()-20*CommonUtils.getDensity(this);


            fl.addView(promptlv,lp);
            promptlv.setBackgroundResource(R.drawable.prompt_bg);
            promptlv.setAdapter(usernamesAdapter);
            promptlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String[] ary = usernamesAdapter.getItem(position);
                    txt_userName.setText(ary[0]);
                    txt_password.setText(ary[1]);
                    promptlv.setVisibility(View.GONE);
                }
            });

        }
        promptlv.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void changeTextPrompt(String str){
        List<String[]> list = usernamesAdapter.getDatas();
        list.clear();
        for(String ary[]:usernames){
            if(ary[0].contains(str)){
                list.add(ary);
            }
        }
        usernamesAdapter.notifyDataSetChanged();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        String versionName = null;
        try {
            versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName + ":" +
                    getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        }catch (Exception e){
            e.printStackTrace();
        }
        ((TextView)findViewById(R.id.versionlab)).setText(versionName);

        UpdateManager.updating=false;


        //如果服务端IP为空时设置默认地址
        String ipStr = PushUtils.getServerIPText(this);

        if("".equals(ipStr)) {
            ipStr = "61.154.9.242:5551";
            PushUtils.setServerIPText(LoginActivity.this, ipStr);
        }
        //获取实例对象
        sharedPreferences = this.getSharedPreferences("userInfo", 1);
        users = this.getSharedPreferences("users",2);
        Map<String,String> map=(Map<String,String>)users.getAll();
        usernames=new ArrayList<>();
        for (String key:map.keySet()){
            usernames.add(new String[]{key,map.get(key)});
        }
        usernamesAdapter=new CommonAdapter<String[]>(this,
                new ArrayList<String[]>(),R.layout.simple_list_item4) {
            @Override
            public void convert(ViewHolder holder, String[] strings) {
                holder.setText(R.id.textView1,strings[0]);
            }
        };
        txt_userName = (EditText) findViewById(R.id.et_userName);
        txt_userName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                showPromptLv(hasFocus);
            }
        });
        txt_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPromptLv(true);
            }
        });
        txt_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str=s.toString();
                changeTextPrompt(str);
            }
        });
        txt_password = (EditText) findViewById(R.id.et_password);
        remenber_pwd = (CheckBox) findViewById(R.id.cb_remenber);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.btn_settings);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheckNum++;
                if(setCheckNum >= 5) {
                    setCheckNum = 0;
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, SettingsActivity.class);
                    intent.putExtra("title", "设置");
                    startActivity(intent);
                }
            }
        });

        //判断记住密码多选框的状态
        if (sharedPreferences.getBoolean("REMENBER_ISCHECK", false)) {
            //设置默认是记录密码状态
            remenber_pwd.setChecked(true);
            txt_userName.setText(sharedPreferences.getString("USER_NAME", ""));
            txt_password.setText(sharedPreferences.getString("PASSWORD", ""));
            //判断自动登陆多选框状态
            if (sharedPreferences.getBoolean("AUTO_ISCHECK", false)) {
                //设置默认是自动登录状态
                auto_login.setChecked(true);
                //跳转界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);

            }
        }

        class TomAsyncTask extends AsyncTask<String, Void, Map<String, Object>> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog.showDialog();
            }

            @Override
            protected void onPostExecute(Map<String, Object> stringObjectMap) {
                dialog.cancelDialog();//关闭ProgressDialog

                if(stringObjectMap == null){
                    Toast.makeText(LoginActivity.this, "无法连接到服务器！", Toast.LENGTH_LONG).show();
                    return;
                }
                //获取状态值
                String status = stringObjectMap.containsKey("status")?stringObjectMap.get("status").toString() : "";

                if ("1".equals(status)) {
                    PushUtils.token =stringObjectMap.get("token").toString();
                    Map<String, Object> userInfoMap = ((Map<String, Object>)stringObjectMap.get("userInfo"));
                    PushUtils.userId = userInfoMap.get("ID").toString();
                    PushUtils.userName= userInfoMap.get("NAME").toString();
                    PushUtils.userUnit= userInfoMap.get("BELONGS_UNIT").toString();
                    PushUtils.sysTitle= userInfoMap.get("TITLE").toString();
                    if(userInfoMap.containsKey("TERMINAL_PHONE") && userInfoMap.get("TERMINAL_PHONE") != null){
                        PushUtils.terminalPhone = userInfoMap.get("TERMINAL_PHONE").toString();
                    }else{
                        PushUtils.terminalPhone = "";
                    }

                    if (PushUtils.getServerIPText(LoginActivity.this) == "") {
                        PushUtils.setServerIPText(LoginActivity.this, "192.168.1.129:8080");
                    }
                    //Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if (remenber_pwd.isChecked()) {
                        //记住用户名、密码、
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("USER_NAME", txt_userName.getText().toString());
                        editor.putString("PASSWORD", txt_password.getText().toString());
                        editor.putString("FILL_UNITS", passwordValue);
                        editor.commit();
                        editor = users.edit();
                        editor.putString(txt_userName.getText().toString(),txt_password.getText().toString());
                        editor.commit();
                    }
                    PushUtils.setWareHouse(null);
                    Intent intent=null;
                        //跳转界面
                    intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    //将当前Activity移出栈
                    finish();

                }else if("00".equals(status)){
                    PushUtils.token =stringObjectMap.get("token").toString();
                    Map<String, Object> userInfoMap = ((Map<String, Object>)stringObjectMap.get("userInfo"));
                    PushUtils.userId = userInfoMap.get("ID").toString();
                    PushUtils.userName= userInfoMap.get("NAME").toString();
                    PushUtils.setWareHouse(stringObjectMap.get("wareHouse"));

//                    PushUtils.userUnit= userInfoMap.get("BELONGS_UNIT").toString();
//                    PushUtils.sysTitle= userInfoMap.get("TITLE").toString();
                    if(userInfoMap.containsKey("TERMINAL_PHONE") && userInfoMap.get("TERMINAL_PHONE") != null){
                        PushUtils.terminalPhone = userInfoMap.get("TERMINAL_PHONE").toString();
                    }else{
                        PushUtils.terminalPhone = "";
                    }

                    if (PushUtils.getServerIPText(LoginActivity.this) == "") {
                        PushUtils.setServerIPText(LoginActivity.this, "192.168.1.129:8080");
                    }
                    //Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    //登录成功和记住密码框为选中状态才保存用户信息
                    if (remenber_pwd.isChecked()) {
                        //记住用户名、密码、
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("USER_NAME", txt_userName.getText().toString());
                        editor.putString("PASSWORD", txt_password.getText().toString());
                        editor.putString("FILL_UNITS", passwordValue);
                        editor.commit();
                        editor = users.edit();
                        editor.putString(txt_userName.getText().toString(),txt_password.getText().toString());
                        editor.commit();
                    }
                    Intent intent=null;
                    intent=new Intent(LoginActivity.this,SafeManagerActivity.class);
                    intent.putExtra("title", "安全管理");
                    intent.putExtra("root", true);
                    startActivity(intent);
                    //将当前Activity移出栈
                    finish();

                } else if("2".equals(status)) {
                    Toast.makeText(LoginActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
                } else if("9999".equals(status)) {
                    Toast.makeText(LoginActivity.this, stringObjectMap.get("errmsg").toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "未知错误！", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected Map<String, Object> doInBackground(String... params) {
                String path = params[0];
                String jsonString1 = HttpUtils.getJsonContent(path);
                if (jsonString1 == null) {
                    return null;
                }
                Map<String, Object> map = JsonTools.getMap(jsonString1);
                return map;
            }
        }

        //登录监听事件，现在默认用户admin,密码123
        btn_login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("".equals(txt_userName.getText().toString())){
                            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if ("".equals(txt_password.getText().toString())){
                            Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        boolean isHas = NetworkUtils.isNetworkAvailable(LoginActivity.this);
                        if (isHas == false) {
                            Toast.makeText(LoginActivity.this, "网络未打开，请检查网络。", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (PushUtils.getServerIPText(LoginActivity.this) == "") {
                            Toast.makeText(LoginActivity.this, "服务IP和端口为空！请重新设置", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            userNameValue = URLEncoder.encode(txt_userName.getText().toString(), "utf-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        passwordValue = txt_password.getText().toString();
                        String PATH = "http://" + PushUtils.getServerIPText(LoginActivity.this) + "/rgyx/appUser/login";
                        String path = PATH + "?name=" + userNameValue + "&password=" + passwordValue;
                        new TomAsyncTask().execute(path);

                    }
                }
        );

        //监听记住密码多选框按钮事件
        remenber_pwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (remenber_pwd.isChecked()) {

                    System.out.println("记住密码已选中");
                    sharedPreferences.edit().putBoolean("REMENBER_ISCHECK", true).commit();

                } else {

                    System.out.println("记住密码没有选中");
                    sharedPreferences.edit().putBoolean("REMENBER_ISCHECK", false).commit();
                }

            }
        });

        //监听自动登录多选框事件
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (auto_login.isChecked()) {
                    System.out.println("自动登录已选中");
                    sharedPreferences.edit().putBoolean("AUTO_ISCHECK", true).commit();

                } else {
                    System.out.println("自动登录没有选中");
                    sharedPreferences.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });
    }

    /**
     * 初始化
     */
    private void init(){
        dialog = new ProgressDialog(LoginActivity.this);
    }
}
