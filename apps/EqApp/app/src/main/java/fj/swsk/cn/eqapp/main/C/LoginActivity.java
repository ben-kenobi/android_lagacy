package fj.swsk.cn.eqapp.main.C;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fj.swsk.cn.eqapp.R;
import fj.swsk.cn.eqapp.conf.IConstants;
import fj.swsk.cn.eqapp.main.Common.CommonAdapter;
import fj.swsk.cn.eqapp.main.Common.JsonTools;
import fj.swsk.cn.eqapp.main.Common.NetUtil;
import fj.swsk.cn.eqapp.main.Common.PushUtils;
import fj.swsk.cn.eqapp.main.Common.ViewHolder;
import fj.swsk.cn.eqapp.main.M.UserInfo;
import fj.swsk.cn.eqapp.subs.user.C.RetrievePwdActivity;
import fj.swsk.cn.eqapp.util.CommonUtils;
import fj.swsk.cn.eqapp.util.CustomizedDialog;
import fj.swsk.cn.eqapp.util.DialogUtil;
import fj.swsk.cn.eqapp.util.FileUtils;
import fj.swsk.cn.eqapp.util.HttpUtils;
import fj.swsk.cn.eqapp.util.NetworkUtils;
import fj.swsk.cn.eqapp.util.ResUtil;
import fj.swsk.cn.eqapp.util.SharePrefUtil;


