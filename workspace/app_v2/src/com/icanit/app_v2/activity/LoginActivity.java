package com.icanit.app_v2.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.icanit.app_v2.R;
import com.icanit.app_v2.common.IConstants;
import com.icanit.app_v2.util.AppUtil;
import com.icanit.app_v2.util.DialogUtil;
import com.icanit.app_v2.util.SharedPreferencesUtil;

public class LoginActivity extends Activity implements OnClickListener {
	private int resId = R.layout.activity_login;
	private Button phoneNumDisposer, passwordDisposer, register, login;
	private ImageButton backButton;
	private EditText phoneNum, password;
	private TextView retrievePassword;
	private CheckedTextView autoLogin;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(resId);
		init();
		bindListeners();
	}

	public void onResume() {
		super.onResume();
		restoreSetup();
	}

	private void init() {
		phoneNumDisposer = (Button) findViewById(R.id.button1);
		passwordDisposer = (Button) findViewById(R.id.button2);
		register = (Button) findViewById(R.id.button3);
		login = (Button) findViewById(R.id.button4);
		backButton = (ImageButton) findViewById(R.id.imageButton1);
		phoneNum = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		retrievePassword = (TextView) findViewById(R.id.textView1);
		autoLogin = (CheckedTextView) findViewById(R.id.checkedTextView1);
	}

	private void bindListeners() {
		AppUtil.bindBackListener(backButton);
		AppUtil.bindEditTextNtextDisposer(phoneNum, phoneNumDisposer);
		AppUtil.bindEditTextNtextDisposer(password, passwordDisposer);
		retrievePassword.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				startActivity(new Intent(LoginActivity.this,
						RetrievePasswordActivity1.class));
				AppUtil.putIntoApplication(
						IConstants.DESTINATION_AFTER_RETRIEVEPASSWORD,
						LoginActivity.class);
			}
		});
		register.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				AppUtil.putIntoApplication(
						IConstants.DESTINATION_AFTER_REGISTER,
						LoginActivity.class);
			}
		});
		login.setOnClickListener(this);
		AppUtil.setOnClickListenerForCheckedTextView(autoLogin);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (AppUtil.getLoginUser() != null)
			finish();
	}

	private void restoreSetup() {
		phoneNum.setText(null);
		SharedPreferencesUtil share = AppUtil
				.getSharedPreferencesUtilInstance();
		String phone = share.getReservedUserInfo(IConstants.PHONE);
		String password = share.getReservedUserInfo(IConstants.PASSWORD);
		String autoLogin = share.getReservedUserInfo(IConstants.AUTO_LOGIN);
		if (phone != null)
			this.phoneNum.setText(phone);
		if (password != null && autoLogin != null && "true".equals(autoLogin)) {
			this.password.setText(password);
			this.autoLogin.setChecked(true);
		}
	}

	private void saveSetup(String phone, String pwd, boolean autoLogin) {
		AppUtil.getSharedPreferencesUtilInstance().saveLoginUserInfo(phone,
				pwd, autoLogin);
	}

	@Override
	public void onClick(View v) {
		final String phoneTx = phoneNum.getText().toString();
		final String passwordTx = password.getText().toString();
		if (!AppUtil.isPhoneNum(phoneTx)) {
			Toast.makeText(this, "无效格式号码", Toast.LENGTH_LONG).show();
			return;
		} else if (!AppUtil.isPassword(passwordTx)) {
			Toast.makeText(this, "密码格式错误", Toast.LENGTH_LONG).show();
			return;
		}
		DialogUtil.loginFlow(LoginActivity.this, phoneTx, passwordTx, autoLogin.isChecked(), new Runnable(){
			public void run(){
				setResult(12, new Intent().putExtra("key", "loginActivity"));
				LoginActivity.this.finish();
			}
		});
	}
}
