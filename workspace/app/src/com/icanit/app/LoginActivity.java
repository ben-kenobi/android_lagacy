package com.icanit.app;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app.common.IConstants;
import com.icanit.app.entity.EntityMapFactory;
import com.icanit.app.ui.CustomizedDialog;
import com.icanit.app.util.AppUtil;
import com.icanit.app.util.SharedPreferencesUtil;

public class LoginActivity extends Activity implements OnClickListener {
	private Button backButton, forgotpswButton, loginButton;
	private CheckedTextView rememberMe, autoLogin;
	private TextView freeRegister;
	private EditText username, password;
	private ImageButton usernameDisposer, passwordDisposer;
	private LinearLayout otherAccount;
	private String phoneTx, passwordTx;
	private boolean contentSaveIf=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();bindListeners();
	}
	@Override
	protected void onResume() {
		super.onResume();
		restoreSetup();
	}
	@Override
	protected void onStop() {
		super.onStop();
		saveSetup();
	}
	
	private void bindListeners() {
		freeRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

		AppUtil.bindBackListener(backButton);
		loginButton.setOnClickListener(this);
		rememberMe.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (rememberMe.isChecked()) {
					rememberMe.setChecked(false);
					autoLogin.setChecked(false);
				} else
					rememberMe.setChecked(true);
			}
		});
		autoLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (autoLogin.isChecked())
					autoLogin.setChecked(false);
				else {
					autoLogin.setChecked(true);
					rememberMe.setChecked(true);
				}
			}
		});
		forgotpswButton.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,RetrievePasswordActivity1.class));
			}
		});

		AppUtil.bindEditTextNtextDisposer(username, usernameDisposer);
		AppUtil.bindEditTextNtextDisposer(password, passwordDisposer);

	}

	private void init() {
		backButton = (Button) findViewById(R.id.button1);
		forgotpswButton = (Button) findViewById(R.id.button2);
		freeRegister = (TextView) findViewById(R.id.textView2);
		loginButton = (Button) findViewById(R.id.button3);
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		usernameDisposer = (ImageButton) findViewById(R.id.imageButton1);
		passwordDisposer = (ImageButton) findViewById(R.id.imageButton2);
		otherAccount = (LinearLayout) findViewById(R.id.linearLayout1);
		rememberMe = (CheckedTextView) findViewById(R.id.checkedTextView1);
		autoLogin = (CheckedTextView) findViewById(R.id.checkedTextView2);
	}

	@Override
	public void onClick(View v) {
		phoneTx = username.getText().toString();
		passwordTx = password.getText().toString();
		if (!AppUtil.isPhoneNum(phoneTx)) {
			Toast.makeText(this, "无效格式号码", Toast.LENGTH_LONG).show();
			return;
		} else if (!AppUtil.isPassword(passwordTx)) {
			Toast.makeText(this, "密码格式错误", Toast.LENGTH_LONG).show();
			return;
		}
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setTitle("用户登录");
		pd.setMessage("正在登录。。。");
		new AsyncTask<String, Void, String>() {
			protected void onPreExecute() {
				pd.show();
			}
			protected String doInBackground(String... arg0) {
				try {
				return AppUtil.loginOnServer(arg0[0],arg0[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(String result) {
				pd.dismiss();
				CustomizedDialog.Builder builder = new CustomizedDialog.Builder(LoginActivity.this);
				builder.setTitle("通知");
				if(result!=null&&!"".equals(result)){
					try {
						if(AppUtil.loginOnClient(phoneTx, passwordTx,result)){
							contentSaveIf=true;
							builder.setMessage("登录成功\n返回主页");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									startActivity(new Intent(LoginActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
											setAction(IConstants.AFTER_LOGIN));
								}
							});
							builder.create().show();
						} else {
							builder.setMessage("登录失败\n账号或密码错误");
							builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
								}
							});
							builder.create().show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}else{
					builder.setMessage("登录失败\n抱歉。。。");
					builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					builder.create().show();
				}
			}

		}.execute(phoneTx,passwordTx);

	}
	
	
	private void restoreSetup(){
		username.setText(null);
		SharedPreferencesUtil share=AppUtil.getSharedPreferencesUtilInstance();
		String phone = share.get(null, IConstants.PHONE);
		String password =share.get(null,IConstants.PASSWORD);
		String autoLogin= share.get(null, IConstants.AUTO_LOGIN);
		if(phone!=null) this.username.setText(phone);
		if(password!=null){
			this.password.setText(password);
			rememberMe.setChecked(true);
			if(autoLogin!=null&&"true".equals(autoLogin)){
				this.autoLogin.setChecked(true);
			}
		}
	}
	private void saveSetup(){
		SharedPreferencesUtil share = AppUtil.getSharedPreferencesUtilInstance();
		if(rememberMe.isChecked()){
			if(contentSaveIf)
			share.put(null, EntityMapFactory.generateUser(phoneTx, passwordTx));
			if(autoLogin.isChecked()){
				share.put(null,IConstants.AUTO_LOGIN,"true");
			}else{
				share.put(null, IConstants.AUTO_LOGIN,"false");
			}
		}else{
			share.clear(null);
		}
	}

}