/**
 * Created by apple on 16/2/26.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private EditText txt_userName, txt_password;
    private Button btn_login, retrieve,emergency;
    private int setCheckNum = 0; //设置按钮点击次数
    private ListView promptlv;
    private List<String[]> usernames=SharePrefUtil.getUsers();
    private CommonAdapter<String[]> usernamesAdapter;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        promptlv.setVisibility(View.GONE);
        return  super.onTouchEvent(event);
    }

    private void showPromptLv(boolean show){
        if(promptlv==null){
            promptlv=new ListView(this);

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
            FrameLayout fl = (FrameLayout)findViewById(android.R.id.content);
            fl.addView(promptlv);
        }
        int visi=show&&usernamesAdapter.getCount()>0 ? View.VISIBLE : View.GONE;
        promptlv.setVisibility(visi);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(txt_userName.getWidth(),-2);
        int pos[]=new int[2];
        txt_userName.getLocationOnScreen(pos);
        lp.leftMargin=pos[0];
        lp.topMargin=pos[1]+txt_userName.getHeight()- ResUtil.dp2Intp(25);
        promptlv.setLayoutParams(lp);


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
        if(promptlv!=null) {
            int visi = list.size() > 0 ? View.VISIBLE : View.GONE;
            promptlv.setVisibility(visi);
        }
    }

    @Override
    protected void onHideKeyboard() {
        showPromptLv(txt_userName.isFocused());
    }

    @Override
    protected void onShowKeyboard(int keyboardHeight) {
        showPromptLv(txt_userName.isFocused());

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        usernamesAdapter = new CommonAdapter<String[]>(this,
                new ArrayList<String[]>(), R.layout.simple_list_item4) {
            @Override
            public void convert(ViewHolder holder, String[] strings) {
                holder.setText(R.id.textView1, strings[0]);
            }
        };

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
                String str = s.toString();
                changeTextPrompt(str);
            }
        });

        this.attachKeyboardListeners();
        txt_userName.setText(SharePrefUtil.getUsername());
        txt_password.setText(SharePrefUtil.getPwd());
//        txt_userName.setText("张三");
//        txt_password.setText("1");


        // 添加测试数据
//        usernames.clear();
//        usernames.add(new String[]{"test","1"});
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(PushUtils.shouldAutoLogin()){
            login();
        }
    }

    private void emergencyLogin(){
        if ("".equals(txt_userName.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(txt_password.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_LONG).show();
            return;
        }

        String username =  txt_userName.getText().toString();
        String pwd = txt_password.getText().toString();
        for(String[] strary : usernames){
            if(username.equals(strary[0])&&pwd.equals(strary[1])){
                SharePrefUtil.saveUser(txt_userName.getText().toString(), txt_password.getText().toString());
                String json = SharePrefUtil.getByUser("userInfo","");
                PushUtils.setLoginUser(JsonTools.getInstance(json, new TypeReference<UserInfo>() {
                }));
                PushUtils.loginUser.setNAME(username);
                initMapData();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return ;
            }
        }

//        SharePrefUtil.saveUser(txt_userName.getText().toString(), txt_password.getText().toString());
//        PushUtils.setLoginUser(new UserInfo());
//        PushUtils.loginUser.setName(username);
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();
        CommonUtils.toast("用户名密码不匹配");
    }
    private void login(){
        if ("".equals(txt_userName.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(txt_password.getText().toString())) {
            Toast.makeText(LoginActivity.this, "请输入密码!", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isHas = NetworkUtils.isNetworkAvailable(LoginActivity.this);
        if (isHas == false) {
            NetUtil.noNetWorkToast();
            return;
        }
        if (HttpUtils.getServerIPText() == "") {
            Toast.makeText(LoginActivity.this, "服务IP和端口为空！请重新设置", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = IConstants.loginUrl + "?name=" + CommonUtils.UrlEnc(txt_userName.getText().toString()) + "&pwd=" + txt_password.getText().toString();
        NetUtil.commonRequest(LoginActivity.this, path, new NetUtil.RequestCB() {
            @Override
            public void cb(Map<String, Object> map, String resp, int flag) {
                Map<String, Object> userInfoMap = ((Map<String, Object>) map.get("data"));
                PushUtils.setLoginUser(JsonTools.getInstance(JsonTools.getJsonString(userInfoMap), new TypeReference<UserInfo>() {
                }));

                SharePrefUtil.saveUser(txt_userName.getText().toString(), txt_password.getText().toString());
                SharePrefUtil.putByUser("userInfo", JsonTools.getJsonString(userInfoMap));
                initMapData();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        if(v==btn_login){
            login();
        }else if (v.getId()==R.id.btn_settings){
//            Uri uri = Uri.fromFile(getFilesDir());
//            Intent intent = new Intent("com.yf.filesystem.FILESYSTEM");
////            intent.setData(uri);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            intent.putExtra("directory",getFilesDir());
//            startActivity(intent);
//if (true)
//            return ;

            setCheckNum++;
            if (setCheckNum >= 5) {
                setCheckNum = 0;
                final CustomizedDialog d = CustomizedDialog.initEditDialog(LoginActivity.this);
                d.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HttpUtils.setServerIPText(d.editText.getText().toString());
                    }
                }).setNegativeButton("取消", null);
                d.editText.setText(HttpUtils.getServerIPText());
                d.editText.setHint("输入要设置的ip:port");
                d.editText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((EditText)v).setText(IConstants.defServerIP);
                        return true;
                    }
                });
                d.show();

            }
        }else if(v==retrieve){
            Intent intent = new Intent(LoginActivity.this, RetrievePwdActivity.class);
            startActivity(intent);
        }else if(v==emergency){
//            emergency.setSelected(!emergency.isSelected());
            emergencyLogin();

        }
    }

    private void init(){
        txt_userName = (EditText) findViewById(R.id.et_userName);
        txt_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        retrieve=(Button)findViewById(R.id.btn_retrieve);
        emergency=(Button)findViewById(R.id.btn_emergency);
    }

    private void initMapData() {
        Dialog dialog = DialogUtil.progressD2(LoginActivity.this, "数据初始化...");
        dialog.show();
        String dir = FileUtils.getSDPath() + "/eqAppData/mapLayer/";
        if (FileUtils.isFileExists(dir)) {
            return;
        }

        try {
            FileUtils.CopyAssets(this, "mapLayer", dir);
        } catch (Exception e) {
            finish();
        } finally {
            dialog.dismiss();
        }

    }
}
