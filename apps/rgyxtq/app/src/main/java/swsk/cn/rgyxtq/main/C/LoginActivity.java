package swsk.cn.rgyxtq.main.C;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import swsk.cn.rgyxtq.R;
import swsk.cn.rgyxtq.conf.IConstants;
import swsk.cn.rgyxtq.main.Common.JsonTools;
import swsk.cn.rgyxtq.main.Common.PushUtils;
import swsk.cn.rgyxtq.subs.work.Common.NetUtil;
import swsk.cn.rgyxtq.util.BaseUtils;
import swsk.cn.rgyxtq.util.CommonUtils;
import swsk.cn.rgyxtq.util.DialogUtil;
import swsk.cn.rgyxtq.util.HttpUtils;
import swsk.cn.rgyxtq.util.NetworkUtils;

/**
 * Created by apple on 16/2/26.
 */
public class LoginActivity extends Activity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener,Runnable{

    private EditText txt_userName,txt_password;
    private CheckBox remenber_pwd,auto_login;
    private Button btn_login,btn_cancel;
    private String userNameValue,passwordValue;
    private SharedPreferences sharedPreferences;
    private int setCheckNum = 0;
    private Dialog dialog = null;
    private String loginurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = DialogUtil.progressD(this);
        String ipstr = PushUtils.getServerIPText(this);
        init();

    }
    private  void init(){
        sharedPreferences = this.getSharedPreferences("userInfo", 1);
        txt_userName = (EditText) findViewById(R.id.et_userName);
        txt_password = (EditText) findViewById(R.id.et_password);
        remenber_pwd = (CheckBox) findViewById(R.id.cb_remenber);
        auto_login = (CheckBox) findViewById(R.id.cb_auto);
        btn_login = (Button) findViewById(R.id.btn_login);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.btn_settings);

        btn_settings.setOnClickListener(this);
        if(sharedPreferences.getBoolean("REMENBER_ISCHECK",false)){
            remenber_pwd.setChecked(true);
            txt_userName.setText(sharedPreferences.getString("USER_NAME", ""));
            txt_password.setText(sharedPreferences.getString("PASSWORD",""));
            if(sharedPreferences.getBoolean("AUTO_ISCHECK",false)){
                auto_login.setChecked(true);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
        btn_login.setOnClickListener(this);
        remenber_pwd.setOnCheckedChangeListener(this);
        auto_login.setOnCheckedChangeListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_settings){
            setCheckNum++;
            if(setCheckNum>=5){
                setCheckNum=0;
                Intent intent = BaseUtils.titleIntent("设置");
                intent.setClass(this, SettingActivity.class);
                startActivity(intent);

            }
        }else if(v==btn_login){
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
                NetUtil.noNetWorkToast();
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
            loginurl = "rgyx/appUser/login" + "?name=" + userNameValue + "&password=" + passwordValue;

            dialog.show();

            IConstants.THREAD_POOL.submit(this);



        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String key="";
        if(buttonView==remenber_pwd){
            key = "REMENBER_ISCHECK";

        }else if(buttonView==auto_login){
            key = "AUTO_ISCHECK";
        }
        sharedPreferences.edit().putBoolean(key,remenber_pwd.isChecked()).commit();

    }

    @Override
    public void run() {
        String resp=HttpUtils.getJsonContent(loginurl);
         Map<String,Object> map1 = null;
        if(resp!=null){
            map1 = JsonTools.getMap(resp);
        }
        final Map<String,Object> map =map1;
        IConstants.MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                if(map==null){
                    NetUtil.connectFailToast();
                    return ;
                }
                String status = map.containsKey("status")?map.get("status").toString():"";
                if("1".equals(status)){
                    PushUtils.token =map.get("token").toString();
                    Map<String, Object> userInfoMap = ((Map<String, Object>)map.get("userInfo"));
                    PushUtils.userId = userInfoMap.get("ID").toString();
                    PushUtils.userName= userInfoMap.get("NAME").toString();
                    PushUtils.userUnit= userInfoMap.get("BELONGS_UNIT").toString();
                    PushUtils.sysTitle= userInfoMap.get("TITLE").toString();
                    if(userInfoMap.containsKey("TERMINAL_PHONE") && userInfoMap.get("TERMINAL_PHONE") != null){
                        PushUtils.terminalPhone = userInfoMap.get("TERMINAL_PHONE").toString();
                    }else{
                        PushUtils.terminalPhone = "";
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
                    }
                    //跳转界面
                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
                    startActivity(intent);
                    //将当前Activity移出栈
                    finish();

                }else if("9999".equals(status)||"2".equals(status)){
                    CommonUtils.toast(map.get("errmsg").toString());
                }else {
                    CommonUtils.toast("未知错误");
                }

            }
        });
    }
}
